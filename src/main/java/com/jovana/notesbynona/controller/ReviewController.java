package com.jovana.notesbynona.controller;

import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.entity.review.Review;
import com.jovana.notesbynona.model.parfume.PerfumeCreationRequest;
import com.jovana.notesbynona.model.review.ReviewCreationRequest;
import com.jovana.notesbynona.model.review.ReviewRetrieveRequest;
import com.jovana.notesbynona.repository.ReviewRepository;
import com.jovana.notesbynona.service.JwtService;
import com.jovana.notesbynona.service.ReviewService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.tomcat.Jar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewRepository reviewRepository;

    @PostMapping("/createReview")
    public ResponseEntity<Review> createReview(@RequestBody @Valid ReviewCreationRequest reviewCreationRequest,
                                               @RequestHeader("Authorization") String token) {
        Claims claims = jwtService.verifyAndParseToken(token.replace("Bearer ", ""));
        Long userIdFromToken = claims.get("userId", Long.class);

        Review review = reviewService.createReview(reviewCreationRequest, userIdFromToken);
        return ResponseEntity.ok(review);
    }

//    @DeleteMapping("/deleteReview/{reviewId}")
//    public ResponseEntity<Review> deleteReview(@PathVariable Long reviewId){
//        logger.info("Deleting review with ID: {}", reviewId);
//        reviewService.deleteReview(reviewId);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String token) {
        logger.info("Deleting review with ID: {}", reviewId);

        Claims claims = jwtService.verifyAndParseToken(token.replace("Bearer ", ""));
        Integer userIdFromToken = claims.get("userId", Integer.class);
        String userIdAsString = String.valueOf(userIdFromToken);
        List<String> roles = claims.get("roles", List.class);

        if (roles == null) {
            roles = List.of();
        }

        if (roles.contains("ADMIN")) {
            logger.info("Admin user is deleting review ID: {}", reviewId);
            reviewService.deleteReview(reviewId);
            return ResponseEntity.noContent().build();
        }

        Review review = reviewService.getReviewById(reviewId);
        if (!review.getUser().getId().equals(Long.valueOf(userIdAsString))) {
            logger.warn("User ID {} is not authorized to delete review ID {}", userIdAsString, reviewId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        logger.info("User ID {} is deleting their own review ID: {}", userIdAsString, reviewId);
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateReview/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId,
                                               @RequestBody ReviewCreationRequest reviewCreationRequest,
                                               @RequestHeader("Authorization") String token) {
        logger.info("Updating review with ID: {}", reviewId);

        Claims claims = jwtService.verifyAndParseToken(token.replace("Bearer ", ""));
        Integer userIdFromToken = claims.get("userId", Integer.class);
        String userIdAsString = String.valueOf(userIdFromToken);

        Review review = reviewService.getReviewById(reviewId);
        if (!review.getUser().getId().equals(Long.valueOf(userIdAsString))) {
            logger.warn("User ID {} is not authorized to update review ID {}", userIdAsString, reviewId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reviewService.updateReview(reviewId, reviewCreationRequest);
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

