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
import java.util.Optional;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT R FROM Reservation R WHERE R.user.id = :userId")
    Page<Reservation> findAllByUser(Pageable pageable, @Param("userId") Long userId);
    @Query("SELECT R FROM Reservation R WHERE DATE(R.bookingDate) = DATE(:date)")
    Page<Reservation> findAllByBookingDate(@Param("date") Date date, Pageable pageable);

    Reservation findByUser(User user);



    @Query("SELECT COUNT(R) FROM Reservation R WHERE DATE(R.createdDate) = :date AND  R.status = 'COMPLETED'")
    int countCompletedReservationsOnDay(@Param("date") Date date);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.status = 'PENDING'")
    Optional<Reservation> findPendingReservationByUserId(@Param("userId") Long userId);

}
