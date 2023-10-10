package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoryRequest {
    private Long id;
    private String name;
    private MultipartFile image;
    private String description;
    private boolean status;
}
