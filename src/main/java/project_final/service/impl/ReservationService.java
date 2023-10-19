package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.Reservation;
import project_final.entity.User;
import project_final.exception.TimeIsValidException;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationResponse;
import project_final.repository.IReservationRepository;
import project_final.repository.ITableMenuRepository;
import project_final.service.IReservationService;
import project_final.service.mapper.IReservationMapper;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService implements IReservationService<ReservationRequest, ReservationResponse,Long> {
   private final IReservationRepository reservationRepository;
   private final IReservationMapper reservationMapper;
   private final ITableMenuRepository tableMenuRepository;

    @Override
    public Page<ReservationResponse> findByUserId(int page, int size, Long userId) {
        Page<Reservation> reservations = reservationRepository.findAllByUser(PageRequest.of(page, size), userId);
        return reservations.map(reservationMapper::toResponse);
    }

    @Override
    public Page<ReservationResponse> findAll(Date date, int page, int size) {
        Page<Reservation> reservations = reservationRepository.findAllByBookingDate(date, PageRequest.of(page, size));
        return reservations.map(reservationMapper::toResponse);
    }

    @Override
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream().map(reservationMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public ReservationResponse findById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(reservationMapper::toResponse).orElse(null);
    }

    @Override

    public void save(ReservationRequest reservationRequest, Reservation reservation) throws TimeIsValidException {
        if (isEndTimeAfterStartTime(reservationRequest.getEndTime(),reservationRequest.getStartTime())){
            throw new TimeIsValidException("End time must be must be larger start time");
        }

       ReservationRequest request = ReservationRequest.builder()
               .id(reservation.getId())
               .table(reservation.getTable())
               .user(reservation.getUser())
               .bookingDate(reservationRequest.getBookingDate())
               .startTime(reservationRequest.getStartTime())
               .endTime(reservationRequest.getEndTime())
               .nameBooking(reservationRequest.getNameBooking())
               .phoneBooking(reservationRequest.getPhoneBooking())
               .emailBooking(reservationRequest.getEmailBooking())
               .description(reservationRequest.getDescription())
               .build();
        reservationRepository.save(reservationMapper.toEntity(request));
    }
    private boolean isEndTimeAfterStartTime(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            return end.isAfter(start);
        } catch (DateTimeParseException e) {
            return false;
        }
    }




    @Override
    public void confirm(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservation.get().setStatus(Status.CONFIRM);
            reservationRepository.save(reservation.get());
        }
    }

     @Override
    public void cancel(Long id, Long idUser) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            if (idUser.equals(reservation.get().getUser().getId())) {}
            reservation.get().setStatus(Status.CANCEL);
            reservationRepository.save(reservation.get());
        }
    }

    @Override
    public double revenuesOnDay(Date date ) {
        return 0;
    }

    @Override
    public int countCompletedReservationsOnDay(Date date) {
        return reservationRepository.countCompletedReservationsOnDay(date);
    }

    @Override
    public Page<Map<String, Object>> findReservationStatistics(int page, int size) {
        return reservationRepository.getReservationStatistics(PageRequest.of(page, size));
    }
}

