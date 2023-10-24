package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.exception.CustomsException;
import project_final.model.dto.request.TableTypeRequest;
import project_final.service.ITableTypeService;

@Controller
@AllArgsConstructor
@RequestMapping("/table-type")
public class TableTypeController {
    private final ITableTypeService tableTypeService;
    @GetMapping
    public String getTableType(Model model,
                               @RequestParam(defaultValue= "") String name,
                               @RequestParam(defaultValue ="0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("name", name);
        model.addAttribute("tableTypes", tableTypeService.findAll(name, page, size));
        return "/dashboard/page/table-type/table-type-list";
    }

    @GetMapping("/add")
    public ModelAndView add(){
        return new ModelAndView("/dashboard/page/table-type/table-type-add","tableType",new TableTypeRequest());
    }

    @PostMapping("/add")
    public String addTableType(@ModelAttribute("tableType") TableTypeRequest tableTypeRequest) throws CustomsException {

        tableTypeService.save(tableTypeRequest);
        return "redirect:/table-type";
    }
    @GetMapping("/edit/{id}")
    public ModelAndView add(@PathVariable Long id){
        return new ModelAndView("/dashboard/page/table-type/table-type-update","tableType",tableTypeService.findById(id));
    }

    @PostMapping("/update")
    public String updateTableType(@ModelAttribute("tableType") TableTypeRequest tableTypeRequest) throws CustomsException {
        tableTypeService.save(tableTypeRequest);
        return "redirect:/table-type";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id){
        tableTypeService.delete(id);
        return "redirect:/table-type";
    }

    @GetMapping("delete-home/{id}")
    public String deleteHome(@PathVariable Long id){
        tableTypeService.delete(id);
        return "redirect:/home";
    }

    @GetMapping("status/{id}")
    public String changeStatus(@PathVariable Long id){
        tableTypeService.changeStatus(id);
        return "redirect:/table-type";
    }

}
