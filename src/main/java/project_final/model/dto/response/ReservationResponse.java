package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.model.domain.Status;


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
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date bookingDate;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
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
