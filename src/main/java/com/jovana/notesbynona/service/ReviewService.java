package com.jovana.notesbynona.service;

import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.entity.review.Review;
import com.jovana.notesbynona.model.parfume.PerfumeCreationRequest;
import com.jovana.notesbynona.model.review.ReviewCreationRequest;
import com.jovana.notesbynona.model.review.ReviewRetrieveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ReviewService {

    Review createReview(ReviewCreationRequest reviewCreationRequest);

    Review getReviewById(Long reviewId);

    void deleteReview(Long reviewId);

    Page<Review> getReviews(ReviewRetrieveRequest reviewRetrieveRequest, Pageable pageable);

    Review getReviewByRatingAndPerfumeId(Integer rating, Long perfumeId);

    Review updateReview(Long reviewId, ReviewCreationRequest reviewCreationRequest);

    Double getAverageRatingForPerfume(Long perfumeId);
}
