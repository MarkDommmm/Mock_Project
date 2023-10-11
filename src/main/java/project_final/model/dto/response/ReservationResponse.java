package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.model.domain.Status;


import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;
    private User user;
    private Tables table;
    private Date createdDate;
    private Date startTime;
    private Date endTime;
    private Status status;
}
