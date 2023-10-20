package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.model.domain.Status;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCheckCodeResponse {
    private Long id;
    private Long idUser;
    private String table;
    private Date bookingDate;
    private Date createdDate;
    private Time startTime;
    private Time endTime;
    private String emailBooking;
    private String phoneBooking;
    private String nameBooking;
    private String description;
    private String code;
    private String payment;
    private Status status;
}
