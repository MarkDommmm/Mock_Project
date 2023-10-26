package project_final.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequest {
    private Long id;
    @NotBlank(message = "Name not blank")
    private String name;
    private MultipartFile image;
    private String description;
    @NotNull
    @Positive(message = "Price must be greater than 0")
    private double price;
    private Category category;
}
