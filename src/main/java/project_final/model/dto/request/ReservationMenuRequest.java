package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Menu;
import project_final.entity.Reservation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationMenuRequest {
    private Long id;
    private Menu menu;

    private Reservation reservation;
    private int quantity = 1;
    private double price;
    private boolean status;

}