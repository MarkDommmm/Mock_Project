package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.entity.Review;
import project_final.entity.User;
import project_final.model.dto.request.ReviewRequest;
import project_final.service.IReviewService;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final IReviewService reviewService;
    @GetMapping
    public String getReview(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("reviews",reviewService.findAll(page, size));
        return "/dashboard/page/reviews/reviews-list";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("review",new Review());
        return "/dashboard/page/add";
    }


    @GetMapping("/hidden/{id}")
    public String hidden(@PathVariable Long id){
        reviewService.changeStatus(id);
        return "redirect:/reviews";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        reviewService.delete(id, user);
        return "redirect:/reviews";
    }

}
