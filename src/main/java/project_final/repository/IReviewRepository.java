package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.Review;
import project_final.entity.User;

@Repository
public interface IReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAll(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.status = true ORDER BY r.createdDate DESC")
    Page<Review> findAllByStatus(Pageable pageable);
@Query("SELECT r FROM Review r join Reservation re ON r.reservation = re WHERE re.user.id =:id")
    Page<Review> findAllByUser(@Param("id") Long id,Pageable pageable);

//    @Query("SELECT count(r.id) FROM Review r join User u on r.user= u join Reservation re on re.user=u" +
//            " where u.id =: id")
//    boolean existsByUserAndReservation(@Param("id") Long id);
@Query("SELECT count(r.id) > 0 FROM Review r JOIN r.reservation re WHERE re.id = :id")
boolean existsByReservation(@Param("id") Long id);

}
