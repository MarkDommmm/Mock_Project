package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.model.dto.request.TableRequest;
import project_final.model.service.impl.table.ITableService;

@Controller
@AllArgsConstructor
@RequestMapping("/table")
public class TableController {
    private final ITableService tableService;

    @GetMapping("/")
    public String findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
        model.addAttribute("tables", tableService.findAll(page, size));
        return "/table";
    }

    @GetMapping("/add")
    public ModelAndView add() {
        return new ModelAndView("/create", "table", new TableRequest());
    }

    @PostMapping("/add")
    public String addTable(@ModelAttribute TableRequest tableRequest) {
        tableService.save(tableRequest);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        return new ModelAndView("/edit", "table", tableService.findById(id));
    }

    @PostMapping("/update")
    public String updateTable(@ModelAttribute TableRequest tableRequest) {
        tableService.save(tableRequest);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTable(@PathVariable Long id) {
        tableService.delete(id);
        return "redirect:/";
    }
}
