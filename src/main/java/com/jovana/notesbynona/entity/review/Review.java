package com.jovana.notesbynona.entity.review;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jovana.notesbynona.entity.User;
import com.jovana.notesbynona.entity.perfume.Perfume;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfume_id", nullable = false)
    private Perfume perfume;

    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment")
    private String comment;


    // I want to make is so user has to set one of these two but not both, will have to use custom validation
}
