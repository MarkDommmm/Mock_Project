package project_final.model.service.impl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_final.exception.RegisterException;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.model.entity.Role;
import project_final.model.entity.RoleName;
import project_final.model.entity.User;
import project_final.model.repository.IUserRepository;
import project_final.model.service.impl.role.IRoleService;
import project_final.model.service.mapper.user.IUserMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserMapper userMapper;

    @Override
    public boolean checkPassword(String password, String confirm_password) {
        if (password.equals(confirm_password)) {
            return true;
        }
        return false;
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
        return null;
    }

    @Override
    public List<UserResponse> findAll() {
        return null;
    }
}
