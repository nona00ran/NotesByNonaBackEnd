package com.jovana.notesbynona.entity.perfume;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
    @JsonProperty("perfume_gender")
    private PerfumeGender perfumeGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @JsonProperty("perfume_brand")
    private PerfumeBrand perfumeBrand;

    @NotBlank(message = "First name is required")
    @Column(name = "perfume_name", unique = true)
    @JsonProperty("perfume_name")
    private String perfumeName;

    @Column(name = "price")
    private Long price;

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
