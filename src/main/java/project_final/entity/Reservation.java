package project_final.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_final.model.domain.Status;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

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
        private Date bookingDate;
        private Time startTime;
        private Time endTime;
        private String emailBooking;
        private String phoneBooking;
        private String nameBooking;
        private String description;
        private String code;

    @Enumerated(EnumType.STRING)
    private Status status;
}
