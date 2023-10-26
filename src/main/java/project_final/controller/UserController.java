package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project_final.service.IUserService;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @GetMapping
    public String findAll(Model model,@RequestParam(defaultValue = "") String name,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size ){
        model.addAttribute("users", userService.findAll(name, page, size));
        model.addAttribute("name", name);
    return "dashboard/page/user/user-list";
    }

    @GetMapping("/status/{id}")
    public String getStatus(@PathVariable Long id){
        userService.lock(id);
        return "redirect:/user";
    }

}
