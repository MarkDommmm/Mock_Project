package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.entity.Reservation;
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
    private final IMenuRepository menuRepository;
    private final IUserService userService;
    private final IMailService mailService;
    private final IReservationRepository reservationRepository;
    private final IUserRepository userRepository;
    private final ITableRepository tableRepository;
    private final ITableTypeRepository tableTypeRepository;
    private final ICategoryRepository categoryRepository;
    private final IReviewRepository reviewRepository;


    @RequestMapping("/public/login")
    public ModelAndView login() {
        return new ModelAndView("/dashboard/auth/sign-in", "login", new LoginRequestDto());
    }

    @RequestMapping("/public/sign-up")
    public ModelAndView signUp() {
        return new ModelAndView("/dashboard/auth/sign-up", "signup", new UserRequest());
    }

    @PostMapping("/public/sign-up")
    public String register(@Valid @ModelAttribute("signup") UserRequest userRequest, BindingResult bindingResult) throws RegisterException, CustomsException {
        if (bindingResult.hasErrors()) {
            return "/dashboard/auth/sign-up";
        }
        userService.save(userRequest);
        return "redirect:/public/login";
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
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(name = "date", required = false)
                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                               @RequestParam(name = "start", required = false, defaultValue = "") String start,
                               @RequestParam(name = "end", required = false, defaultValue = "") String end) {

        session.setAttribute("currentUser", userPrinciple);
//        model.addAttribute("tables", tableService.findAllByStatusIsTrueAndName(name, page, size));

        model.addAttribute("tableTypes", tableTypeService.findAllByStatusIsTrueAndName(nameTableType, page, size));
        model.addAttribute("reservation", new ReservationRequest());
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
    public String choseTable(@AuthenticationPrincipal UserPrinciple userPrinciple,
                             @RequestParam(name = "id") Long id,
                             @RequestParam(name = "date", required = false)
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                             @RequestParam(name = "start", required = false, defaultValue = "") String start,
                             @RequestParam(name = "end", required = false, defaultValue = "") String end,
                             HttpSession session) throws TimeIsValidException {
        if (tableService.isTableAvailable(id, date, start, end)) {
            throw new TimeIsValidException("bàn đã được sử dụng");
        }
        Reservation reservation = reservationService.add(userPrinciple.getId(), date, start, end, id);
        session.setAttribute("reservationLocal", reservation);

        return "redirect:/home/menu";
    }

    @RequestMapping("/home/menu")
    public String getMenu(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "10") int sizeCart,
            Model model, HttpSession session) {

        if (userPrinciple != null) {
            model.addAttribute("cart", reservationMenuService.getAll(userPrinciple.getId(), page, size));
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
                                         Model model, @RequestParam(defaultValue = "") String name,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "12") int size,
                                         @AuthenticationPrincipal UserPrinciple userPrinciple,
                                         HttpSession session) {
        Map<String, String> map = new HashMap<>();
        if (userPrinciple == null || !Objects.equals(userPrinciple.getId(), idU)) {
            map.put("icon", "error");
            map.put("message", "Please log in to the account with this code to update");
        } else {
            session.setAttribute("idReservation", idR);
            Optional<Reservation> reservation = reservationRepository.findById(idR);
            reservation.get().setStatus(Status.PENDING);
            reservationRepository.save(reservation.get());
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
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @RequestParam(defaultValue = "") Long id,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizeCart,
            HttpSession session) throws CustomsException {


        reservationMenuService.addCart(id, userPrinciple.getId());
        return reservationMenuService.getTableMenu(userPrinciple.getId(), page, sizeCart);
    }


    @RequestMapping("/remove-cart-item")
    @ResponseBody
    public Page<TableMenuCartResponse> removeCartItem(
            @RequestParam(defaultValue = "") Long id,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizeCart,
            HttpSession session) {

        reservationMenuService.removeCartItem(id);
        return reservationMenuService.findAll(name, page, sizeCart);
    }


    @GetMapping("/check-out")
    public String checkoutTable(HttpSession session,
                                Model model,
                                @AuthenticationPrincipal UserPrinciple userPrinciple,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "12") int size) {

        Optional<Reservation> existingReservation = reservationRepository.findPendingReservationByUserId(userPrinciple.getId());

        model.addAttribute("table", tableService.findById(existingReservation.get().getTable().getId()));
        model.addAttribute("reservationR", existingReservation.get());
        model.addAttribute("cart", reservationMenuService.getDetails(existingReservation.get().getId()));
        model.addAttribute("payment", paymentRepository.findAll());
        return "dashboard/checkoutTable";
    }

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
//        model.addAttribute("menuTrending", reservationMenuRepository.findAllByMenuTop());
//        System.out.println(reservationMenuRepository.findAllByMenuTop());
        model.addAttribute("menuTopList", menuService.findTopMenusByMonth());

        return "/dashboard/dashboard";
    }
}

 
