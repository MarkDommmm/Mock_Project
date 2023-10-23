package project_final.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.Category;
import project_final.entity.Menu;

import java.util.List;

@Repository
public interface IMenuRepository extends JpaRepository<Menu,Long> {
    Page<Menu> findAllByNameContains(String name, Pageable pageable);
    @Query("SELECT m FROM Menu m WHERE m.status = true AND m.name LIKE %:name%")
    Page<Menu> findAllByStatusIsTrueAndName(@Param("name") String name, Pageable pageable);
    @Query(value = "SELECT m.* " +
            "FROM menu m " +
            "JOIN reservation_menu r ON m.id = r.menu_id " +
            "GROUP BY m.id " +
            "ORDER BY SUM(r.quantity) DESC " +
            "LIMIT 6", nativeQuery = true)
    List<Menu> findTopSellingMenus();
    @Query("SELECT M FROM Menu M JOIN M.category C WHERE C.name =:category")
    Page<Menu> findAllByCategoryName(@Param("category") String category, Pageable pageable);


    @Query("SELECT M FROM Menu M JOIN M.category C WHERE C.name =:category")
    List<Menu> findAllByCategoryName(@Param("category") String category);
}
