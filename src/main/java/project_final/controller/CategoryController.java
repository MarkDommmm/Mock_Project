package project_final.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project_final.entity.Category;
import project_final.exception.CustomsException;
import project_final.model.dto.request.CategoryRequest;
import project_final.service.ICategoryService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public String findAllCategory(Model model, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("category", categoryService.findAll(name, page, size));
        model.addAttribute("name", name);
        return "dashboard/page/category/category-list";
    }

    @GetMapping("/add")
    public ModelAndView add() {
        return new ModelAndView("dashboard/page/category/category-add", "category", new CategoryRequest());
    }

 
    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute("category") CategoryRequest categoryRequest,BindingResult bindingResult ) {
 
        if (bindingResult.hasErrors()){
            return "dashboard/page/category/category-add";
        }
        categoryService.save(categoryRequest);
        return "redirect:/category";

    }


    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {

        return new ModelAndView("dashboard/page/category/category-update", "category", categoryService.findById(id));
    }

    @PostMapping("/update")
    public String editCategory(@Valid @ModelAttribute CategoryRequest categoryRequest, BindingResult bindingResult) throws CustomsException {
        if (bindingResult.hasErrors()) {
            return "dashboard/page/category/category-update";
        }
        categoryService.save(categoryRequest);
        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/category";
    }

    @GetMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id) {
        categoryService.changeStatus(id);
        return "redirect:/category";
    }


}
