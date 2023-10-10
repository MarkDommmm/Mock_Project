package project_final.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.model.entity.TableType;

@Repository
public interface ITableTypeRepository extends JpaRepository<TableType,Long> {
    Page<TableType> findAllByNameContains(String name, Pageable pageable);
}
