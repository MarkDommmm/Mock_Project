package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Reservation;
import project_final.entity.TableMenu;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableMenuExportDTO {
//    private Long id;
//    private String Code;
//    private String CreatedDate;
//    private String BookingDate;
//    private String StartTime;
//    private String EndTime;
//    private String Description;
    private List<String> Food;
    private Reservation reservation;
    private List<TableMenu> tableMenu;


}
