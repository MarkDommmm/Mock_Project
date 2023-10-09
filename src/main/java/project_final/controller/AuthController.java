package project_final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

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
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IMailService mailService;


    @PostMapping("/register")
    public String register(@ModelAttribute @Valid UserRequest userRequest) throws RegisterException {
        userService.save(userRequest);
        String emailContent = "<p style=\"color: red; font-size: 18px;\">\n" + "Registered successfully</p>";
        mailService.sendMail(userRequest.getEmail(), "RegisterSuccess", emailContent);
        return "redirect:/home/sign-in";
    }

    @PostMapping("/sign-in")
    public String login(@Valid @ModelAttribute LoginRequestDto loginRequestDto) throws LoginException {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new LoginException("Username or password is incorrect!");
        }
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        if (!userPrinciple.isStatus()) {
            throw new LoginException("Account is locked");
        }

        return "redirect:/home";

    }
}
