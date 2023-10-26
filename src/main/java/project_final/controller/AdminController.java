package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project_final.entity.User;
import project_final.model.domain.GooglePojo;
import project_final.model.dto.request.UserRequest;
import project_final.repository.*;
import project_final.security.UserPrinciple;
import project_final.service.*;
import project_final.util.GoogleUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    private final GoogleUtils googleUtils;

    @GetMapping("/login-google")
    public String loginGoogle(HttpServletRequest request, Model model,HttpSession session) throws IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            return "redirect:/login?google=error";
        }
        String accessToken = googleUtils.getToken(code);
        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        UserDetails userDetail = googleUtils.buildUser(googlePojo);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/home";
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
