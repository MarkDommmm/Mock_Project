package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project_final.entity.Reservation;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.exception.TimeIsValidException;
import project_final.model.dto.request.ReservationRequest;
import project_final.repository.IUserRepository;
import project_final.security.UserPrinciple;
import project_final.service.IReservationService;
import project_final.service.ITableMenuService;
import project_final.service.ITableService;
import project_final.service.IUserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final IReservationService reservationService;
    private final IUserService userService;
    private final ITableService tableService;
    private final ITableMenuService tableMenuService;

    @GetMapping
    public String getAll(Model model,
                         @RequestParam(name = "date", required = false)
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size) {
        if (date == null) {
            date = new Date();
        }
        model.addAttribute("date", date);
        model.addAttribute("reservations", reservationService.findAll(date,page,size));
        return "dashboard/page/reservation/reservation-list";
    }

    @GetMapping("/statistics")
    public String getStatistics(Model model,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5")int size) {
       model.addAttribute("statistics",reservationService.findReservationStatistics(page, size));
        return "dashboard/page/reservation/statistics";
    }






    @PostMapping("/add")
    public String addReservation(@Valid @ModelAttribute("reservation") ReservationRequest reservationRequest,BindingResult bindingResult ,HttpSession session,Model model)throws TimeIsValidException {
        Reservation reservation = (Reservation) session.getAttribute("reservationLocal");
        if (bindingResult.hasErrors()){
            Long idTable = (Long) session.getAttribute("idTable");
            model.addAttribute("table", tableService.findById(idTable));
            model.addAttribute("reservation", new ReservationRequest());
            model.addAttribute("cart", tableMenuService.getDetails(reservation.getId()));
            return "dashboard/checkoutTable";
        }
        reservationService.save(reservationRequest,reservation);
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    public String edir(Model model, @PathVariable Long id) {
        model.addAttribute("reservation", reservationService.findById(id));
        return "/dashboard/page/reservation/add";
    }

    @PostMapping("/update")
    public String updateReservation(@Valid @ModelAttribute("reservation") ReservationRequest reservationRequest, BindingResult bindingResult, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        reservationRequest.setUser(user);
        return "redirect:/";
    }

    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id) {
        reservationService.confirm(id);
        return "redirect:/";
    }

    @GetMapping("/cancel/{id}")

    public String cancel(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        reservationService.cancel(id, userPrinciple.getId());
        return "redirect:/auth/profile/" + userPrinciple.getId();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable("id") Long id) {
        String filename = "Reservation.xlsx";
        InputStreamResource file = new InputStreamResource(generateExcelService.load(id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);

    }
}
