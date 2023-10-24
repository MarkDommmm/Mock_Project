package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import project_final.repository.*;
import project_final.security.UserPrinciple;
import project_final.service.*;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class AdminController {

    private final IMenuService menuService;
    private final IPaymentRepository paymentRepository;
    private final IMenuRepository menuRepository;
    private final IReservationRepository reservationRepository;
    private final IUserRepository userRepository;
    private final ITableRepository tableRepository;
    private final ITableTypeRepository tableTypeRepository;
    private final ICategoryRepository categoryRepository;
    private final IReviewRepository reviewRepository;

    @RequestMapping("/user")
    public String User(@AuthenticationPrincipal UserPrinciple userPrinciple, HttpSession session) {
        session.setAttribute("currentUser", userPrinciple);
        return "dashboard/ChoseTable";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "dashboard/page/user/user-list";
    }

    @RequestMapping("/403")
    public String error403() {
        return "/dashboard/errors/error403";
    }

    @RequestMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("table", tableRepository.findAll());
        model.addAttribute("user", userRepository.findAll());
        model.addAttribute("payment", paymentRepository.findAll());
        model.addAttribute("category", categoryRepository.findAll());
        model.addAttribute("menu", menuRepository.findAll());
        model.addAttribute("reservation", reservationRepository.findAll());
        model.addAttribute("tableType", tableTypeRepository.findAll());
        model.addAttribute("review", reviewRepository.findAll());
        model.addAttribute("menuTopList", menuService.findTopMenusByMonth());
        return "/dashboard/dashboard";
    }

}
