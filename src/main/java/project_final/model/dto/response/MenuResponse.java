package project_final.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Category;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponse {
    private Long id;
    private String name;
    private String image;
    private String description;
    private double price;
    private boolean status;
    private Date creatDate;
    private Category category;
}
