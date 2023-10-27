package project_final.controller;

import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.entity.Reservation;
import project_final.entity.User;
import project_final.exception.CustomsException;
import project_final.exception.ForgotPassWordException;
import project_final.exception.RegisterException;
import project_final.exception.TimeIsValidException;
import project_final.model.domain.Status;
import project_final.model.dto.request.*;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.repository.*;
import project_final.security.UserPrinciple;
import project_final.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ITableService tableService;
    private final IReservationService reservationService;
    private final IReservationMenuRepository reservationMenuRepository;
    private final IReservationMenuService reservationMenuService;
    private final ITableTypeService tableTypeService;
    private final IMenuService menuService;
    private final ICategoryService categoryService;
    private final IPaymentRepository paymentRepository;
    private final IUserService userService;
    private final IMailService mailService;
    private final IReservationRepository reservationRepository;
    private final IReviewRepository reviewRepository;
    private final IReviewService reviewService;


    @RequestMapping("/public/login")
    public ModelAndView login() {
        return new ModelAndView("/dashboard/auth/sign-in", "login", new LoginRequestDto());
    }

    @RequestMapping("/public/sign-up")
    public ModelAndView signUp() {
        return new ModelAndView("/dashboard/auth/sign-up", "signup", new UserRequest());
    }

    @RequestMapping("/sign-up/success")
    public String signUpSuccess() {
        return "/dashboard/errors/sign-up-success";
    }

    @PostMapping("/public/sign-up")
    public String register(@Valid @ModelAttribute("signup") UserRequest userRequest, BindingResult bindingResult) throws RegisterException, CustomsException {
        if (bindingResult.hasErrors()) {
            return "/dashboard/auth/sign-up";
        }
        userService.save(userRequest);
        User user = userService.findByUserName(userRequest.getUsername()).get();
        String email = user.getEmail();
        String content = "Hello " + userRequest.getUsername() + ",\n\n" +
                "Thank you for registering an account !!\n" +

                "You need to activate your account to use our services.\n" +

                "\n\nVisit the link below to activate your account\n\n" +
                "http://localhost:8888/public/active-account?email=" + email;
        mailService.sendMail(userRequest.getEmail(), "Active Account", content);
        return "redirect:/sign-up/success";
    }


    @GetMapping("/public/active-account")
    public String activateAccount(@RequestParam String email) {
        userService.active(email);
        String content = "Hello " + email + ",\n\n" +
                "You have successfully activated your account !!\n" +
                "Now you can log in to use the service \n";
        mailService.sendMail(email, "Account activation successful", content);
        return "/dashboard/auth/active-account";

    }


    @RequestMapping("/public/forgot-password")
    public ModelAndView recoverPw() {
        return new ModelAndView("/dashboard/auth/recoverpw", "forgotPw", new ForgotPassForm());
    }

    @PostMapping("/public/forgot-password")
    public String sendVerification(@ModelAttribute("forgotPw") ForgotPassForm forgotPassForm, Model model) throws ForgotPassWordException {
        if (forgotPassForm.getVerification() == null) {
            String verification = userService.sendVerification(forgotPassForm.getEmail());
            if (verification == null) {
                String emailContent = "Invalid email, please try again";
                mailService.sendMail(forgotPassForm.getEmail(), "Aprycot Alert", emailContent);
                return "/dashboard/auth/recoverpw-confirm";
            }
            String content = "Hello " + forgotPassForm.getEmail() + ",\n\n" +
                    "For security reasons, you are required to use the following One Time Password to log in:\n" +
                    "\n" + verification +
                    "\n\nNote: This OTP is set to expire in 5 minutes.\n\n" +
                    "If you did not request this password reset, please contact us immediately at support@example.com.";


            mailService.sendMail(forgotPassForm.getEmail(), "Verification", content);
            return "/dashboard/auth/recoverpw-confirm";
        }
        userService.passwordRetrieval(forgotPassForm);
        return "redirect:/public/confirm-mail";
    }

    @RequestMapping("/public/confirm-mail")
    public String confirmMail() {

        return "/dashboard/auth/confirm-mail";
    }

    @RequestMapping("/home")
    public String getTableType(Model model,
                               HttpSession session,
                               @AuthenticationPrincipal UserPrinciple userPrinciple,
                               @RequestParam(defaultValue = "") String nameTableType,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {

        session.setAttribute("currentUser", userPrinciple);
        model.addAttribute("notifications", reservationRepository.findAllByStatusORDER());
        model.addAttribute("tableTypes", tableTypeService.findAllByStatusIsTrueAndName(nameTableType, page, size));
        model.addAttribute("reservation", new ReservationRequest());
        session.removeAttribute("idReservation");
        session.removeAttribute("reservationForPayment");
        return "dashboard/ChoseTable";
    }

    @RequestMapping("/home/chose-table")
    @ResponseBody
    public TableDataDTO getTableByIdTableType(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(name = "start", required = false, defaultValue = "") String start,
            @RequestParam(name = "end", required = false, defaultValue = "") String end) {
        if (date == null) {
            date = new Date();
        }

        TableDataDTO tableDataDTO = new TableDataDTO();
        tableDataDTO.setTableTypes(tableTypeService.findAll(name, page, size));
        tableDataDTO.setTables(tableService.findAvailableTables(name, date, start, end, page, size).getContent());
        return tableDataDTO;
    }

    @GetMapping("/home/chose-idTable")
    @ResponseBody
    public Map<String, String> choseTable(@AuthenticationPrincipal UserPrinciple userPrinciple,
                                          @RequestParam(name = "id") Long id,
                                          @RequestParam(name = "date", required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                          @RequestParam(name = "start", required = false, defaultValue = "") String start,
                                          @RequestParam(name = "end", required = false, defaultValue = "") String end,
                                          HttpSession session) throws TimeIsValidException {
        Map<String, String> map = new HashMap<>();
        Optional<Reservation> existingReservation = reservationRepository.findOrderReservationByUserId(userPrinciple.getId());
        Optional<Reservation> exitsReservation = reservationRepository.findPendingReservationByUserId(userPrinciple.getId());

        if (tableService.isTableAvailable(id, date, start, end)) {
            map.put("icon", "error");
            map.put("message", "The dining table has been booked");
            return map;
        }
        if (existingReservation.isPresent() || exitsReservation.isPresent()) {
            map.put("icon", "error");
            map.put("message", "You have an order in progress, please wait for Admin to confirm! Contact Hotline 7777");
        } else {
            Reservation reservation = reservationService.add(userPrinciple.getId(), date, start, end, id);

//            session.setAttribute("reservationLocal", reservation);
            session.setAttribute("idReservation", reservation.getId());

            map.put("icon", "success");
        }

        return map;
    }

    @RequestMapping("/home/menu")
    public String getMenu(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "10") int sizeCart,
            Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("idReservation");

        if (userPrinciple != null) {
            model.addAttribute("cart", reservationMenuService.getAll(id, page, size));
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("menuAll", menuService.findAllByStatusIsTrueAndName(name, page, size));
        model.addAttribute("name", name);
        model.addAttribute("menuTrending", menuService.findTopSellingMenus());
        model.addAttribute("tableMenu", new ReservationMenuRequest());
        return "dashboard/menu";
    }

    @RequestMapping("/edit-order")
    @ResponseBody
    public Map<String, String> editOrder(@RequestParam("idResvertion") Long idR,
                                         @RequestParam("idUser") Long idU,
                                         @RequestParam(value = "date", defaultValue = "0") String date,
                                         Model model, @RequestParam(defaultValue = "") String name,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "12") int size,
                                         @AuthenticationPrincipal UserPrinciple userPrinciple,
                                         HttpSession session) {
        Map<String, String> map = new HashMap<>();
        if (userPrinciple != null && (Objects.equals(userPrinciple.getId(), idU) || userPrinciple.getUsername().equals("admin"))) {
            model.addAttribute("cart", reservationMenuService.getDetails(idR));
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("menuAll", menuService.findAllByStatusIsTrueAndName(name, page, size));
//            model.addAttribute("idR", idR);
            model.addAttribute("reservationMenu", new ReservationMenuRequest());
//            Tao ss luu tru idReservation sang trang home menu  và set if cho  button update
            session.setAttribute("idReservation", idR);
            session.setAttribute("date", date);

            Optional<Reservation> reservation = reservationRepository.findById(idR);
            if (reservation.isPresent()) {
                if (reservation.get().getStatus().equals(Status.ORDER) || reservation.get().getStatus().equals(Status.PENDING))
                    reservation.get().setStatus(Status.PENDING);
                reservationRepository.save(reservation.get());
            }


            reservation.get().setStatus(Status.PENDING);
            reservationRepository.save(reservation.get());


        } else {
            map.put("icon", "error");
            map.put("message", "Access denied. Please log in to the correct account or contact an administrator.");
        }
        return map;
    }

    @RequestMapping("/home/chose-menu-category")
    @ResponseBody
    public MenuDataDTO getMenuByIdCategory(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {

        MenuDataDTO menuDataDTO = new MenuDataDTO();
        menuDataDTO.setCategoryResponse(categoryService.findAll());
        menuDataDTO.setMenu(menuService.findAllByStatusIsTrueAndName(name, page, size));
        return menuDataDTO;
    }


    @RequestMapping("/add-cart")
    @ResponseBody
    public Page<TableMenuCartResponse> addTableMenu(
            @RequestParam(defaultValue = "") Long id,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizeCart,
            HttpSession session, @AuthenticationPrincipal UserPrinciple userPrinciple) {

        Long idR = (Long) session.getAttribute("idReservation");
        Optional<Reservation> existingReservation = null;
        if (idR != null) {
            Optional<Reservation> reservation = reservationRepository.findById(idR);
            existingReservation = reservationRepository.findPendingReservationByUserId(reservation.get().getUser().getId());

        } else {
            existingReservation = reservationRepository.findPendingReservationByUserId(userPrinciple.getId());
        }

        if (existingReservation.isPresent()) {
            reservationMenuService.addCart(id, existingReservation.get().getId());
            return reservationMenuService.getTableMenu(existingReservation.get().getId(), page, sizeCart);
        }
        return reservationMenuService.getTableMenu(null, page, sizeCart);

    }


    @RequestMapping("/remove-cart-item")
    @ResponseBody
    public List<TableMenuCartResponse> removeCartItem(
            @RequestParam(defaultValue = "") Long id,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizeCart,
            Model model, @AuthenticationPrincipal UserPrinciple userPrinciple) {

        reservationMenuService.removeCartItem(id);
        Optional<Reservation> existingReservation = reservationRepository.findPendingReservationByUserId(userPrinciple.getId());
        return reservationMenuService.getDetails(existingReservation.get().getId());
    }


    @GetMapping("/check-out")
    @ResponseBody
    public Map<String, String> checkoutTable(HttpSession session,
                                             @AuthenticationPrincipal UserPrinciple userPrinciple) {
        Map<String, String> map = new HashMap<>();
        Optional<Reservation> existingReservationOrder = reservationRepository.findOrderReservationByUserId(userPrinciple.getId());
        if (existingReservationOrder.isPresent()) {
            map.put("icon", "error");
            map.put("message", "You have an order in progress, please wait for Admin to confirm! Contact Hotline 7777");

        } else {
            session.removeAttribute("idReservation");
            session.removeAttribute("carts");
        }
        return map;
    }

    @RequestMapping("/checkout")
    public String checkout(Model model, @AuthenticationPrincipal UserPrinciple userPrinciple, HttpSession session) {

        Optional<Reservation> existingReservation = reservationRepository.findPendingReservationByUserId(userPrinciple.getId());
        List<TableMenuCartResponse> tableMenuCartResponse = reservationMenuService.getDetails(existingReservation.get().getId());

        double totalPrice = tableMenuCartResponse.stream()
                .mapToDouble(TableMenuCartResponse::getPrice)
                .sum();
        model.addAttribute("table", tableService.findById(existingReservation.get().getTable().getId()));
        model.addAttribute("reservationR", existingReservation.get());
        model.addAttribute("cart", tableMenuCartResponse);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("payment", paymentRepository.findAll());
        return "dashboard/checkoutTable";
    }

    @RequestMapping("/checkout-final")
    public String checkoutFinal(Model model,
                                @RequestParam("idR") Long idR,
                                @RequestParam("idU") Long idU,
                                @AuthenticationPrincipal UserPrinciple userPrinciple) {

        Optional<Reservation> existingReservation = reservationRepository.findById(idR);

        List<TableMenuCartResponse> tableMenuCartResponse = reservationMenuService.getDetails(existingReservation.get().getId());
        if (reservationMenuRepository.checkUnpaidExists(idR)) {
            model.addAttribute("table", tableService.findById(existingReservation.get().getTable().getId()));
            model.addAttribute("reservationR", existingReservation.get());
            model.addAttribute("cart", tableMenuCartResponse);
            model.addAttribute("totalPrice", reservationService.getTotalPrice(idR));
            model.addAttribute("payment", paymentRepository.findAll());
            model.addAttribute("totalPaid", reservationService.getTotalPaid(idR));
        } else {
            return "redirect:/home?error=check-out";
        }


        return "dashboard/checkoutFinal";
    }


    @RequestMapping("/home/reviews/{id}")
    public String getHomeReview(Model model, @PathVariable Long id, @AuthenticationPrincipal UserPrinciple userPrinciple) {

        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            model.addAttribute("reviews", reviewRepository.findAll());
            model.addAttribute("review", "");
            ReviewRequest reviewRequest = new ReviewRequest();
            reviewRequest.setReservation(reservation.get());
            model.addAttribute("reviewRequest", reviewRequest);
            return "dashboard/reviews";
        }
        return "redirect:/auth/profile" + userPrinciple.getId();
    }

    @RequestMapping("/home/reviews")
    public String getHomeReviews(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "dashboard/reviews";
    }

    @PostMapping("/home/create/review")
    @ResponseBody
    public Map<String, String> addReview(@ModelAttribute ReviewRequest reviewRequest, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        String review = reviewService.save(reviewRequest, userPrinciple.getId());
        Map<String, String> map = new HashMap<>();
        map.put("icon", "warning");
        map.put("message", review);
        return map;
    }


}

 
