package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import project_final.entity.TableMenu;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.model.domain.Status;


import javax.validation.constraints.NotBlank;
import java.sql.Time;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;
    private User user;
    private Tables table;

    private Date bookingDate;
    private Date createdDate;
    private Time startTime;
    private Time endTime;

    private String emailBooking;

    private String phoneBooking;
    private String nameBooking;
    private String description;
    private String code;
    private Status status;
}
