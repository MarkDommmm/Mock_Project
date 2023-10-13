package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableTypeRequest {
    private Long id;
    @NotBlank(message="Name not blank")
    private String name;
    @NotNull(message="Image name not null")
    private MultipartFile image;
    private String description;
}
