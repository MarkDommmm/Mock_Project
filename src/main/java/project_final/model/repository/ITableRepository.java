package project_final.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.model.entity.Tables;

import java.awt.print.Pageable;

@Repository
public interface ITableRepository extends JpaRepository<Tables,Long> {
    Page<Tables> findAll(Pageable pageable);
}
