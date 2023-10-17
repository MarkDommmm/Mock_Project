package project_final.controller;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.exception.ForgotPassWordException;
import project_final.exception.RegisterException;
import project_final.model.dto.request.ForgotPassForm;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.UpdateUserRequest;
import project_final.model.dto.request.UserRequest;

import project_final.model.dto.response.TableMenuCartResponse;
import project_final.service.*;

import project_final.security.UserPrinciple;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final IMailService mailService;
    private final IReservationService reservationService;
    private final ITableMenuService tableMenuService;

    @PostMapping("/register")
    public String register(@ModelAttribute("signup") @Valid UserRequest userRequest, BindingResult bindingResult) throws RegisterException {
        if (bindingResult.hasErrors()) {
            return "redirect:/home/sign-up";
        }
        userService.save(userRequest);
        return "redirect:/home/sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(HttpSession session, @Valid @ModelAttribute("signin") LoginRequestDto loginRequestDto, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            return "redirect:/home/sign-in";
        }
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
    private ModelAndView profile(@PathVariable("id") Long id,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("reservation", reservationService.findByUserId(page, size, id));

        return new ModelAndView("/dashboard/page/user/user-profile", "profile", userService.findById(id));
    }

    @GetMapping("/reservation-detail")
    @ResponseBody
    private List<TableMenuCartResponse> detailOrder(@RequestParam(defaultValue = "0") Long id) {
        return tableMenuService.getDetails(id);
    }

    @GetMapping("/edit/{id}")
    private ModelAndView edit(@PathVariable("id") Long id) {
        return new ModelAndView("/dashboard/page/user/user-update", "profile", userService.findById(id));
    }

    @PostMapping("/update")
    private String update(@ModelAttribute("profile") UpdateUserRequest userRequest) throws RegisterException {
        userService.update(userRequest);
        return "redirect:/home";
    }

    @GetMapping("/change-password/{id}")
    private ModelAndView changePassword(@PathVariable("id") Long id) {
        return new ModelAndView("/dashboard/page/user/user-change-pass", "user", userService.findById(id));
    }

    @PostMapping("/change-password")
    private String changePass(@ModelAttribute("changePass") UpdateUserRequest userRequest) throws RegisterException {
        userService.changePass(userRequest);
        return "redirect:/home";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("user", new ForgotPassForm());
        return "/dashboard/app/user-forgot-pass";
    }

    @PostMapping("/forgot-password")
    public String sendVerification(@ModelAttribute("user") ForgotPassForm forgotPassForm) throws ForgotPassWordException {
        if (forgotPassForm.getVerification() == null) {
            String verification = userService.sendVerification(forgotPassForm.getEmail());
            String emailContent = "<p style=\"color: red; font-size: 18px;\">\n" + "Your confirmation code is: " + verification + "</p>";
            mailService.sendMail(forgotPassForm.getEmail(), "Verification", emailContent);
            return "/dashboard/app/user-forgot-pass";
        }
        userService.passwordRetrieval(forgotPassForm);
        return "redirect: /home";
    }


}
