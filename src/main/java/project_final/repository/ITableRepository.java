package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import project_final.entity.Tables;
import project_final.model.dto.response.TableResponse;

import java.util.List;


@Repository
public interface ITableRepository extends JpaRepository<Tables, Long> {
    Page<Tables> findAllByNameContains(String name, Pageable pageable);
    @Query("Select T from Tables T join T.tableType TableType where TableType.name=:name " )
    List<Tables> findAllByTableTypeName(@Param("name")String name);
}
