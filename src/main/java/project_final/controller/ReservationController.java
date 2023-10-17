package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.entity.Reservation;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.model.dto.request.ReservationRequest;
import project_final.repository.IUserRepository;
import project_final.security.UserPrinciple;
import project_final.service.IReservationService;
import project_final.service.ITableService;
import project_final.service.IUserService;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final IReservationService reservationService;
    private final IUserService userService;
    private final ITableService tableService;

    @GetMapping
    public String getAll(Model model, @RequestParam Date date,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size) {
        if (date == null) {
            date = new Date(Long.MIN_VALUE);
        }
        model.addAttribute("date", date);
        model.addAttribute("reservations", reservationService.findAll(date, page, size));
        return "/dashboard/page/reservation/list";
    }



    @PostMapping("/add")
    public String addReservation(@ModelAttribute("reservation") ReservationRequest reservationRequest, HttpSession session) {
        Reservation reservation = (Reservation) session.getAttribute("reservationLocal");
        reservationService.save(reservationRequest,reservation);
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    public String edir(Model model, @PathVariable Long id) {
        model.addAttribute("reservation", reservationService.findById(id));
        return "/dashboard/page/reservation/add";
    }

    @PostMapping("/update")
    public String updateReservation(@ModelAttribute("reservation") ReservationRequest reservationRequest, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        reservationRequest.setUser(user);
//        reservationService.save(reservationRequest);
        return "redirect:/";
    }

    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id) {
        reservationService.confirm(id);
        return "redirect:/";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        reservationService.cancel(id, user);
        return "redirect:/";
    }
}
