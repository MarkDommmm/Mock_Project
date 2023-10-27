package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import project_final.entity.Reservation;
import project_final.entity.Review;
import project_final.entity.User;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReviewRequest;
import project_final.model.dto.response.ReviewResponse;
import project_final.repository.IReservationRepository;
import project_final.repository.IReviewRepository;
import project_final.repository.IUserRepository;
import project_final.service.IReviewService;
import project_final.service.mapper.IReviewMapper;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService<ReviewRequest, ReviewResponse, Long> {
    private final IReviewRepository reviewRepository;
    private final IReservationRepository reservationRepository;
    private final IReviewMapper reviewMapper;
    private final IUserRepository userRepository;

    @Override
    public Page<ReviewResponse> findAll(int page, int size) {
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(page, size));
        return reviews.map(reviewMapper::toResponse);
    }

    @Override
    public Page<ReviewResponse> findAllByStatus(int page, int size) {
        Page<Review> reviews = reviewRepository.findAllByStatus(PageRequest.of(page, size));
        return reviews.map(reviewMapper::toResponse);
    }

    @Override
    public Page<ReviewResponse> findAllByUser(Long id, int page, int size) {
        User user = userRepository.findById(id).get();
        Page<Review> reviews = reviewRepository.findAllByUser(user.getId(), PageRequest.of(page, size));
        return reviews.map(reviewMapper::toResponse);
    }

    @Override
    public ReviewResponse findById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            return reviewMapper.toResponse(review.get());
        }
        return null;
    }

    @Override
    public String save(ReviewRequest reviewRequest, Long id) {

        if (!reviewRepository.existsByReservation(reviewRequest.getReservation().getId()) && reviewRequest.getReservation().getUser().getId().equals(id)
                && reviewRequest.getReservation().getStatus().equals(Status.COMPLETED)) {
            reviewRepository.save(reviewMapper.toEntity(reviewRequest));
            return "success";
        }
        return "error";
    }


    @Override
    public void delete(Long id, User user) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            if (user.getId().equals(review.get().getReservation().getUser().getId())) {
                reviewRepository.deleteById(id);
            }
        }
    }

    @Override
    public void changeStatus(Long id) {
        Review review = reviewRepository.findById(id).get();
        if (review != null) {
            review.setStatus(!review.isStatus());
            reviewRepository.save(review);
        }
    }


}
