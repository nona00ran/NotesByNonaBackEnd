package com.jovana.notesbynona.model.parfume;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jovana.notesbynona.entity.perfume.PerfumeNote;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class PerfumeCreationRequest {

    @NotNull
    @JsonProperty("perfume_name")
    private String perfumeName;

    @NotNull
    @JsonProperty("gender_name")
    private String genderName;

    @NotNull
    @JsonProperty("brand_name")
    private String brandName;

    @NotNull
    private Long price;

    @JsonProperty("base_notes")
    private Set<PerfumeNote> baseNotes;

    @JsonProperty("middle_notes")
    private Set<PerfumeNote> middleNotes;

    @JsonProperty("top_notes")
    private Set<PerfumeNote> topNotes;

    @JsonProperty("perfume_image")
    private byte[] perfumeImage;

}
