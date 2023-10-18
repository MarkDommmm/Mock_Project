package project_final.controller;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
<<<<<<< HEAD
import org.springframework.security.core.annotation.AuthenticationPrincipal;
=======
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
>>>>>>> origin/master
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.entity.Reservation;
import project_final.exception.CustomsException;
import project_final.exception.ForgotPassWordException;
import project_final.exception.RegisterException;
import project_final.model.dto.request.*;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.repository.IMenuRepository;
import project_final.security.UserPrinciple;
import project_final.service.*;
import javax.servlet.http.HttpSession;
<<<<<<< HEAD
import javax.validation.Valid;
=======
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
>>>>>>> origin/master

@Controller
@AllArgsConstructor
public class HomeController {
    private final ITableService tableService;
    private final ITableMenuService tableMenuService;
    private final ITableTypeService tableTypeService;
    private final IMenuService menuService;
    private final ICategoryService categoryService;
<<<<<<< HEAD
    private final IMenuRepository menuRepository;
    private final IUserService userService;
    private final IMailService mailService;
=======
    private final IUserService userService;
>>>>>>> origin/master

    @RequestMapping("/public/login")
    public ModelAndView login() {
        return new ModelAndView("/dashboard/auth/sign-in", "login", new LoginRequestDto());
    }

    @RequestMapping("/public/sign-up")
    public ModelAndView signUp() {
        return new ModelAndView("/dashboard/auth/sign-up", "signup", new UserRequest());
    }

    @PostMapping("/public/sign-up")
    public String register(@ModelAttribute("signup") @Valid UserRequest userRequest, BindingResult bindingResult) throws RegisterException, CustomsException {
        if (bindingResult.hasErrors()) {
            return "redirect:/public/sign-up";
        }
        userService.save(userRequest);
        return "redirect:/public/login";
    }

    @RequestMapping("/public/forgot-password")
    public ModelAndView recoverPw() {
        return new ModelAndView("/dashboard/auth/recoverpw", "forgotPw", new ForgotPassForm());
    }

    @PostMapping("/public/forgot-password")
    public String sendVerification(@ModelAttribute("forgotPw") ForgotPassForm forgotPassForm,Model model) throws ForgotPassWordException {
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
                               @RequestParam(defaultValue = "") String nameTableType,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "0") int page,
<<<<<<< HEAD
                               @RequestParam(defaultValue = "5") int size, @AuthenticationPrincipal UserPrinciple userPrinciple, HttpSession session) {
        model.addAttribute("tables", tableService.findAll(nameTableType, page, size));
        model.addAttribute("tableTypes", tableTypeService.findAll(name, page, size));
        session.setAttribute("currentUser", userPrinciple);
=======
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(name = "date", required = false)
                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                               @RequestParam(name = "start", required = false, defaultValue = "") String start,
                               @RequestParam(name = "end", required = false, defaultValue = "") String end) {
        model.addAttribute("tables", tableService.findAvailableTables(date, start, end, page, size));
        model.addAttribute("tableTypes", tableTypeService.findAllByStatusIsTrueAndName(nameTableType, page, size));
        model.addAttribute("reservation", new ReservationRequest());
>>>>>>> origin/master
        return "dashboard/ChoseTable";
    }

    @RequestMapping("/home/chose-table")
    @ResponseBody
    public TableDataDTO getTableByIdTableType(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        TableDataDTO tableDataDTO = new TableDataDTO();
        tableDataDTO.setTableTypes(tableTypeService.findAll(name, page, size));
        tableDataDTO.setTables(tableService.getTables(name));
        return tableDataDTO;
    }


    @RequestMapping("/home/menu")
    public String getMenu(@RequestParam(defaultValue = "") String name,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "12") int size,
                          @RequestParam(defaultValue = "10") int sizeCart,
                          Model model, HttpSession session) {


        UserPrinciple u = (UserPrinciple) session.getAttribute("currentUser");
        if (u != null) {
            model.addAttribute("cart", tableMenuService.getAll(u.getId(), page, sizeCart));
        }

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("menuAll", menuService.findAllByStatusIsTrueAndName(name, page, size));
        model.addAttribute("name", name);
        model.addAttribute("menuTrending", menuService.findTopSellingMenus());
        model.addAttribute("tableMenu", new TableMenuRequest());

        return "dashboard/menu";
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
            HttpSession session) throws CustomsException {
        Long idTable = (Long) session.getAttribute("idTable");
        UserPrinciple u = (UserPrinciple) session.getAttribute("currentUser");
        Reservation reservation = tableMenuService.addCart(id, u.getId(), idTable);
        session.setAttribute("reservationLocal", reservation);
        return tableMenuService.getTableMenu(u.getId(), page, sizeCart);
    }


    @RequestMapping("/remove-cart-item")
    @ResponseBody
    public Page<TableMenuCartResponse> removeCartItem(
            @RequestParam(defaultValue = "") Long id,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizeCart,
            HttpSession session) {

        tableMenuService.removeCartItem(id);
        return tableMenuService.findAll(name, page, sizeCart);
    }

    @GetMapping("/home/chose-table/{id}")
    public String choseTable(@PathVariable("id") Long id, HttpSession session) {
        session.setAttribute("idTable", id);
        return "redirect:/home/menu";
    }

    @GetMapping("/check-out")
    public String checkoutTable(HttpSession session,
                                Model model,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "12") int size) {
        Reservation reservation = (Reservation) session.getAttribute("reservationLocal");
        Long id = reservation.getId();
        Long idTable = (Long) session.getAttribute("idTable");
        model.addAttribute("table", tableService.findById(idTable));
        model.addAttribute("reservation", new ReservationRequest());
        model.addAttribute("cart", tableMenuService.getDetails(id));
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


<<<<<<< HEAD
=======
    @RequestMapping("/home/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signup", new UserRequest());
        return "dashboard/auth/sign-up";
    }

    @RequestMapping("/user")
    public String homeUser(Model model,@RequestParam(defaultValue = "") String name,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        model.addAttribute("users", userService.findAll(name,page,size) );
        model.addAttribute("name",name);
        return "dashboard/page/user/user-list";
    }



    @RequestMapping("/admin")
    public String admin() {
        return "dashboard/ChoseTable";
    }
>>>>>>> origin/master


}
