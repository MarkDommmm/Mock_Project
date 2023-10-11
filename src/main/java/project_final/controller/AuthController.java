package project_final.controller;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.exception.RegisterException;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.UserRequest;

import project_final.model.service.impl.mail.IMailService;
import project_final.model.service.impl.user.IUserService;

import project_final.security.user_principle.UserPrinciple;

import javax.security.auth.login.LoginException;

import javax.validation.Valid;


@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final IMailService mailService;


    @PostMapping("/register")
    public String register(@ModelAttribute @Valid UserRequest userRequest) throws RegisterException {
        userService.save(userRequest);
        String emailContent = "<p style=\"color: red; font-size: 18px;\">\n" + "Registered successfully</p>";
        mailService.sendMail(userRequest.getEmail(), "RegisterSuccess", emailContent);
        return "redirect:/home/sign-in";
    }
//aaaaaaaaaaaaaaaaaaaaaaaaaaaaa
}
