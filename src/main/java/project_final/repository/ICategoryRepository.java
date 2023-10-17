package project_final.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.Category;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {
    Page<Category> findAllByNameContains(String name, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.status = true")
    List<Category> findAllByStatusIsTrue();


}
