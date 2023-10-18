package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.entity.Reservation;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.exception.CustomsException;
import project_final.model.dto.request.ReservationRequest;
import project_final.repository.IUserRepository;
import project_final.security.UserPrinciple;
import project_final.service.IReservationService;
import project_final.service.ITableService;
import project_final.service.IUserService;
import project_final.service.impl.GenerateExcelService;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final IReservationService reservationService;
    private final IUserService userService;
    private final ITableService tableService;
    private final GenerateExcelService generateExcelService;

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
    public String addReservation(@ModelAttribute("reservation") ReservationRequest reservationRequest, HttpSession session) throws CustomsException {
        Reservation reservation = (Reservation) session.getAttribute("reservationLocal");
        reservationService.save(reservationRequest, reservation);
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
    public String confirm(@PathVariable("id") Long id) {
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
        String filename = "Merge_cell_handle.xlsx";
        InputStreamResource file = new InputStreamResource(generateExcelService.load(id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

}
