package com.jovana.notesbynona.controller;

import com.jovana.notesbynona.entity.review.Review;
import com.jovana.notesbynona.model.review.ReviewCreationRequest;
import com.jovana.notesbynona.model.review.ReviewRetrieveRequest;
import com.jovana.notesbynona.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping ("/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @PostMapping("/createReview")
    public ResponseEntity<Review> createReview(@RequestBody @Valid ReviewCreationRequest reviewCreationRequest) {
        logger.info("Creating review for user ID: {}", reviewCreationRequest.getUserId());
        logger.info("Creating review for pefume ID: {}", reviewCreationRequest.getPerfumeId());
        Review review = reviewService.createReview(reviewCreationRequest);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Review> deleteReview(@PathVariable Long reviewId){
        logger.info("Deleting review with ID: {}", reviewId);
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getReviews")
    public ResponseEntity<Page<Review>> getReviews(ReviewRetrieveRequest reviewRetrieveRequest, Pageable pageable, @RequestParam(value = "commentsFlag", required = false) String commentsFlag) {
        if(commentsFlag != null && commentsFlag.equalsIgnoreCase("true")) {
            reviewRetrieveRequest.setCommentsFlag(true);
        } else {
            reviewRetrieveRequest.setCommentsFlag(false);
        }
        logger.info("Retrieving reviews with request: {}", reviewRetrieveRequest);
        Page<Review> reviews = reviewService.getReviews(reviewRetrieveRequest, pageable);
        return ResponseEntity.ok(reviews);
    }

}

