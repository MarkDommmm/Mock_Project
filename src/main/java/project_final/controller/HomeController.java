package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project_final.entity.TableType;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.TableDataDTO;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.service.ICategoryService;
import project_final.service.IMenuService;
import project_final.service.ITableService;
import project_final.service.ITableTypeService;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ITableService tableService;

    private final ITableTypeService tableTypeService;
    private final IMenuService menuService;
    private final ICategoryService categoryService;

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


    @RequestMapping("/user")
    public String homeUser() {
        return "dashboard/ChoseTable";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "dashboard/extra/terms-of-service";
    }

    @RequestMapping("/home/menu")
    public String getMenu(@RequestParam(defaultValue = "") String name,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "4") int size,
                          Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("menu", menuService.findAll(name, page, size));
        model.addAttribute("name", name);
        model.addAttribute("menuTrending", menuService.findTopSellingMenus());
        return "dashboard/menu";
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


}
