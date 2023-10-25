package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.entity.Category;
import project_final.entity.Review;
import project_final.model.dto.request.ReviewRequest;
import project_final.model.dto.response.ReviewResponse;
import project_final.repository.IReviewRepository;
import project_final.service.IUploadService;

import java.util.Date;
import java.util.Optional;
@Component
@AllArgsConstructor
public class ReviewMapper implements IReviewMapper{
    private final IReviewRepository reviewRepository;
    private final IUploadService uploadService;
    @Override
    public Review toEntity(ReviewRequest reviewRequest) {
        // check Review
        Optional<Review> existingReview = reviewRequest.getId() != null ?
                reviewRepository.findById(reviewRequest.getId()) :
                Optional.empty();

        String image;
        if (reviewRequest.getImage() != null && !reviewRequest.getImage().isEmpty()) {
            // nếu có ảnh mới
            image = uploadService.uploadFile(reviewRequest.getImage());
        } else if (existingReview.isPresent()) {
            // nếu review  tồn tại
            image = existingReview.get().getImage();

        } else {
            // không có ảnh và không tồn tại category
            image = null;
        }

        return Review.builder()
                .id(reviewRequest.getId())
                .image(image)
                .comment(reviewRequest.getComment())
                .rating(reviewRequest.getRating())
                .createdDate(new Date())
                .reservation(reviewRequest.getReservation())
                .status(true)
                .build();
    }

    @Override
    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .comment(review.getComment())
                .rating(review.getRating())
                .image(review.getImage())
                .reservation(review.getReservation())
                .status(review.isStatus()).build();
    }
}
