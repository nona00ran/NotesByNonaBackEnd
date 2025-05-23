package com.jovana.notesbynona.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreationRequest {
    @NotNull
    @JsonProperty("perfume_id")
    private Long perfumeId;

    private String comment;

    @NotNull
    private Integer rating;
   // private String userName;
}
