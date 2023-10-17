package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.entity.Menu;
import project_final.entity.Reservation;
import project_final.entity.User;
import project_final.model.dto.request.*;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.repository.IMenuRepository;
import project_final.security.UserPrinciple;
import project_final.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ITableService tableService;
    private final ITableMenuService tableMenuService;
    private final ITableTypeService tableTypeService;
    private final IMenuService menuService;
    private final ICategoryService categoryService;
    private final IMenuRepository menuRepository;

    @GetMapping("/vnpay-return")
    public String showPaymentResult(Model model, HttpServletRequest request) {

        model.addAttribute("vnp_TxnRef", request.getParameter("vnp_TxnRef"));

        if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
            model.addAttribute("transactionStatus", "Thành công");
        } else {
            model.addAttribute("transactionStatus", "Không thành công");
        }
        return "dashboard/payment/vnpay/vnpay_return";
    }


    @RequestMapping("/home")
    public String getTableType(Model model,
                               @RequestParam(defaultValue = "") String nameTableType,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("tables", tableService.findAllByStatusIsTrueAndName(nameTableType, page, size));
        model.addAttribute("tableTypes", tableTypeService.findAllByStatusIsTrueAndName(name, page, size));
        model.addAttribute("reservation",new ReservationRequest());
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
            HttpSession session) {
        Long idTable = (Long) session.getAttribute("idTable");
        UserPrinciple u = (UserPrinciple) session.getAttribute("currentUser");
        Reservation reservation = tableMenuService.addCart(id, u.getId(), idTable);
        session.setAttribute("reservationLocal",reservation);
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


    @RequestMapping("/403")
    public String error403() {
        return "/dashboard/errors/error403";
    }

    @RequestMapping("/home/sign-in")
    public String signIn(Model model) {
        model.addAttribute("signin", new LoginRequestDto());
        return "dashboard/auth/sign-in";
    }

    @RequestMapping("/home/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signup", new UserRequest());
        return "dashboard/auth/sign-up";
    }

    @RequestMapping("/user")
    public String homeUser() {
        return "dashboard/ChoseTable";
    }
    @RequestMapping("/admin")
    public String admin() {
        return "dashboard/ChoseTable";
    }


}
