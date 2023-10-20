package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Reservation;
import project_final.entity.ReservationMenu;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableMenuExportDTO {
    private List<String> Food;
    private Reservation reservation;
    private List<ReservationMenu> reservationMenus;


}
