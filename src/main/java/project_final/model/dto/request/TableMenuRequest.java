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
public class TableMenuRequest {
    private Long id;
    private Menu menu;
    private Reservation reservation;
    private int quantity;
    private double price;
    private boolean status;
}
