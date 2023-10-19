package project_final.controller;

import lombok.AllArgsConstructor;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.exception.CustomsException;
import project_final.exception.ForgotPassWordException;
import project_final.exception.RegisterException;
import project_final.model.dto.request.ForgotPassForm;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.UpdateUserRequest;
import project_final.model.dto.request.UserRequest;

import project_final.model.dto.response.TableMenuCartResponse;
import project_final.service.*;

import project_final.security.UserPrinciple;
import project_final.service.impl.GenerateExcelService;

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

    private final IReservationService reservationService;
    private final ITableMenuService tableMenuService;

 

    @GetMapping("/profile/{id}")
    private ModelAndView profile(@PathVariable("id") Long id,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("reservation", reservationService.findByUserId(page, size, id));
        return new ModelAndView("/dashboard/page/user/user-profile", "profile", userService.findById(id));
    }
    @GetMapping("/get-order-pending/{id}")
    private ModelAndView getOrderByStatusPending(@PathVariable("id") Long id,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("reservation", reservationService.findByUserIdAndStatusPending(page, size, id));
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

    @GetMapping("/status/{id}")
    public String lockAndUnlock(@PathVariable("id") Long id){
        userService.lock(id);
        return "redirect:/user";
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



}
