package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/custom_logout", method = RequestMethod.GET)
    public String logoutMapping() {

        return "custom_logout";
    }


    @RequestMapping(value = "/shortcut", method = RequestMethod.GET)
    public String shortcutMapping(@RequestParam String url) {

        // Vulnerability #5 2013-A10-Unvalidated Redirects and Forwards
        // Request parameter url is not validated.
        return "redirect:" + url;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Authentication authentication, Model model) {

        User user = (User) authentication.getPrincipal();
        model.addAttribute("username", user.getUsername());
        return "form";
    }

    @RequestMapping(value = "/done/{userid}", method = RequestMethod.GET)
    public String done(Model model, @PathVariable String userid) {

        // Vulnerability #4 2013-A4-Insecure Direct Object References
        // Authenticated user can access directly to other user's
        // resource by guessing his username; path variable  /done/{userid}
        List<String> list = new ArrayList<String>();

        List<Signup> all = signupRepository.findAll();

        for (Signup item: all) {
            if (item.getEnteredByUser().equals(userid)) {
                list.add("Name: " + item.getName() + " Address: " +item.getAddress());
            }
        }
        model.addAttribute("list" , list);
        return "done";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, Model model,
            @RequestParam String name, @RequestParam String address) {

        User user = (User) authentication.getPrincipal();
        signupRepository.save(new Signup(name, address, user.getUsername()));
        model.addAttribute("username", user.getUsername());
        return "redirect:/done/" + user.getUsername();
    }

}
