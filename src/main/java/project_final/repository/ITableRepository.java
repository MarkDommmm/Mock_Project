package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import project_final.entity.Tables;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Repository
public interface ITableRepository extends JpaRepository<Tables, Long> {
    Page<Tables> findAllByNameContains(String name, Pageable pageable);
    @Query("SELECT T FROM Tables T WHERE T.status = true AND T.name LIKE %:name%")
    Page<Tables> findAllByStatusIsTrueAndName(@Param("name") String name, Pageable pageable);
    @Query("Select T from Tables T join T.tableType TableType where TableType.name=:name " )
    List<Tables> findAllByTableTypeName(@Param("name")String name);

    @Query("SELECT t FROM Tables t " +
            "WHERE t.id NOT IN " +
            "(SELECT r.table.id FROM Reservation r " +
            "WHERE t.status = true " +
            "AND DATE(r.bookingDate) = :bookingDate " +
            "AND ((TIME(r.startTime) <= :endTime AND TIME(r.endTime) >= :endTime) " +
            "OR (TIME(r.startTime) <= :startTime AND TIME(r.endTime) >= :startTime) " +
            "OR (TIME(r.startTime) >= :startTime AND TIME(r.endTime) <= :endTime)))")
    Page<Tables> findAvailableTables(@Param("bookingDate") Date bookingDate,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     Pageable pageable);

    @Query(value = "SELECT t.name as name, " +
            "CASE " +
            "    WHEN r.id IS NOT NULL AND (" +
            "        (r.start_time IS NULL AND r.end_time IS NULL) " +
            "        OR (r.start_time IS NOT NULL AND r.end_time IS NOT NULL AND TIME(r.end_time) >= :startTime AND TIME(r.start_time) <= :endTime) " +
            "        OR (r.start_time IS NULL AND r.end_time IS NOT NULL AND TIME(r.end_time) >= :startTime AND TIME(r.start_time) <= :endTime) " +
            "        OR (r.start_time IS NOT NULL AND r.end_time IS NULL AND TIME(r.start_time) <= :endTime)" +
            "    ) THEN 'ĐangSD' " +
            "    ELSE 'Trống' " +
            "END as status " +
            "FROM tables t " +
            "LEFT JOIN reservation r ON t.id = r.table_id AND DATE(r.booking_date) = :date",
            countQuery = "SELECT COUNT(*) FROM tables t LEFT JOIN reservation r ON t.id = r.table_id AND DATE(r.booking_date) = :date",
            nativeQuery = true)
    Page<Map<String, Object>> getTableStatusForDate(@Param("date") Date date,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime,
                                                    Pageable pageable);


}
