package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.Payment;
import project_final.entity.Reservation;
import project_final.entity.Tables;
import project_final.entity.User;
import project_final.exception.TimeIsValidException;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationCheckCodeResponse;
import project_final.model.dto.response.ReservationResponse;
import project_final.repository.*;
import project_final.service.IReservationService;
import project_final.service.mapper.IReservationMapper;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class ReservationService implements IReservationService<ReservationRequest, ReservationResponse, Long> {
    private final IReservationRepository reservationRepository;
    private final IReservationMapper reservationMapper;
    private final IReservationMenuRepository reservationMenuRepository;
    private final IPaymentRepository paymentRepository;
    private final IUserRepository userRepository;
    private final ITableRepository tableRepository;


    @Override
    public Page<ReservationResponse> findByUserIdAndStatusPending(int page, int size, Long id) {
        Page<Reservation> reservations = reservationRepository.findAllByUserAndStatusPending(PageRequest.of(page, size), id);
        return reservations.map(reservationMapper::toResponse);
    }

    @Override
    public Page<ReservationResponse> findAllByUserAndStatusCompleted(int page, int size, Long id) {
        Page<Reservation> reservations = reservationRepository.findAllByUserAndStatusCompleted(PageRequest.of(page, size), id);
        return reservations.map(reservationMapper::toResponse);
    }

    @Override
    public Page<ReservationResponse> findAllByUserAndStatusCancel(int page, int size, Long id) {
        Page<Reservation> reservations = reservationRepository.findAllByUserAndStatusCancel(PageRequest.of(page, size), id);
        return reservations.map(reservationMapper::toResponse);
    }

    @Override
    public Page<ReservationResponse> findAllByUserAndStatusConfirm(int page, int size, Long id) {
        Page<Reservation> reservations = reservationRepository.findAllByUserAndStatusConfirm(PageRequest.of(page, size), id);
        return reservations.map(reservationMapper::toResponse);
    }

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
    public List<ReservationResponse> findAllByStatusORDER() {
        return reservationRepository.findAllByStatusORDER().stream()
                .map(reservationMapper::toResponse).collect(Collectors.toList());
    }


    @Override
    public Reservation add(Long user, Date date, String start, String end, Long idTable) throws TimeIsValidException {


        Optional<User> u = userRepository.findById(user);
        Optional<Tables> table = tableRepository.findById(idTable);
        Optional<Payment> payment = paymentRepository.findById(1L);
        ReservationRequest reservation = ReservationRequest.builder()
                .user(u.get())
                .code(UUID.randomUUID().toString().substring(0, 8))
                .table(table.get())
                .bookingDate(date)
                .startTime(start + ":00")
                .endTime(end + ":00")
                .status(Status.PENDING)
                .payment(payment.get())
                .build();
        return reservationRepository.save(reservationMapper.toEntity(reservation));
    }


    @Override
    public void save(ReservationRequest reservationRequest) throws TimeIsValidException {
        reservationRepository.save(reservationMapper.toEntity(reservationRequest));
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

    private static boolean isValidTimeRange(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            // Kiểm tra startTime phải sau 9:00 và endTime phải trước 23:00
            if (start.isAfter(LocalTime.parse("09:00")) && end.isBefore(LocalTime.parse("23:00"))) {
                return true;
            } else {
                return false;
            }
        } catch (DateTimeParseException e) {
            return false; // Trả về false nếu có lỗi khi parse thời gian
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
    public void changeStatusOrder(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservation.get().setStatus(Status.ORDER);
            reservationRepository.save(reservation.get());
        }
    }

    @Override
    public String cancel(Long id, Long idUser) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            LocalDateTime currentTime = LocalDateTime.now();
            Date reservationDate = reservation.get().getBookingDate();
            Time reservationTime = reservation.get().getStartTime();

            LocalDateTime reservationDateTime = LocalDateTime.ofInstant(reservationDate.toInstant(), ZoneId.systemDefault())
                    .plusHours(reservationTime.toLocalTime().getHour())
                    .plusMinutes(reservationTime.toLocalTime().getMinute());

            // Kiểm tra nếu thời điểm đặt phòng cách thời điểm hiện tại ít nhất 4 tiếng
            if (ChronoUnit.HOURS.between(currentTime, reservationDateTime) >= 4) {
                if (idUser.equals(reservation.get().getUser().getId())) {
                    reservation.get().setStatus(Status.CANCEL);
                    reservationRepository.save(reservation.get());
                    return "Reservation canceled successfully";
                } else {
                    return "You don't have permission to cancel this reservation";
                }
            } else {
                return "You cannot cancel reservation within 4 hours of booking. If you want to cancel, please contact Hotline 777 Thank you!";
            }
        }
        return "Reservation not found";
    }


    @Override
    public void completed(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservation.get().setStatus(Status.COMPLETED);
            reservationRepository.save(reservation.get());
        }
    }

    @Override
    public void noShow(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservation.get().setStatus(Status.NO_SHOW);
            reservationRepository.save(reservation.get());
        }
    }

    @Override
    public ReservationCheckCodeResponse findByCode(String code) {
        return reservationMapper.toResponseCheckCode(reservationRepository.findByCode(code));
    }

    @Override
    public double getTotalPrice(Long id) {
        return reservationRepository.getTotalPrice(id);
    }

    @Override
    public double getTotalPaid(Long id) {
        return reservationRepository.getTotalPaid(id);
    }


    @Override
    public Page<Map<String, Object>> findReservationStatistics(int page, int size) {
        return reservationRepository.getReservationStatistics(PageRequest.of(page, size));
    }
}

