package com.jovana.notesbynona.service.impl;

import com.jovana.notesbynona.entity.User;
import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.entity.perfume.PerfumeBrand;
import com.jovana.notesbynona.entity.perfume.PerfumeGender;
import com.jovana.notesbynona.entity.review.Review;
import com.jovana.notesbynona.exceptions.DataNotFoundError;
import com.jovana.notesbynona.exceptions.PerfumeAlreadyExistsException;
import com.jovana.notesbynona.model.enums.EnumUtils;
import com.jovana.notesbynona.model.enums.SortBy;
import com.jovana.notesbynona.model.enums.SortOrder;
import com.jovana.notesbynona.model.review.ReviewCreationRequest;
import com.jovana.notesbynona.model.review.ReviewRetrieveRequest;
import com.jovana.notesbynona.repository.ReviewRepository;
import com.jovana.notesbynona.repository.UserRepository;
import com.jovana.notesbynona.repository.perfume.PerfumeRepository;
import com.jovana.notesbynona.service.ReviewService;
import com.jovana.notesbynona.validation.ReviewSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PerfumeRepository perfumeRepository;
    @Override
    public Review createReview(ReviewCreationRequest reviewCreationRequest) {
        Review review = getOrCreateReview(reviewCreationRequest);
        return reviewRepository.save(review);
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new DataNotFoundError("Review not found"));
    }

    @Override
    public void deleteReview(Long reviewId) {
        if(!reviewRepository.existsById(reviewId)){
            throw new DataNotFoundError("Review not found with ID:" + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public Page<Review> getReviews(ReviewRetrieveRequest reviewRetrieveRequest, Pageable pageable) {
        if(reviewRetrieveRequest.getSortOrder() != null && reviewRetrieveRequest.getSortBy() == null) {
            throw new IllegalArgumentException("Sort by must be specified if sort order is specified.");
        } else if (reviewRetrieveRequest.getSortBy()!=null && !EnumUtils.isInEnum(reviewRetrieveRequest.getSortBy(), SortBy.class)){
            throw new IllegalArgumentException("Invalid sort by: " + reviewRetrieveRequest.getSortBy());
        }
        Specification<Review> spec = Specification.where(null);
        if(reviewRetrieveRequest.isCommentsFlag()){
            spec = spec.and(ReviewSpecification.hasComments());
        }
        if (reviewRetrieveRequest.getRating() != null) {
            spec = spec.and(ReviewSpecification.hasRating(reviewRetrieveRequest.getRating()));
        }
        if(reviewRetrieveRequest.getSortBy() != null){
            boolean ascending = reviewRetrieveRequest.getSortOrder() == null || reviewRetrieveRequest.getSortOrder().equalsIgnoreCase(SortOrder.ASC.name());
            spec = spec.and(ReviewSpecification.sortBy(reviewRetrieveRequest.getSortBy(), ascending));
        }
        if(reviewRetrieveRequest.getPerfumeId() != null){
            spec = spec.and(ReviewSpecification.hasPerfumeId(reviewRetrieveRequest.getPerfumeId()));
        }
        return reviewRepository.findAll(spec, pageable);
    }


    @Override
    public Review getReviewByRatingAndPerfumeId(Integer rating, Long perfumeId) {
        return null;
    }

    @Override
    public Review updateReview(Long reviewId, ReviewCreationRequest reviewCreationRequest) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundError("Review not found with ID: " + reviewId));

        User user = userRepository.findById(reviewCreationRequest.getUserId())
                .orElseThrow(() -> new DataNotFoundError("User not found with id: " + reviewCreationRequest.getUserId()));
        Perfume perfume = perfumeRepository.findById(reviewCreationRequest.getPerfumeId())
                .orElseThrow(() -> new DataNotFoundError("Perfume not found with id: " + reviewCreationRequest.getPerfumeId()));
        review.setPerfume(perfume);
        review.setUser(user);
        review.setComment(reviewCreationRequest.getComment());
        review.setRating(reviewCreationRequest.getRating());

        return reviewRepository.save(review);
    }

    private Review getOrCreateReview(ReviewCreationRequest reviewCreationRequest) {
        User user = userRepository.findById(reviewCreationRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"+ reviewCreationRequest.getUserId()));
        Perfume perfume = perfumeRepository.findById(reviewCreationRequest.getPerfumeId())
                .orElseThrow(() -> new RuntimeException("Perfume not found"+ reviewCreationRequest.getPerfumeId()));
        Review review = new Review();
        review.setUser(user);
        review.setPerfume(perfume);
        review.setComment(reviewCreationRequest.getComment());
        review.setRating(reviewCreationRequest.getRating());
        return review;
    }

    @Override
    public Double getAverageRatingForPerfume(Long perfumeId) {
        return reviewRepository.findAverageRatingByPerfumeId(perfumeId);
    }
}
