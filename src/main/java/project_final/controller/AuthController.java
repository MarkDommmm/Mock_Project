package project_final.controller;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.entity.User;
import project_final.exception.RegisterException;
import project_final.model.dto.request.ForgotPassForm;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.UpdateUserRequest;
import project_final.model.dto.request.UserRequest;

import project_final.model.dto.response.UserResponse;
import project_final.service.IMailService;
import project_final.service.IUserService;

import project_final.security.UserPrinciple;

import javax.management.monitor.StringMonitor;
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
        return "redirect:/home/sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(HttpSession session, LoginRequestDto loginRequestDto) throws LoginException {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );

        } catch (AuthenticationException e) {
            throw new LoginException("Username or password is incorrect!");
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        session.setAttribute("currentUser", userPrinciple);
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/home";
    }

    @GetMapping("/profile/{id}")
    private ModelAndView profile(@PathVariable("id") Long id) {
        return new ModelAndView("/dashboard/app/user-profile", "profile", userService.findById(id));
    }

    @GetMapping("/edit/{id}")
    private ModelAndView edit(@PathVariable("id") Long id) {
        return new ModelAndView("/dashboard/app/user-update", "profile", userService.findById(id));
    }

    @PostMapping("/update")
    private String update(@ModelAttribute("profile") UpdateUserRequest userRequest) throws RegisterException {
        userService.update(userRequest);
        return "redirect:/home";
    }

    @GetMapping("/change-password/{id}")
    private ModelAndView changePassword(@PathVariable("id") Long id) {
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setId(id);
        return new ModelAndView("/dashboard/app/user-change-pass", "changePass",userRequest );
    }

    @PostMapping("/change-password")
    private String changePass(@ModelAttribute("changePass") UpdateUserRequest userRequest) throws RegisterException {
        userService.changePass(userRequest);
        return "redirect:/home";
    }
    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("user",new ForgotPassForm());
        return "/dashboard/app/user-forgot-pass";
    }
    @PostMapping("/forgot-password")
    public String sendVerification(@ModelAttribute("user") ForgotPassForm forgotPassForm){

        String verification = userService.sendVerification(forgotPassForm.getEmail());
        String emailContent = "<p style=\"color: red; font-size: 18px;\">\n" + verification + "</p>";
        mailService.sendMail(forgotPassForm.getEmail(), "Verification", emailContent);
        return "redirect: /forgot-password";
    }



}
