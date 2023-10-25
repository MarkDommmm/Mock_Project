package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.TableType;
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
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserMapper implements IUserMapper {
    private final IUploadService uploadService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;


    public User toUpdate(UpdateUserRequest userRequest) {
        Optional<User> user = userRepository.findById(userRequest.getId());
        String avatar;
        if (userRequest.getAvatar() != null && !userRequest.getAvatar().isEmpty()) {
            // nếu có ảnh mới
            avatar = uploadService.uploadFile(userRequest.getAvatar());
        } else {
            avatar = user.get().getAvatar();
        }
        return User.builder()
                .id(userRequest.getId())
                .name(userRequest.getName())
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .email(user.get().getEmail())
                .avatar(avatar)
                .phone(userRequest.getPhone())
                .status(user.get().isStatus())
                .roles(user.get().getRoles())
                .build();
    }


    @Override
    public User toEntity(UserRequest userRequest) {
        Optional<User> user = userRequest.getId() != null ?
                userRepository.findById(userRequest.getId()) :
                Optional.empty();

        String image;
        if (userRequest.getAvatar() != null && !userRequest.getAvatar().isEmpty()) {
            // nếu có ảnh mới
            image = uploadService.uploadFile(userRequest.getAvatar());
        } else if (user.isPresent()) {
            // nếu user  tồn tại
            image = user.get().getAvatar();
        } else {
            // không có ảnh và không tồn tại user
            image = "../../assets/images/avatars/01.png";
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
        return User.builder()
                .id(userRequest.getId())
                .avatar(image)
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .activeCode(UUID.randomUUID().toString().substring(0,6))
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .roles(roles)
                .active(false)
                .status(true).build();
    }

    @Override
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .status(user.isStatus())
                .active(user.isActive())
                .build();
    }
}
