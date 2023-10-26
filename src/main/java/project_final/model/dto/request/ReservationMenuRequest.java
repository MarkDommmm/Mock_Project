package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Menu;
import project_final.entity.Reservation;
import project_final.model.domain.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationMenuRequest {
    private Long id;
    private Menu menu;

    private Reservation reservation;
    private int quantityOrdered = 1;
    private int quantityDelivered = 0;
    private double price;
    private Status status;
    private Status pay;
}
