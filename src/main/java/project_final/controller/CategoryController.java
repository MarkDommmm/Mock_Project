package project_final.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project_final.entity.Category;
import project_final.service.ICategoryService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;
    @GetMapping
    public String findAllCategory(Model model, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("category", categoryService.findAll(name, page, size));
        model.addAttribute("name", name);
        return "category/list";
    }

    @GetMapping("/add")
    public ModelAndView add(){
        return new ModelAndView("category/add","category",new Category());
    }

    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "category/add";
        }
        categoryService.save(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id){
        return new ModelAndView("category/edit","category",categoryService.findById(id));
    }

    @PostMapping("/update")
    public String editCategory(@Valid @ModelAttribute Category category,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "category/edit";
        }
        categoryService.save(category);
        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        return "redirect:/category";
    }


}
