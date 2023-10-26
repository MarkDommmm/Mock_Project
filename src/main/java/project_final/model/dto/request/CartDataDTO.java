package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.model.domain.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDataDTO {
    private Long id;
    private Long menuId;
    private Long reservationId;
    private int quantityOrdered ;
    private int quantityDelivered;
    private double price;
    private Status status;
    private Status pay;

}
