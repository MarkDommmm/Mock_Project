package project_final.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.model.domain.Status;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableMenuCartResponse {
    private Long id;
    private String image;
    private String name;
    private Date dateBooking;
    private Time startTime;
    private Time endTime;
    private int quantityOrdered ;
    private int quantityDelivered ;
    private double price;
    private Status status;
    private Status pay;
}
