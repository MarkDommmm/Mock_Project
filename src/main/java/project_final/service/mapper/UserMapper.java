package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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

    @Override
    public User toEntity(UserRequest userRequest) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRoleName(RoleName.ROLE_USER));

        MultipartFile oldImage = userRequest.getAvatar();
        if (oldImage.isEmpty()){
            Optional<User> user = userRepository.findById(userRequest.getId());
            return User.builder()
                    .id(userRequest.getId())
                    .name(userRequest.getName())
                    .username(userRequest.getUsername())
                    .avatar(user.get().getAvatar())
                    .password(user.get().getPassword())
                    .email(userRequest.getEmail())
                    .phone(userRequest.getPhone())
                    .roles(roles)
                    .status(true).build();
        }
        if (userRequest.getPassword() == null) {
            String url = uploadService.uploadFile(userRequest.getAvatar());
            Optional<User> user = userRepository.findById(userRequest.getId());
            return User.builder()
                    .id(userRequest.getId())
                    .name(userRequest.getName())
                    .username(userRequest.getUsername())
                    .avatar(url)
                    .password(user.get().getPassword())
                    .email(userRequest.getEmail())
                    .phone(userRequest.getPhone())
                    .roles(roles)
                    .status(true).build();
        }else {
            String url = uploadService.uploadFile(userRequest.getAvatar());
            return User.builder()
                    .avatar(url)
                    .name(userRequest.getName())
                    .username(userRequest.getUsername())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .email(userRequest.getEmail())
                    .roles(roles)
                    .status(true).build();
        }

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
