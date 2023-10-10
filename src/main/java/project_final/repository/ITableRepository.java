package project_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.Tables;

@Repository
public interface ITableRepository extends JpaRepository<Tables,Long> {

}
