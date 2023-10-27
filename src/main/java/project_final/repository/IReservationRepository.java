package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.Reservation;
import project_final.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT R FROM Reservation R WHERE R.user.id = :userId AND R.status ='PENDING' ")
    Page<Reservation> findAllByUserAndStatusPending(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT R FROM Reservation R WHERE R.user.id = :userId AND R.status ='COMPLETED'")
    Page<Reservation> findAllByUserAndStatusCompleted(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT R FROM Reservation R WHERE R.user.id = :userId AND R.status ='CANCEL'")
    Page<Reservation> findAllByUserAndStatusCancel(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT R FROM Reservation R WHERE R.user.id = :userId AND R.status ='CONFIRM'")
    Page<Reservation> findAllByUserAndStatusConfirm(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT R FROM Reservation R WHERE   R.status ='ORDER' ")
    List<Reservation> findAllByStatusORDER();

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.status = 'ORDER'")
    Optional<Reservation> findOrderReservationByUserId(@Param("userId") Long userId);

    @Query("SELECT R FROM Reservation R WHERE R.user.id = :userId")
    Page<Reservation> findAllByUser(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT R FROM Reservation R WHERE DATE(R.bookingDate) = DATE(:date)")
    Page<Reservation> findAllByBookingDate(@Param("date") Date date, Pageable pageable);


    Reservation findByCode(String code);

    @Query("SELECT COUNT(R) FROM Reservation R WHERE DATE(R.createdDate) = :date AND  R.status = 'COMPLETED'")
    int countCompletedReservationsOnDay(@Param("date") Date date);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.status = 'PENDING'")
    Optional<Reservation> findPendingReservationByUserId(@Param("userId") Long userId);

//    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.id =: idR AND r.status = 'PENDING'")
//    Optional<Reservation> findPendingReservationByUser(@Param("userId") Long userId, @Param("idR") Long idR);

    @Query("SELECT r FROM Reservation r WHERE r.id = :idR AND r.status = 'PENDING'")
    Optional<Reservation> findPendingReservationByReservationId(@Param("idR") Long idR);

    @Query(value = "SELECT r.booking_date as bookingDate, " +
            "(SELECT COUNT(DISTINCT r1.id) FROM reservation r1 " +
            " JOIN reservation_menu t1 ON r1.id = t1.reservation_id " +
            " WHERE r1.status = 'COMPLETED' AND r1.booking_date = r.booking_date) as successfulOrders, " +
            "(SELECT COUNT(DISTINCT r2.id) FROM reservation r2 " +
            " JOIN reservation_menu t2 ON r2.id = t2.reservation_id " +
            " WHERE r2.status = 'CANCEL' AND r2.booking_date = r.booking_date) as failedOrders, " +
            "(SELECT SUM(t3.quantity_ordered * t3.price) FROM reservation r3 " +
            " JOIN reservation_menu t3 ON r3.id = t3.reservation_id " +
            " WHERE r3.status = 'COMPLETED' AND r3.booking_date = r.booking_date) as totalAmount " +
            " FROM reservation r GROUP BY r.booking_date" +
            " ORDER BY r.booking_date DESC",
            nativeQuery = true)
    Page<Map<String, Object>> getReservationStatistics(Pageable pageable);


    @Query("SELECT COALESCE(ROUND(SUM(RM.quantityOrdered * RM.price),1) ,0) FROM Reservation R " +
            "JOIN ReservationMenu RM ON R.id = RM.reservation.id " +
            "WHERE R.id = :id")
    double getTotalPrice(@Param("id") Long id);


    @Query("SELECT COALESCE(ROUND (SUM(RM.quantityOrdered * RM.price),1), 0) FROM Reservation R " +
            "JOIN ReservationMenu RM ON R.id = RM.reservation.id " +
            "WHERE R.id = :id AND RM.pay = 'PAID'")
    double getTotalPaid(@Param("id") Long id);


    @Query("SELECT R.bookingDate as date, " +
            "SUM(CASE WHEN HOUR(R.endTime) <= 17 THEN 1 ELSE 0 END) AS dayTime, " +
            "SUM(CASE WHEN HOUR(R.endTime) > 17 THEN 1 ELSE 0 END) AS nightTime " +
            "FROM Reservation R WHERE R.status = 'COMPLETED' " +
            "GROUP BY R.bookingDate")
    List<Map<String, Object>> getByTime();


}



