package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoryRequest {
    private Long id;
    @NotBlank(message = "Name not blank")
    private String name;
    @NotNull(message = "Image not null")
    private MultipartFile image;
    @NotBlank(message = "Description not blank")
    private String description;
    private boolean status;
}
