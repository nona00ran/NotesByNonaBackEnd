package com.jovana.notesbynona.entity.perfume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "perfume_genders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PerfumeGender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gender_id")
    private Long id;

    @NotBlank(message = "Gender is a required field")
    @Column(name = "gender_name", nullable = false, unique = true)
    @JsonProperty("gender_name")
    private String genderName;

    public PerfumeGender(String genderName) {
        this.genderName = genderName;
    }
}
