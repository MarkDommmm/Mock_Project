package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project_final.entity.Review;
import project_final.entity.User;

@Repository
public interface IReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAll(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.status = true ORDER BY r.createdDate DESC")
    Page<Review> findAllByStatus(Pageable pageable);

    Page<Review> findAllByUser(User user,Pageable pageable);

}
