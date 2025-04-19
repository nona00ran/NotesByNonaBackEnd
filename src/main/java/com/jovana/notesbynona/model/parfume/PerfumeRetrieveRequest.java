package com.jovana.notesbynona.model.parfume;

import lombok.Data;

import java.util.List;

@Data
public class PerfumeRetrieveRequest {
    private String perfumeName;
    private String genderName;
    private Long minPrice;
    private Long maxPrice;
    private String sortOrder; // "asc" for ascending, "desc" for descending
    private String sortBy;
    private List<String> notes;
}
