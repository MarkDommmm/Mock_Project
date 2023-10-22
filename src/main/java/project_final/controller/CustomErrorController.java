package project_final.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import project_final.model.dto.request.LoginRequestDto;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Lấy thông tin về lỗi từ request
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if (statusCode == 401) {
//                // Xử lý lỗi đăng nhập không thành công 401 Unauthorized
//                model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
//                return "redirect:/public/login";
//            }
//        }
        model.addAttribute("login", new LoginRequestDto());
        model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
        // Xử lý các trường hợp lỗi khác
        return "/dashboard/auth/sign-in";
    }


}
