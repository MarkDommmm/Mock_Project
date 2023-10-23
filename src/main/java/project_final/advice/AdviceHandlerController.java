package project_final.advice;


import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import project_final.exception.*;

import java.time.LocalDate;

@ControllerAdvice
public class AdviceHandlerController {

        @ExceptionHandler(RegisterException.class)
    public String registerFail(RegisterException registerException) {
        String error = registerException.getMessage();
        return error;
    }

    @ExceptionHandler(ReviewException.class)
    public String registerFail(ReviewException reviewException) {
        return "/error";
    }

    @ExceptionHandler(ForgotPassWordException.class)
    public String passwordRetrievalFail(ForgotPassWordException forgotPassWordException, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/error";
        }
        return null;
    }

//    @ExceptionHandler(TimeIsValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // Đặt HTTP status code cho response
//    public String handleTimeIsValidException(TimeIsValidException ex, Model model) {
//        // Xử lý exception ở đây và truyền thông tin lỗi đến view thông qua Model
//        model.addAttribute("error", ex.getMessage());
//        return "/home";
//    }
    @ExceptionHandler(CustomsException.class)
    public String customFail(CustomsException customsException, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/error";
        }
        return null;
    }

    @ExceptionHandler(TimeIsValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleTimeIsValidException(TimeIsValidException ex) {
        // Trả về JSON response chứa thông tin lỗi
        return new Error(ex.getMessage());
    }



}
