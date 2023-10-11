package project_final.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.Menu;

import java.util.List;

@Repository
public interface IMenuRepository extends JpaRepository<Menu,Long> {
    Page<Menu> findAllByNameContains(String name, Pageable pageable);
    @Query(value = "SELECT m.* " +
            "FROM menu m " +
            "JOIN table_menu t ON m.id = t.menu_id " +
            "GROUP BY m.id " +
            "ORDER BY SUM(t.quantity) DESC " +
            "LIMIT 6", nativeQuery = true)
    List<Menu> findTopSellingMenus();
    @Query("SELECT M FROM Menu M JOIN M.category C WHERE C.name =:category")
    Page<Menu> findAllByCategoryName(@Param("category") String category, Pageable pageable);
}
