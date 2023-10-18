package project_final.advice;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import project_final.exception.ForgotPassWordException;
import project_final.exception.RegisterException;
import project_final.exception.ReviewException;
import project_final.exception.TimeIsValidException;

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

    @ExceptionHandler(TimeIsValidException.class)
    public String TimeFail(TimeIsValidException timeIsValidException, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/error";
        }
        return null;
    }


}
