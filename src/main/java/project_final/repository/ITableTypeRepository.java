package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.TableType;

import java.util.List;

@Repository
public interface ITableTypeRepository extends JpaRepository<TableType,Long> {
    Page<TableType> findAllByNameContains(String name, Pageable pageable);

}
