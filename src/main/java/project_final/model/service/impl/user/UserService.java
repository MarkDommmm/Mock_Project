package project_final.model.service.impl.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.exception.RegisterException;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.model.entity.RoleName;
import project_final.model.entity.User;
import project_final.model.repository.IUserRepository;

import project_final.model.service.mapper.user.IUserMapper;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private IUserRepository userRepository;

    private IUserMapper userMapper;

    @Override
    public boolean checkPassword(String password, String confirm_password) {
        return password.equals(confirm_password);
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(UserRequest userRequest) throws RegisterException {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RegisterException("User is exits");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RegisterException("Email is exits");
        }
        if (!checkPassword(userRequest.getPassword(), userRequest.getConfirm_password())) {
            throw new RegisterException("Password not match");
        }
        return userRepository.save(userMapper.toEntity(userRequest));
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public UserResponse lock(Long id) {
        Optional<User> users = userRepository.findById(id);
        if (users.isPresent()) {
            users.get().setStatus(!users.get().isStatus());
            return userMapper.toResponse(userRepository.save(users.get()));
        }
        return null;
    }

    @Override
    public Page<UserResponse> findAll(String name, int page, int size) {
        Page<User> users = userRepository.findAllUsersWithUserRoleAndUseAndUsernameContaining(RoleName.ROLE_USER, name, PageRequest.of(page,size));
        return users.map(user -> userMapper.toResponse(user));
    }
}
