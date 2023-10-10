package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.model.dto.request.TableRequest;
import project_final.service.ITableService;
import project_final.service.ITableTypeService;

@Controller
@AllArgsConstructor
@RequestMapping("/table")
public class TableController {
    private final ITableService tableService;

    private final ITableTypeService tableTypeService;
    @GetMapping
    public String findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model){
        model.addAttribute("tables",tableService.findAll(page, size));
        return "/dashboard/page/table/table-list";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("tables",new TableRequest());
        model.addAttribute("tableTypes",tableTypeService.findAll());
        return "/dashboard/page/table/table-add";

    }

    @PostMapping("/add")
    public String addTable(@ModelAttribute TableRequest tableRequest) {
        tableService.save(tableRequest);
        return "redirect:/table";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        model.addAttribute("tables",tableService.findById(id));
        model.addAttribute("tableTypes",tableTypeService.findAll());
        return "/dashboard/page/table/table-update";

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
