package project_final.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project_final.exception.CustomsException;
import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.service.ICategoryService;
import project_final.service.IMenuService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final IMenuService menuService;
    private final ICategoryService categoryService;

    @GetMapping
    public String findAllMenu(@RequestParam(defaultValue = "") String name,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size, Model model) {
        model.addAttribute("menu", menuService.findAll(name, page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        return "dashboard/page/menu/menu-list";
    }

    @GetMapping("/menu/category")
    public String getMenuByCategory(@RequestParam(defaultValue = "") String category,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "6") int size,
                                    Model model) {
        model.addAttribute("category", category);
        model.addAttribute("menus",menuService.findAllByCategoryName(category, page, size));
        return "dashboard/ChoseTable";
    }

    @GetMapping("/menu")
    public String getMenu(@RequestParam(defaultValue = "") String name,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "4") int size,
                          Model model) {
        model.addAttribute("categories",categoryService.findAll());
        model.addAttribute("menu", menuService.findAll(name, page, size));
        model.addAttribute("name", name);
        model.addAttribute("menuTrending",menuService.findTopSellingMenus());
        return "dashboard/menu";
    }


    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("menu", new MenuRequest());
        model.addAttribute("catalog", categoryService.findAll());
        return "dashboard/page/menu/menu-add";
    }

    @PostMapping("/add")
    public String addMenu(@Valid @ModelAttribute MenuRequest menuRequest, BindingResult bindingResult,Model model) throws CustomsException {
        if (bindingResult.hasErrors()){
            model.addAttribute("menu", new MenuRequest());
            model.addAttribute("catalog", categoryService.findAll());
            return "dashboard/page/menu/menu-add";
        }
        menuService.save(menuRequest);
        return "redirect:/menu";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        model.addAttribute("menu", menuService.findById(id));
        model.addAttribute("catalog", categoryService.findAll());
        return "dashboard/page/menu/menu-update";
    }

    @PostMapping("/update")
    public String updateMenu(@ModelAttribute("menu") MenuRequest menuRequest) throws CustomsException {
        menuService.save(menuRequest);
        return "redirect:/menu";
    }

    @GetMapping("delete/{id}")
    private String deleteMenu(@PathVariable Long id) {
        menuService.delete(id);
        return "redirect:/menu";
    }

    @GetMapping("status/{id}")
    public String changeStatus(@PathVariable Long id){
        menuService.changeStatus(id);
        return "redirect:/menu";
    }


}
