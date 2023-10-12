package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDataDTO {
    private Long id;
    private Long menuId;
    private Long reservationId;
    private int quantity;
    private double price;
    private boolean status;

}
