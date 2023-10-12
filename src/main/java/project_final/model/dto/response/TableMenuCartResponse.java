package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableMenuCartResponse {
    private Long id;
    private String image;
    private String name;
    private Long reservation;
    private int quantity;
    private double price;
    private boolean status;
}
