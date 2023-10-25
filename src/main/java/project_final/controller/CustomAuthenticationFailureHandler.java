package project_final.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // Xử lý đăng nhập thất bại ở đây
        String errorMessage = exception.getMessage();

        // Bạn có thể thực hiện các xử lý cụ thể dựa trên thông điệp lỗi hoặc URL
        if (errorMessage != null && errorMessage.contains("Bad credentials")) {
            // Đây có thể là lỗi sai mật khẩu, thực hiện xử lý tương ứng
            response.sendRedirect("/public/login?error=bad_credentials");
        } else{
            // Xử lý các trường hợp khác
            response.sendRedirect("/public/login?error=other_error");
        }
    }
}
