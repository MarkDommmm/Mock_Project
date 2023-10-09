package project_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.UserRequest;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "dashboard/ChoseTable";
    }
    @RequestMapping("/403")
    public String error403() {
        return "/dashboard/errors/error403";
    }
 
    @RequestMapping("/home/sign-in")
    public String signIn(Model model) {
        model.addAttribute("signin", new LoginRequestDto());
        return "dashboard/auth/sign-in";
    }

    @RequestMapping("/home/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signup", new UserRequest());
        return "dashboard/auth/sign-up";
    }


}
