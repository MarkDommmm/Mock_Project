package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.TableMenu;
import project_final.entity.User;
import project_final.model.domain.Status;

import java.util.List;


@Repository
public interface ITableMenuRepository extends JpaRepository<TableMenu,Long> {
    @Query("SELECT T FROM TableMenu T JOIN T.menu M WHERE M.name LIKE %:name%")
    Page<TableMenu> findAllByMenuName(@Param("name") String name, Pageable pageable);

    @Query("SELECT T FROM TableMenu T WHERE T.reservation.user.id = :id AND T.reservation.status = 'PENDING'")
    Page<TableMenu> findAllByUser(@Param("id") Long id, Pageable pageable);

    @Query("SELECT T FROM TableMenu T WHERE T.reservation.id = :id")
    List<TableMenu> findAllByReservation(@Param("id") Long id);

}
