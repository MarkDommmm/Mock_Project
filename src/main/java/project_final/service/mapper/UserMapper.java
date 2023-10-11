package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project_final.model.dto.request.UpdateUserRequest;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.entity.Role;
import project_final.model.domain.RoleName;
import project_final.entity.User;
import project_final.repository.IUserRepository;
import project_final.service.IRoleService;
import project_final.service.IUploadService;
import project_final.service.mapper.IUserMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserMapper implements IUserMapper {
    private final IUploadService uploadService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;



    public User toUpdate(UpdateUserRequest userRequest) {
        Optional<User> user = userRepository.findById(userRequest.getId());

        String newPassword = userRequest.getNew_password();
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        System.out.println("passOld" + user.get().getPassword());
        if (userRequest.getAvatar().isEmpty()) {
            return User.builder()
                    .id(userRequest.getId())
                    .name(userRequest.getName())
                    .avatar(user.get().getAvatar())
                    .password(user.get().getPassword())
                    .phone(userRequest.getPhone())
                    .build();
        }

        String url = uploadService.uploadFile(userRequest.getAvatar());
        return User.builder()
                .id(userRequest.getId())
                .avatar(url)
                .password(userRequest.getPassword() != null ?
                        passwordEncoder.encode(userRequest.getPassword()) :
                        user.get().getPassword())
                .phone(userRequest.getPhone())
                .build();
    }


    @Override
    public User toEntity(UserRequest userRequest) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
            return User.builder()
                    .avatar("../../assets/images/avatars/01.png")
                    .name(userRequest.getName())
                    .username(userRequest.getUsername())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .email(userRequest.getEmail())
                    .roles(roles)
                    .status(true).build();
    }

    @Override
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .status(user.isStatus())
                .build();
    }
}
