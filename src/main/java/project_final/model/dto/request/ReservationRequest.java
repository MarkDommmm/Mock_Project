package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Tables;
import project_final.entity.User;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Tables table;
    private Date startTime;
    private Date endTime;
}
