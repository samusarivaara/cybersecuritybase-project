package sec.project.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class AdminController {
    
    @Autowired
    private SignupRepository signupRepository;
    
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String submitForm(Authentication authentication, Model model) {
                
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
        
        // Vulnerability #3 2013-A7-Missing Function Level Access Control
        // Any authenticated user can access admin url

        // Fix me:
        // if (!authorized) {
            // redirect to main page
        //    return "redirect:/form";
        //}

        // TODO: fix this
        List<Signup> list = signupRepository.findAll();
        List<String> names = new ArrayList<String>();
        for (Signup item: list) {
            names.add(item.getName());
        }
        model.addAttribute("list" , names);
        return "queryresults";
    }
}
