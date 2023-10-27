package project_final.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project_final.entity.Reservation;
import project_final.entity.ReservationMenu;
import project_final.entity.User;
import project_final.exception.TimeIsValidException;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.repository.IReservationMenuRepository;
import project_final.repository.IReservationRepository;
import project_final.security.UserPrinciple;
import project_final.service.IReservationService;
import project_final.service.IReservationMenuService;
import project_final.service.impl.GenerateExcelService;
import project_final.service.impl.PaypalService;
import project_final.service.impl.VNPayService;
import project_final.util.PdfUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@AllArgsConstructor
public class ReservationController {
    private final PdfUtil pdfUtil;
    private final IReservationService reservationService;
    private final IReservationMenuService reservationMenuService;
    private final GenerateExcelService generateExcelService;
    private final IReservationMenuRepository reservationMenuRepository;
    private final IReservationRepository reservationRepository;

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
        model.addAttribute("searchReservations", "");
        model.addAttribute("date", date);
        model.addAttribute("reservations", reservationService.findAll(date, page, size));
        return "dashboard/page/reservation/reservation-list";
    }

    @RequestMapping("/reservation/statistics")
    public String getStatistics(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("statistics", reservationService.findReservationStatistics(page, size));
        return "dashboard/page/reservation/statistics";
    }

    @GetMapping("/reservation/reservationMenu/{id}")
    public String getReservationMenu(@PathVariable Long id, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("reservationMenu", reservationMenuService.getReservationMenu(id, page, size));

        model.addAttribute("total", reservationService.getTotalPrice(id));
        model.addAttribute("totalPaid", reservationService.getTotalPaid(id));

        return "dashboard/page/reservation/reservation-menu";
    }


    @PostMapping("/reservation/add")
    public ResponseEntity<?> addReservation(@Valid @ModelAttribute("reservationR") ReservationRequest reservationRequest,
                                            BindingResult bindingResult, HttpSession session,
                                            @AuthenticationPrincipal UserPrinciple userPrinciple,
                                            Model model, HttpServletRequest request) {
        try {
            Optional<Reservation> existingReservation = reservationRepository.findById(reservationRequest.getId());
            double totalPrice = 0;
            if (existingReservation.get().getStatus().equals(Status.PENDING)) {
                List<TableMenuCartResponse> tableMenu = reservationMenuService.getDetails(existingReservation.get().getId());
                totalPrice = tableMenu.stream().mapToDouble(TableMenuCartResponse::getPrice).sum();
            } else {
                totalPrice = reservationService.getTotalPrice(existingReservation.get().getId())
                        - reservationService.getTotalPaid(existingReservation.get().getId());
            }

            if (reservationRequest.getPayment().getId().equals(1L)) {
                reservationRequest.setStatus(Status.ORDER);
                reservationService.save(reservationRequest);
                session.removeAttribute("idReservation");
                session.removeAttribute("carts");
            } else if (reservationRequest.getPayment().getId().equals(2L)) {
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String vnPayUrl = vnPayService.createOrder(totalPrice, existingReservation.get().getCode(), baseUrl);
                reservationService.save(reservationRequest);
                session.removeAttribute("idReservation");
                session.removeAttribute("carts");
                return ResponseEntity.ok().body(Collections.singletonMap("redirectUrl", vnPayUrl));
            } else if (reservationRequest.getPayment().getId().equals(3L)) {
                session.setAttribute("reservationRequest", reservationRequest);
                session.removeAttribute("idReservation");
                session.removeAttribute("carts");
                return ResponseEntity.ok().body(Collections.singletonMap("redirectUrl", "/reservation/paypal"));
            }


            return ResponseEntity.ok().body(Collections.singletonMap("redirectUrl", "/home"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error placing reservation.");
        }
    }

    @GetMapping("/reservation/paypal")
    public ResponseEntity<?> initiatePaypalPayment(HttpSession session, HttpServletResponse response) {
        try {
            ReservationRequest reservationRequest = (ReservationRequest) session.getAttribute("reservationRequest");
            double totalPrice = reservationService.getTotalPrice(reservationRequest.getId());
            Payment payment = paypalService.createPayment(totalPrice, "USD", "Paypal",
                    "sale", reservationRequest.getDescription(), "http://localhost:8888/" + CANCEL_URL,
                    "http://localhost:8888/" + SUCCESS_URL);
            session.setAttribute("reservationForPayment", reservationRequest);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    response.sendRedirect(link.getHref());
                    return ResponseEntity.ok().build();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error initiating PayPal payment");
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay(HttpSession session, @AuthenticationPrincipal UserPrinciple userPrinciple) throws TimeIsValidException {
        ReservationRequest reservationRequest = (ReservationRequest) session.getAttribute("reservationForPayment");
        reservationRequest.setStatus(Status.ORDER);
        reservationService.save(reservationRequest);
        return "/dashboard/errors/CancelPayment";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(Model model, HttpSession session, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            ReservationRequest reservationRequest = (ReservationRequest) session.getAttribute("reservationForPayment");
            List<ReservationMenu> reservationMenu = reservationMenuRepository.findAllById(reservationRequest.getId());
            Payment payment = paypalService.executePayment(paymentId, payerId);

            System.out.println(payment.toJSON());
            model.addAttribute("emailPayment", payment.getPayer().getPayerInfo().getEmail());

            if (payment.getState().equals("approved")) {

                for (ReservationMenu rm : reservationMenu) {
                    rm.setPay(Status.PAID);
                    reservationMenuRepository.save(rm);
                }
                if (reservationRequest.getStatus().equals(Status.CONFIRM)) {
                    reservationRequest.setStatus(Status.COMPLETED);
                    reservationService.save(reservationRequest);
                } else {
                    reservationRequest.setStatus(Status.ORDER);
                    reservationService.save(reservationRequest);
                }
                    model.addAttribute("receipt", reservationRequest.getId());
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

    @GetMapping("/reservation/served/{id}")
    public String served(@PathVariable Long id, @RequestParam("idRese") Long idRese, @RequestParam("quantity") int quantity) {
        reservationMenuService.served(quantity, id);
        return "redirect:/reservation/reservationMenu/" + idRese;
    }

    @GetMapping("/reservation/menu/cancel")

    public Map<String, String> cancel(@RequestParam("id") Long id) {
        String message = reservationMenuService.adminCancel(id);
        Map<String, String> map = new HashMap<>();
        map.put("error", "error");
        map.put("message", message);
        return map;
    }


    @GetMapping("/reservation/confirm")
    public String confirm(@RequestParam Long id) {
        reservationService.confirm(id);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/completed")
    public String completed(@RequestParam Long id) {
        reservationService.completed(id);
        List<ReservationMenu> reservationMenus = reservationMenuRepository.findAllById(id);
        for (ReservationMenu r : reservationMenus) {
            r.setPay(Status.PAID);
            reservationMenuRepository.save(r);
        }
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/noShow/{id}")
    public String noShow(@PathVariable Long id) {
        reservationService.noShow(id);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/change-status/{id}")
    public String changeStatus(@PathVariable("id") Long id, HttpSession session, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        reservationService.changeStatusOrder(id);
        session.removeAttribute("idReservation");
        String date = (String) session.getAttribute("date");
        if (userPrinciple.getUsername().equals("admin")) {
            if (date.equals("0")) {
                return "redirect:/home";
            } else {
                return "redirect:/reservation?date=" + date;
            }

        }
        return "redirect:/home";
    }


    @GetMapping("/reservation/cancel")
    @ResponseBody
    public Map<String, String> cancel(@RequestParam("id") Long id, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        String message = reservationService.cancel(id, userPrinciple.getId());
        Map<String, String> map = new HashMap<>();
        map.put("error", "warning");
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

    @GetMapping("/printInvoice/{id}")
    public ResponseEntity<byte[]> printInvoice(@PathVariable Long id) {
        byte[] pdfContent = pdfUtil.createPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "Restaurant Aprycot-Receipt.pdf");
//            headers.set("Content-Disposition", "attachment; filename=Restaurant_Aprycot-Receipt.pdf");
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }


    @GetMapping("/search")
    public String searchByCode(@RequestParam String code, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        reservationService.findByCode(code);
        return "redirect:/reservation";
    }


}
