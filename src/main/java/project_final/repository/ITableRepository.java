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



    @Query("SELECT DISTINCT t FROM Tables t " +
            "JOIN t.tableType tt " +
            "LEFT JOIN Reservation r ON t.id = r.table.id " +
            "WHERE tt.name = :name " +
            "AND (r.id IS NULL OR " +
            "    NOT (DATE(r.bookingDate) = :date AND (" +
            "        (r.startTime IS NOT NULL AND r.endTime IS NOT NULL AND " +
            "           (TIME(r.endTime) >= :start AND TIME(r.startTime) <= :end)) OR " +
            "        (r.startTime IS NULL AND r.endTime IS NOT NULL AND TIME(r.endTime) > :start) OR " +
            "        (r.startTime IS NOT NULL AND r.endTime IS NULL AND TIME(r.startTime) < :end)" +
            "    ))" +
            ")")
    Page<Tables> findAvailableTables(@Param("name") String name,
                                     @Param("date") Date date,
                                     @Param("start") String start,
                                     @Param("end") String end,
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



    @Query("SELECT COUNT(t) > 0 FROM Reservation r JOIN Tables t ON r.table = t " +
            "WHERE t.id = :tableId " +
            "AND DATE(r.bookingDate) = :bookingDate " +
            "AND ((TIME(r.startTime) <= :endTime AND TIME(r.endTime) >= :startTime) " +
            "OR (TIME(r.startTime) >= :startTime AND TIME(r.endTime) <= :endTime))")
    boolean isTableAvailable(@Param("tableId") Long tableId,
                             @Param("bookingDate") Date bookingDate,
                             @Param("startTime") String startTime,
                             @Param("endTime") String endTime);
}
