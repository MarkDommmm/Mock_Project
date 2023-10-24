package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import project_final.entity.Payment;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.model.domain.Status;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Future(message = "Set date is not valid")
    @NotNull(message = "Cannot be left blank")
    private Date bookingDate;
    @NotBlank(message = "Cannot be left blank")
    private String startTime;
    @NotBlank(message = "Cannot be left blank")
    private String endTime;
    @NotBlank(message = "Cannot be left blank")
    @Email(message = "Email is valid")
    private String emailBooking;
    @NotBlank(message = "Cannot be left blank")
    private String phoneBooking;
    @NotBlank(message = "Cannot be left blank")
    private String nameBooking;
    private String description;
    private Payment payment;
    private String code;
    @Enumerated(EnumType.STRING)
    private Status status;
}
