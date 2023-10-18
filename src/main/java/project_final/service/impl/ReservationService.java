package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project_final.entity.Reservation;
import project_final.entity.TableMenu;
import project_final.entity.User;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationResponse;
import project_final.repository.IReservationRepository;
import project_final.repository.ITableMenuRepository;
import project_final.service.IReservationService;
import project_final.service.mapper.IReservationMapper;

import java.util.Date;
import java.util.Optional;

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
        Page<Reservation> reservations = reservationRepository.findAllByCreatedDate(date, PageRequest.of(page, size));
        return reservations.map(reservationMapper::toResponse);
    }

    @Override
    public ReservationResponse findById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(reservationMapper::toResponse).orElse(null);
    }

    @Override
    public void save(ReservationRequest reservationRequest, Reservation reservation) {
        System.out.println(reservationRequest.getBookingDate().getTime());
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
        Reservation r = reservationMapper.toEntity(request);
        reservationRepository.save(r);
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
//        double revenue = reservationRepository.revenuesOnDay(date);
//        return revenue;
        return 0;
    }

    @Override
    public int countCompletedReservationsOnDay(Date date) {
        int count = reservationRepository.countCompletedReservationsOnDay(date);
        return count;
    }
}
