package project_final.model.service.impl.user;

import project_final.exception.RegisterException;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    boolean checkPassword(String password,String confirm_password);
    Optional<User> findByUserName(String username);
    User save(UserRequest userRequest) throws RegisterException;
    User findById(Long id);
    UserResponse lock(Long id);
    List<UserResponse> findAll();
}
