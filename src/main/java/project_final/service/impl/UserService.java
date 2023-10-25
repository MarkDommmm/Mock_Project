package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project_final.exception.ForgotPassWordException;
import project_final.exception.RegisterException;
import project_final.model.dto.request.ActiveForm;
import project_final.model.dto.request.ForgotPassForm;
import project_final.model.dto.request.UpdateUserRequest;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.model.domain.RoleName;
import project_final.entity.User;
import project_final.repository.IUserRepository;

import project_final.service.IUserService;
import project_final.service.IVerificationService;
import project_final.service.mapper.IUserMapper;

import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final IVerificationService verificationService;

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
    public User update(UpdateUserRequest userRequest) throws RegisterException {
        return userRepository.save(userMapper.toUpdate(userRequest));
    }



    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User changePass(UpdateUserRequest userRequest) throws RegisterException {
//        Optional<User> user = userRepository.findById(userRequest.getId());
//
//        if (!checkPassword(userRequest.getNew_password(), userRequest.getConfirm_password())) {
//            throw new RegisterException("New passwords do not match");
//        }
//
//        if (passwordEncoder.matches(userRequest.getPassword(), user.get().getPassword())) {
//
//            String newPassword = userRequest.getNew_password();
//            String encodedNewPassword = passwordEncoder.encode(newPassword);
//            user.get().setPassword(encodedNewPassword);
//            user.get().setName(userRequest.getName());
//            user.get().setPhone(userRequest.getPhone());
//
//            return userRepository.save(user.get());
//        } else {
//            throw new RegisterException("Old password does not match");
//        }
        return null;
    }

    @Override
    public void active(String email) {
        User user = userRepository.findByEmail(email);

            if(user!=null) {
                user.setActive(true);
                userRepository.save(user);
            }

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
        Page<User> users = userRepository.findAllUsersWithUserRoleAndUseAndUsernameContaining(RoleName.ROLE_USER, name, PageRequest.of(page, size));
        return users.map(user -> userMapper.toResponse(user));
    }

    @Override
    public String sendVerification(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String verification = verificationService.create(user).getVerification();
            return verification;
        }
        return null;
    }

    @Override
    public void passwordRetrieval(ForgotPassForm forgotPassForm) throws ForgotPassWordException{
        User user = userRepository.findByEmail(forgotPassForm.getEmail());
        if(user == null){
            throw new ForgotPassWordException("User not found");
        }else if (!verificationService.isExpired(user)){
            throw new ForgotPassWordException("Verification code is not valid ");
        }else if(!forgotPassForm.getPassword().equals(forgotPassForm.getConfirmPassword())){
            throw new ForgotPassWordException("Confirm password is not valid");
        } else {
            user.setPassword(passwordEncoder.encode(forgotPassForm.getPassword()));
            userRepository.save(user);
        }
    }
}
