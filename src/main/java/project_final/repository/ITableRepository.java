package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import project_final.entity.Menu;
import project_final.entity.Tables;
import project_final.model.dto.response.TableResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@Repository
public interface ITableRepository extends JpaRepository<Tables, Long> {
    Page<Tables> findAllByNameContains(String name, Pageable pageable);
    @Query("SELECT t FROM Tables t WHERE t.status = true AND t.name LIKE %:name%")
    Page<Tables> findAllByStatusIsTrueAndName(@Param("name") String name, Pageable pageable);
    @Query("Select T from Tables T join T.tableType TableType where TableType.name=:name " )
    List<Tables> findAllByTableTypeName(@Param("name")String name);
    @Query("SELECT t FROM Tables t WHERE t.id NOT IN " +
            "(SELECT r.table.id FROM Reservation r WHERE DATE(r.bookingDate) = :bookingDate " +
            "AND NOT (TIME(r.endTime) <= :endTime AND TIME(r.startTime) >= :startTime))")
    List<Tables> findAvailableTables(@Param("bookingDate") LocalDate bookingDate,
                                     @Param("startTime") LocalTime startTime,
                                     @Param("endTime") LocalTime endTime);

}
