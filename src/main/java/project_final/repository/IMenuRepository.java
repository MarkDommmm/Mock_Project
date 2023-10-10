package project_final.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.Menu;

@Repository
public interface IMenuRepository extends JpaRepository<Menu,Long> {
    Page<Menu> findAllByNameContains(String name, Pageable pageable);
}
