package project_final.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
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
 
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project_final.config.VNPayConfig;
 
import project_final.entity.Reservation;
import project_final.entity.User;
import project_final.exception.TimeIsValidException;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.security.UserPrinciple;
import project_final.service.IReservationService;
import project_final.service.IReservationMenuService;
import project_final.service.impl.GenerateExcelService;
import project_final.service.impl.PaypalService;
import project_final.service.impl.VNPayService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor

public class ReservationController {
    private final IReservationService reservationService;

    private final IReservationMenuService tableMenuService;
    private final GenerateExcelService generateExcelService;

    private final PaypalService paypalService;
    private final VNPayService vnPayService;
    public static final String SUCCESS_URL = "payment-success";
    public static final String CANCEL_URL = "payment-cancel";

    @GetMapping("/reservation")
    public String getAll(Model model,
                         @RequestParam(name = "date", required = false)
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size) {
        if (date == null) {
            date = new Date();
        }
        model.addAttribute("date", date);
        model.addAttribute("reservations", reservationService.findAll(date, page, size));
        return "dashboard/page/reservation/reservation-list";
    }

    @GetMapping("/reservation/statistics")
    public String getStatistics(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("statistics", reservationService.findReservationStatistics(page, size));
        return "dashboard/page/reservation/statistics";
    }


    @PostMapping("/reservation/add")
    public String addReservation(@Valid @ModelAttribute("reservationRequest") ReservationRequest reservationRequest,
                                 BindingResult bindingResult, HttpSession session,
                                 Model model, HttpServletRequest request) throws TimeIsValidException {
        Reservation reservation = (Reservation) session.getAttribute("reservationLocal");
        List<TableMenuCartResponse> tableMenu = tableMenuService.getDetails(reservation.getId());
        double totalPrice = 0.0;
        for (TableMenuCartResponse item : tableMenu) {
            double price = item.getPrice();
            totalPrice += price;
        }
        if (reservationRequest.getPayment().getId().equals(1L)) {
            reservationService.save(reservationRequest, reservation);
        } else if (reservationRequest.getPayment().getId().equals(2L)) {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnPayUrl = vnPayService.createOrder(totalPrice, reservation.getCode(), baseUrl);
            reservationService.save(reservationRequest, reservation);
            return "redirect:" + vnPayUrl;
        } else if (reservationRequest.getPayment().getId().equals(3L)) {
            try {
                Payment payment = paypalService.createPayment(totalPrice, "USD", "Paypal",
                        "sale", reservationRequest.getDescription(), "http://localhost:8888/" + CANCEL_URL,
                        "http://localhost:8888/" + SUCCESS_URL);
                session.setAttribute("reservationForPayment", reservationRequest);
                for (Links link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        return "redirect:" + link.getHref();
                    }
                }
            } catch (PayPalRESTException e) {

                e.printStackTrace();
            }
        }
        session.removeAttribute("reservationLocal");
        session.removeAttribute("date");
        session.removeAttribute("start");
        session.removeAttribute("end");


        return "redirect:/home";
    }


    @GetMapping(value = CANCEL_URL)
    public String cancelPay(HttpSession session) throws TimeIsValidException {
        Reservation reservation = (Reservation) session.getAttribute("reservationLocal");
        ReservationRequest reservationRequest = (ReservationRequest) session.getAttribute("reservationForPayment");
        reservation.setStatus(Status.PENDING);
        reservationService.save(reservationRequest, reservation);
        return "/dashboard/errors/CancelPayment";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(HttpSession session, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Reservation reservation = (Reservation) session.getAttribute("reservationLocal");
            ReservationRequest reservationRequest = (ReservationRequest) session.getAttribute("reservationForPayment");
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            session.setAttribute("emailPayment", payment.getPayer().getPayerInfo().getEmail());
            if (payment.getState().equals("approved")) {

                reservation.setStatus(Status.PAID);
                reservationService.save(reservationRequest, reservation);
                return "/dashboard/errors/SuccessPayment";
            }

        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        } catch (TimeIsValidException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/home";
    }

    @GetMapping("/reservation/edit/{id}")
    public String edir(Model model, @PathVariable Long id) {
        model.addAttribute("reservation", reservationService.findById(id));
        return "/dashboard/page/reservation/add";
    }

    @PostMapping("/reservation/update")
    public String updateReservation(@Valid @ModelAttribute("reservation") ReservationRequest reservationRequest, BindingResult bindingResult, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        reservationRequest.setUser(user);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/confirm/{id}")
    public String confirm(@PathVariable Long id) {
        reservationService.confirm(id);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/completed/{id}")
    public String completed(@PathVariable Long id) {
        reservationService.completed(id);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/noShow/{id}")
    public String noShow(@PathVariable Long id) {
        reservationService.noShow(id);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/cancel")
    @ResponseBody
    public Map<String, String> cancel(@RequestParam("id") Long id, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        String message = reservationService.cancel(id, userPrinciple.getId());
        Map<String, String> map = new HashMap<>();
        map.put("error", "error");
        map.put("message", message);
        return map;
    }


    @GetMapping("/reservation/download/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable("id") Long id) {
        String filename = "Reservation.xlsx";
        InputStreamResource file = new InputStreamResource(generateExcelService.load(id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);


    }

 
//    @GetMapping("/search")
//    public String searchByCode(@RequestParam String code, HttpSession session) {
//        User user = (User) session.getAttribute("currentUser");
//        reservationService.findByUserAndCode(user, code);
//        return "redirect:/reservation";
//    }
 
}
