package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.Reservation;
import project_final.entity.Review;
import project_final.entity.User;
import project_final.repository.IReservationRepository;
import project_final.repository.IReviewRepository;
import project_final.service.IReviewService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService<Review, Long> {
    private final IReviewRepository reviewRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public Page<Review> findAll(int page, int size) {
        return reviewRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Review findById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            return review.get();
        }
        return null;
    }

    @Override
    public void save(Review review, User user) {
        Reservation reservation = reservationRepository.findByUser(user);
        if (reservation != null && reservation.getStatus().equals("COMPLETED")) {
                review.setStatus(true);
                reviewRepository.save(review);
        }
    }

    @Override
    public void delete(Long id, User user) {
       Review review = findById(id);
       if (user.equals(review.getUser())) {
           reviewRepository.deleteById(id);
       }
    }

    @Override
    public void changeStatus(Long id) {
        Review review = findById(id);
        if (review!=null) {
            review.setStatus(!review.isStatus());
            reviewRepository.save(review);
        }
    }


}
