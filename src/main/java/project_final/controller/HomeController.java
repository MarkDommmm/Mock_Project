package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.entity.Menu;
import project_final.model.dto.request.*;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.repository.IMenuRepository;
import project_final.service.*;

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

    @RequestMapping("/home")
    public String getTableType(Model model,
                               @RequestParam(defaultValue = "") String nameTableType,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("tables", tableService.findAll(nameTableType, page, size));
        model.addAttribute("tableTypes", tableTypeService.findAll(name, page, size));
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
                          Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("menuAll", menuService.findAll(name, page, size));
        model.addAttribute("name", name);
        model.addAttribute("menuTrending", menuService.findTopSellingMenus());
        model.addAttribute("tableMenu", new TableMenuRequest());
        model.addAttribute("cart", tableMenuService.getAll(name, page, sizeCart));
        return "dashboard/menu";
    }

    @RequestMapping("/home/chose-menu-category")
    @ResponseBody
    public MenuDataDTO getMenuByIdCategory(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        MenuDataDTO menuDataDTO = new MenuDataDTO();
        menuDataDTO.setCategoryResponse(categoryService.findAll());
        menuDataDTO.setMenu(menuService.getAll(name));
        return menuDataDTO;
    }


    @RequestMapping("/add-cart")
    @ResponseBody
    public Page<TableMenuCartResponse> addTableMenu(@RequestParam(defaultValue = "") Long id,
                                                    @RequestParam(defaultValue = "") String name,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int sizeCart) {
       tableMenuService.addCart(id);
        return tableMenuService.findAll(name, page, sizeCart);
    }

    @RequestMapping("/checkoutTable/{id}")
    public String checkoutTable(@PathVariable Long id,Model model){
        model.addAttribute("table",tableService.findById(id));
        model.addAttribute("reservation",new ReservationRequest());
        return "dashboard/checkoutTable";
    }

    @RequestMapping("/checkout")
    public String checkout(Model model,@RequestParam(defaultValue = "") Long id,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int sizeCart){
        model.addAttribute("cart", tableMenuService.findAll(name, page, sizeCart));
        return "/dashboard/checkout";
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
        return "dashboard/extra/terms-of-service";
    }


}
