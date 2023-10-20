package project_final.controller;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.exception.RegisterException;
import project_final.model.dto.request.UpdateUserRequest;

import project_final.model.dto.response.TableMenuCartResponse;
import project_final.service.*;

import java.util.List;


@Controller
@AllArgsConstructor

@RequestMapping("/auth")
public class AuthController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;

    private final IReservationService reservationService;
    private final IReservationMenuService tableMenuService;

 

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
