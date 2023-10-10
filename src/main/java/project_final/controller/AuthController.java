package project_final.controller;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import project_final.exception.RegisterException;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.UserRequest;

import project_final.service.IMailService;
import project_final.service.IUserService;

import project_final.security.UserPrinciple;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpSession;
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

    @PostMapping("/sign-in")
    public String signIn(HttpSession session, LoginRequestDto loginRequestDto ) throws LoginException {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );
            // tạo đối tượng authentication để xác thực thông qua username và password

        } catch (AuthenticationException e) {
            throw new LoginException("Username or password is incorrect!");
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        session.setAttribute("currentUser",userPrinciple);
        return "redirect:/home";
    }
    @GetMapping("/logout")
    public  String logout(HttpSession session){
        session.removeAttribute("currentUser");
        return "redirect:/home";
    }


}
