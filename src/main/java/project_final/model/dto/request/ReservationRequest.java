package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import project_final.entity.TableMenu;
import project_final.entity.Tables;
import project_final.entity.User;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.sql.Time;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    private Long id;
    private User user;
    private Tables table;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

    private String startTime;

    private String endTime;
    @NotBlank
    private String emailBooking;
    @NotBlank
    private String phoneBooking;
    @NotBlank
    private String nameBooking;

    private String description;
    private String code;
}
