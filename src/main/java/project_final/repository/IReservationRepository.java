package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.Reservation;

import java.util.Date;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    Page<Reservation> findAllByCreatedDate(Date date, Pageable pageable);
}
