package project_final.service.mapper;

import org.springframework.stereotype.Component;
import project_final.entity.Reservation;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationResponse;

import java.util.Date;
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
                .startTime(reservationRequest.getStartTime())
                .endTime(reservationRequest.getEndTime())
                .status(Status.PENDING).build();
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
                .status(reservation.getStatus()).build();
    }
}
