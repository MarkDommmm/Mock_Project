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
import java.util.Map;

@Repository
public interface IMenuRepository extends JpaRepository<Menu,Long> {
    Page<Menu> findAllByNameContains(String name, Pageable pageable);
    @Query("SELECT m FROM Menu m WHERE m.status = true AND m.name LIKE %:name% ORDER BY DATE(m.createDate) DESC")
    Page<Menu> findAllByStatusIsTrueAndName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT m.* " +
            "FROM menu m " +
            "JOIN reservation_menu r ON m.id = r.menu_id " +
            "GROUP BY m.id " +
            "ORDER BY SUM(r.quantity_ordered) DESC " +
            "LIMIT 6", nativeQuery = true)
    List<Menu> findTopSellingMenus();

    @Query(nativeQuery = true, value =
            "WITH MonthlyData AS (" +
                    "    SELECT " +
                    "        menu.name as name, " +
                    "        SUM(rm.quantity_ordered) AS totalQuantity, " +
                    "        MONTH(r.booking_date) AS Month, " +
                    "        ROW_NUMBER() OVER (PARTITION BY MONTH(r.booking_date) ORDER BY SUM(rm.quantity_ordered) DESC) AS RowNum " +
                    "    FROM " +
                    "        reservation_menu AS rm " +
                    "    JOIN " +
                    "        reservation AS r ON r.id = rm.reservation_id " +
                    "    JOIN " +
                    "        menu ON menu.id = rm.menu_id " +
                    "    GROUP BY " +
                    "        menu.name, MONTH(r.booking_date) " +
                    ") " +
                    "SELECT " +
                    "    name, " +
                    "    totalQuantity, " +
                    "    Month " +
                    "FROM " +
                    "    MonthlyData " +
                    "WHERE " +
                    "    RowNum <= 5 " +
                    "ORDER BY " +
                    "    Month, totalQuantity DESC")
    List<Map<String, Object>> findTopMenusByMonth();

    @Query("SELECT M FROM Menu M JOIN M.category C WHERE C.name =:category")
    Page<Menu> findAllByCategoryName(@Param("category") String category, Pageable pageable);


    @Query("SELECT M FROM Menu M JOIN M.category C WHERE C.name =:category")
    List<Menu> findAllByCategoryName(@Param("category") String category);
}
