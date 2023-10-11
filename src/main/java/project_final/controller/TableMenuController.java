package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.model.dto.request.TableMenuRequest;
import project_final.service.ITableMenuService;

@Controller
@AllArgsConstructor
@RequestMapping("/tableMenu")
public class TableMenuController {
    private final ITableMenuService tableMenuService;
    @GetMapping
    public String getTableMenu(Model model,@RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("tableMenus", tableMenuService.findAll(name,page,size));
        model.addAttribute("name", name);
        return "/dashboard/page/tableMenu";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("tableMenu",new TableMenuRequest());
        return "/dashboard/page/tableMenu/add";
    }

    @PostMapping("/add")
    public String addTableMenu(@ModelAttribute("tableMenu") TableMenuRequest tableMenuRequest ){
        tableMenuService.save(tableMenuRequest);
        return "redirect:/tableMenu";
    }
    @GetMapping("/edit/{id}")
    public String edit(Model model,@PathVariable Long id){
        model.addAttribute("tableMenu",tableMenuService.findById(id));
        return "/dashboard/page/tableMenu/add";
    }

    @PostMapping("/update")
    public String updateTableMenu(@ModelAttribute("tableMenu") TableMenuRequest tableMenuRequest ){
        tableMenuService.save(tableMenuRequest);
        return "redirect:/tableMenu";
    }

    @GetMapping("/delete/{id}")
    public String deleteTableMenu(@PathVariable Long id) {
        tableMenuService.delete(id);
        return "redirect:/tableMenu";
    }
}
