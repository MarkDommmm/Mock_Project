package project_final.service;

import org.springframework.data.domain.Page;
import project_final.exception.ForgotPassWordException;
import project_final.exception.RegisterException;
import project_final.model.dto.request.ActiveForm;
import project_final.model.dto.request.ForgotPassForm;
import project_final.model.dto.request.UpdateUserRequest;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.entity.User;

import java.util.Optional;

public interface IUserService {
    boolean checkPassword(String password, String confirm_password);

    Optional<User> findByUserName(String username);

    User save(UserRequest userRequest) throws RegisterException;
    User update(UpdateUserRequest userRequest) throws RegisterException;
    User findById(Long id);
    User changePass(UpdateUserRequest userRequest) throws RegisterException;
    void active(String email);
    UserResponse lock(Long id);
    Page<UserResponse> findAll(String name, int page, int size);
    String sendVerification(String email);
    void passwordRetrieval(ForgotPassForm forgotPassForm) throws ForgotPassWordException;
}
