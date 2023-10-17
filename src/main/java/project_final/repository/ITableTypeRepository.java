package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.TableType;
import project_final.entity.Tables;

import java.util.List;

@Repository
public interface ITableTypeRepository extends JpaRepository<TableType,Long> {
    Page<TableType> findAllByNameContains(String name, Pageable pageable);
    @Query("SELECT t FROM TableType t WHERE t.status = true AND t.name LIKE %:name%")
    Page<TableType> findAllByStatusIsTrueAndName(@Param("name") String name, Pageable pageable);
    @Query("SELECT t FROM Tables t WHERE t.status = true")
    List<TableType> findAllByStatusIsTrue();

}
