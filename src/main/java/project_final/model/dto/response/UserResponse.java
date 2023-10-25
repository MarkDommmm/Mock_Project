package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String avatar;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Set<Role> roles;
    private boolean status;
    private boolean active;
}
