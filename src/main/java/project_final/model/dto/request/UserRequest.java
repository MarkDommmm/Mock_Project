package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private Long id;
    private String name;
    private MultipartFile avatar;
    @NotBlank(message="Username not blank")
    private String username;
    @NotBlank(message="Password not blank")
    private String password;
    @NotBlank(message="ConfirmPassword not blank")
    private String confirm_password;
    @NotBlank(message="Email not blank")
    private String email;
    private String phone;
    private Set<Long> role;

}
