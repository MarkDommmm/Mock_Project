package project_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @GetMapping
    public String listUsers() {
        return "/dashboard/page/user/user-list";
    }
    @GetMapping("/add-user")
    public String addUser(){
        return "/dashboard/page/user/user-add";
    }
}
