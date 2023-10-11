package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.TableType;
import project_final.entity.Tables;

@Repository
public interface ITableRepository extends JpaRepository<Tables,Long> {
    Page<Tables> findAllByNameContains(String name, Pageable pageable);
}
