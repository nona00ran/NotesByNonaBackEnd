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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PerfumeRepository perfumeRepository;

    public void syncAverageRating(Long perfumeId){
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new DataNotFoundError("Perfume not found with id: " + perfumeId));
        perfume.setAverageRating(getAverageRatingForPerfume(perfume.getId()));
        perfumeRepository.save(perfume);
    }
    @Override
    public Review createReview(ReviewCreationRequest reviewCreationRequest, Long userId) {
        Review review = getOrCreateReview(reviewCreationRequest);
        Optional<User> user = userRepository.findById(userId);
        review.setUser(user.get());
        Review newReview = reviewRepository.save(review);
        syncAverageRating(newReview.getPerfume().getId());
        return newReview;
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
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new DataNotFoundError("Review not found"));
        Long perfumeId = review.getPerfume().getId();
        reviewRepository.deleteById(reviewId);
        syncAverageRating(perfumeId);
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
        /*Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundError("Review not found with ID: " + reviewId));
        Perfume perfume = perfumeRepository.findById(reviewCreationRequest.getPerfumeId())
                .orElseThrow(() -> new DataNotFoundError("Perfume not found with id: " + reviewCreationRequest.getPerfumeId()));
        review.setPerfume(perfume);
        review.setComment(reviewCreationRequest.getComment());
        review.setRating(reviewCreationRequest.getRating());
        Review updatedReview = reviewRepository.save(review);
        syncAverageRating(updatedReview.getPerfume().getId());
        return updatedReview;*/
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundError("Review not found with ID: " + reviewId));
        Perfume perfume = review.getPerfume();
        review.setComment(reviewCreationRequest.getComment());
        review.setRating(reviewCreationRequest.getRating());
        Review updatedReview = reviewRepository.save(review);
        syncAverageRating(perfume.getId());

        return updatedReview;
    }

    private Review getOrCreateReview(ReviewCreationRequest reviewCreationRequest) {
        Perfume perfume = perfumeRepository.findById(reviewCreationRequest.getPerfumeId())
                .orElseThrow(() -> new RuntimeException("Perfume not found"+ reviewCreationRequest.getPerfumeId()));
        Review review = new Review();
        review.setPerfume(perfume);
        review.setComment(reviewCreationRequest.getComment());
        review.setRating(reviewCreationRequest.getRating());
        return review;
    }

    @Override
    public BigDecimal getAverageRatingForPerfume(Long perfumeId) {
        return BigDecimal.valueOf(reviewRepository.findAverageRatingByPerfumeId(perfumeId));
    }
}