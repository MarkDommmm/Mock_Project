package project_final.service.mapper;

import org.springframework.stereotype.Component;
import project_final.entity.Reservation;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationCheckCodeResponse;
import project_final.model.dto.response.ReservationResponse;

import java.sql.Time;
import java.util.Date;

@Component

public class ReservationMapper implements IReservationMapper {

    @Override
    public Reservation toEntity(ReservationRequest reservationRequest) {
        return Reservation.builder()
                .id(reservationRequest.getId())
                .user(reservationRequest.getUser())
                .table(reservationRequest.getTable())
                .createdDate(new Date())
                .bookingDate(reservationRequest.getBookingDate())
                .startTime(Time.valueOf(reservationRequest.getStartTime()))
                .endTime(Time.valueOf(reservationRequest.getEndTime()))
                .emailBooking(reservationRequest.getEmailBooking())
                .nameBooking(reservationRequest.getNameBooking())
                .phoneBooking(reservationRequest.getPhoneBooking())
                .description(reservationRequest.getDescription())
                .payment(reservationRequest.getPayment())
                .code(reservationRequest.getCode())
                .status(reservationRequest.getStatus())
                .build();
    }

    public ReservationCheckCodeResponse toResponseCheckCode(Reservation reservation) {
        return ReservationCheckCodeResponse.builder()
                .id(reservation.getId())
                .idUser(reservation.getUser().getId())
                .table(reservation.getTable().getName())
                .createdDate(reservation.getCreatedDate())
                .bookingDate(reservation.getBookingDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .nameBooking(reservation.getNameBooking())
                .emailBooking(reservation.getEmailBooking())
                .phoneBooking(reservation.getPhoneBooking())
                .description(reservation.getDescription())
                .code(reservation.getCode())
                .payment(reservation.getPayment().getPaymentMethod())
                .status(reservation.getStatus()).build();
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
                .payment(reservation.getPayment().getPaymentMethod())
                .status(reservation.getStatus()).build();
    }
}
