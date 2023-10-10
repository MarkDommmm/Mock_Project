package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.model.dto.request.TableTypeRequest;
import project_final.service.ITableTypeService;

@Controller
@AllArgsConstructor
@RequestMapping("/tableType")
public class TableTypeController {
    private final ITableTypeService tableTypeService;
    @GetMapping
    public String getTableType(Model model, @RequestParam(defaultValue= "") String name,@RequestParam(defaultValue ="0") int page,@RequestParam(defaultValue = "5") int size) {
        model.addAttribute("tableTypes", tableTypeService.findAll());
        return "tableTypes";
    }

    @GetMapping("/add")
    public ModelAndView add(){
        return new ModelAndView("/tableType/create","tableType",new TableTypeRequest());
    }

    @PostMapping("/add")
    public String addTableType(@ModelAttribute("tableType") TableTypeRequest tableTypeRequest){
        tableTypeService.save(tableTypeRequest);
        return "redirect:/tableType";
    }
    @GetMapping("/edit/{id")
    public ModelAndView add(@PathVariable Long id){
        return new ModelAndView("/tableType/edit","tableType",tableTypeService.findById(id));
    }

    @PostMapping("/edit")
    public String updateTableType(@ModelAttribute("tableType") TableTypeRequest tableTypeRequest){
        tableTypeService.save(tableTypeRequest);
        return "redirect:/tableType";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id){
        tableTypeService.delete(id);
        return "redirect:/tableType";
    }

}
