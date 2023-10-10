package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project_final.service.ITableTypeService;

@Controller
@AllArgsConstructor
@RequestMapping("/tableType")
public class TableTypeController {
    private final ITableTypeService iTableTypeService;
    @GetMapping
    public String getTableType(Model model, @RequestParam(defaultValue= "") String name,@RequestParam(defaultValue ="0") int page,@RequestParam(defaultValue = "5") int size) {
        model.addAttribute("tableTypes", iTableTypeService.findAll());
        return "tableTypes";
    }
}
