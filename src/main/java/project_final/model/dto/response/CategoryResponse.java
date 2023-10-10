package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String image;
    private String description;
    private boolean status;
}
