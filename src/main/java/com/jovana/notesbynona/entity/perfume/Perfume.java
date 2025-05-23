package com.jovana.notesbynona.entity.perfume;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;


@Data
@Entity
@Table(name = "perfumes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Perfume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id", nullable = false)
    @NotNull(message = "Gender is a required field.")
    @JsonProperty("perfume_gender")
    private PerfumeGender perfumeGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @NotNull(message = "Brand is a required field.")
    @JsonProperty("perfume_brand")
    private PerfumeBrand perfumeBrand;

    @NotBlank(message = "First name is a required field.")
    @Column(name = "perfume_name", unique = true)
    @JsonProperty("perfume_name")
    private String perfumeName;

    @NotNull(message = "Price is a required field.")
    @Column(name = "price")
    private Long price;

    @Column(name = "average_rating", precision = 3, scale = 2)
    @JsonProperty("average_rating")
    private BigDecimal averageRating;

    @ManyToMany
    @JoinTable(
            name = "top_notes",
            joinColumns = @JoinColumn(name = "perfume_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private Set<PerfumeNote> topNotes;

    @ManyToMany
    @JoinTable(
            name = "middle_notes",
            joinColumns = @JoinColumn(name = "perfume_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private Set<PerfumeNote> middleNotes;

    @ManyToMany
    @JoinTable(
            name = "base_notes",
            joinColumns = @JoinColumn(name = "perfume_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private Set<PerfumeNote> baseNotes;

}
