package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.TableType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableRequest {
    private Long id;
    @NotBlank(message="Name not blank")
    private String name;
    @NotNull(message="Image not null")
    private MultipartFile tableImage;
    private TableType tableType;
    private String description;

}
