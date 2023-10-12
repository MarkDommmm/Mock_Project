package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.Reservation;
import project_final.entity.User;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationResponse;
import project_final.repository.IReservationRepository;
import project_final.service.IReservationService;
import project_final.service.mapper.IReservationMapper;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService implements IReservationService<ReservationRequest, ReservationResponse,Long> {
   private final IReservationRepository reservationRepository;
   private final IReservationMapper reservationMapper;
    @Override
    public Page<ReservationResponse> findAll(Date date, int page, int size) {
        Page<Reservation> reservations = reservationRepository.findAllByCreatedDate(date, PageRequest.of(page, size));
        return reservations.map(reservationMapper::toResponse);
    }

    @Override
    public ReservationResponse findById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            return reservationMapper.toResponse(reservation.get());
        }
        return null;
    }

    @Override
    public void save(ReservationRequest reservationRequest) {
        reservationRepository.save(reservationMapper.toEntity(reservationRequest));
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
    public void cancel(Long id, User user) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            if (user.equals(reservation.get().getUser())) {}
            reservation.get().setStatus(Status.CANCEL);
            reservationRepository.save(reservation.get());
        }
    }

    @Override
    public double revenuesOnDay(Date date ) {
        double revenue = reservationRepository.revenuesOnDay(date);
        return revenue;
    }

    @Override
    public int countCompletedReservationsOnDay(Date date) {
        int count = reservationRepository.countCompletedReservationsOnDay(date);
        return count;
    }
}
