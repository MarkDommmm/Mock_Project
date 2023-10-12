package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.Review;
@Repository
public interface IReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAll(Pageable pageable);
}
