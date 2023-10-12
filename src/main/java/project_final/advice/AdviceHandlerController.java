package project_final.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project_final.exception.RegisterException;
import project_final.exception.ReviewException;

public class AdviceHandlerController {
    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<String> registerFail(RegisterException registerException) {
        return new ResponseEntity<>(registerException.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ReviewException.class)
    public String registerFail(ReviewException reviewException) {
        return "/error";
    }

}
