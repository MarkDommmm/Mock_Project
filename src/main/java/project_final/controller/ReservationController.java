package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.entity.User;
import project_final.model.dto.request.ReservationRequest;
import project_final.service.IReservationService;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final IReservationService reservationService;
    @GetMapping
    public String getAll(Model model, @RequestParam(defaultValue = "") Date date,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("reservations", reservationService.findAll(date, page, size));
        return "/dashboard/page/reservation/list";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("reservation",new ReservationRequest());
        return  "/dashboard/page/reservation/add";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute("reservation") ReservationRequest reservationRequest, HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        reservationRequest.setUser(user);
        reservationService.save(reservationRequest);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edir(Model model,@PathVariable Long id){
        model.addAttribute("reservation",reservationService.findById(id));
        return  "/dashboard/page/reservation/add";
    }

    @PostMapping("/update")
    public String updateReservation(@ModelAttribute("reservation") ReservationRequest reservationRequest, HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        reservationRequest.setUser(user);
        reservationService.save(reservationRequest);
        return "redirect:/";
    }

    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id){
        reservationService.confirm(id);
        return "redirect:/";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id,HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        reservationService.cancel(id, user);
        return "redirect:/";
    }
}
