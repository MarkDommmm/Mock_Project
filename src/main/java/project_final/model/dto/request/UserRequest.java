package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private Long id;
    private String name;
    private MultipartFile avatar;
    @Size(min = 6, message = "Username must be at least 6 characters long")
    @Pattern(regexp = "\\S+", message = "Username must not contain whitespace")
    private String username;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "\\S+", message = "Password must not contain whitespace")
    private String password;
    @NotBlank(message="ConfirmPassword not blank")
    private String confirm_password;
    @NotBlank(message="Email not blank")
    @Email(message = "Invalid email format")
    private String email;
    private String phone;
    private Set<Long> role;
}
