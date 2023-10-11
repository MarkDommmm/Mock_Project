package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project_final.entity.TableType;
import project_final.model.dto.request.LoginRequestDto;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.service.ITableService;
import project_final.service.ITableTypeService;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ITableService tableService;

    private final ITableTypeService tableTypeService;

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
    public String getTableByIdTableType(Model model,
                                        @RequestParam(defaultValue = "") String name,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("tableTypes", tableTypeService.findAll(name, page, size));
        model.addAttribute("tables", tableService.getTables(name, page, size));
        return "dashboard/ChoseTable";
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
    public String menu() {
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
