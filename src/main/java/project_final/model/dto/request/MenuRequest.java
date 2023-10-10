package project_final.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.Category;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequest {
    private Long id;
    private String name;
    private MultipartFile image;
    private String description;
    private double price;
    private Category category;
}
