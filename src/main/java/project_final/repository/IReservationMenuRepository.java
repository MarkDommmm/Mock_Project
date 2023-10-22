package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.ReservationMenu;

import java.util.List;


@Repository
public interface IReservationMenuRepository extends JpaRepository<ReservationMenu, Long> {
    @Query("SELECT RM FROM ReservationMenu RM JOIN RM.menu M WHERE M.name LIKE %:name%")
    Page<ReservationMenu> findAllByMenuName(@Param("name") String name, Pageable pageable);

    @Query("SELECT RM FROM ReservationMenu RM WHERE RM.reservation.user.id = :id AND RM.reservation.status = 'PENDING'")
    Page<ReservationMenu> findAllByUser(@Param("id") Long id, Pageable pageable);

    @Query("SELECT RM FROM ReservationMenu RM WHERE RM.reservation.id = :id")
    List<ReservationMenu> findAllByReservation(@Param("id") Long id);

    @Query("SELECT M.name, SUM(RM.quantity),MONTH(R.createdDate) as month " +
            "FROM ReservationMenu RM " +
            "JOIN RM.reservation R " +
            "JOIN RM.menu M " +
            "GROUP BY M.name, MONTH(R.createdDate)")
    List<Object[]> findAllByMenuTop();


}
