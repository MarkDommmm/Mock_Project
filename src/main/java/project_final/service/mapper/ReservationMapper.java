package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.entity.Reservation;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationResponse;
import project_final.repository.ITableMenuRepository;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Component

public class ReservationMapper implements IReservationMapper{

    @Override
    public Reservation toEntity(ReservationRequest reservationRequest) {
        return Reservation.builder()
                .id(reservationRequest.getId())
                .user(reservationRequest.getUser())
                .table(reservationRequest.getTable())
                .createdDate(new Date())
                .bookingDate(reservationRequest.getBookingDate())
                .startTime(Time.valueOf(reservationRequest.getStartTime() + ":00"))
                .endTime(Time.valueOf(reservationRequest.getEndTime() + ":00"))
                .emailBooking(reservationRequest.getEmailBooking())
                .nameBooking(reservationRequest.getNameBooking())
                .phoneBooking(reservationRequest.getPhoneBooking())
                .description(reservationRequest.getDescription())
                .code(UUID.randomUUID().toString().substring(0, 8))
                .status(Status.PENDING)
                .build();
    }


    @Override
    public ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .user(reservation.getUser())
                .table(reservation.getTable())
                .createdDate(reservation.getCreatedDate())
                .bookingDate(reservation.getBookingDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .nameBooking(reservation.getNameBooking())
                .emailBooking(reservation.getEmailBooking())
                .phoneBooking(reservation.getPhoneBooking())
                .description(reservation.getDescription())
                .code(reservation.getCode())
                .status(reservation.getStatus()).build();
    }
}
