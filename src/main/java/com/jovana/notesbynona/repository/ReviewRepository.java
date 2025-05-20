package com.jovana.notesbynona.repository;

import com.jovana.notesbynona.entity.review.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    Optional<List<Review>> findByPerfumeId(Long perfumeId);

    Optional<Review> findByUserId(Long userId);

    Optional<Review> findByRating(@NotNull @Min(1) @Max(5) Integer rating);

    Page<Review> findAll(Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.perfume.id = :perfumeId")
    Double findAverageRatingByPerfumeId(@Param("perfumeId") Long perfumeId);
}

