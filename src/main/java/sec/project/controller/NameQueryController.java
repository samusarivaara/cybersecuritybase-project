package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.repository.SignupRepositoryImpl;

/**
 *
 * Controller responsible for queries.
 */

@Controller
public class NameQueryController {

    @Autowired
    private SignupRepositoryImpl signupRepository;
    
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, Model model,
            @RequestParam String nameHint) {

        // Vulnerability #1 OWASP 2013-A1-Injection.
        // Authenticated user can inject SQL through nameHint parameter.
        model.addAttribute("list" , signupRepository.getSignupsByName(nameHint));
        return "queryresults";
    }
}
