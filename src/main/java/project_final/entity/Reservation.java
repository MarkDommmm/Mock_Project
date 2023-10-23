package project_final.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import project_final.model.domain.Status;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        @ManyToOne(fetch = FetchType.LAZY)
        private User user;
        @ManyToOne(fetch = FetchType.LAZY)
        private Tables table;
        private Date createdDate;
        @DateTimeFormat(pattern = "MM/dd/yyyy")
        private Date bookingDate;
        private Time startTime;
        private Time endTime;
        private String emailBooking;
        private String phoneBooking;
        private String nameBooking;
        private String description;
        private String code;
        @OneToOne
        private Payment payment;

    @Enumerated(EnumType.STRING)
    private Status status;
}
