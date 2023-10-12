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

@Repository
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    Page<Reservation> findAllByCreatedDate(Date date, Pageable pageable);
    Reservation findByUser(User user);

    @Query(value = "SELECT SUM(T.price * T.quantity) " +
            "FROM Reservation R " +
            "JOIN TableMenu T ON R = T.reservation " +
            "WHERE DATE(R.createdDate) = :date AND R.status = 'COMPLETED'")
    double revenuesOnDay(@Param("date") Date date);

    @Query("SELECT COUNT(R) FROM Reservation R WHERE DATE(R.createdDate) = :date AND  R.status = 'COMPLETED'")
    int countCompletedReservationsOnDay(@Param("date") Date date);



}
