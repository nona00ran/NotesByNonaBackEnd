package com.jovana.notesbynona.model.parfume;

import lombok.Data;

import java.util.List;

@Data
public class PerfumeRetrieveRequest {
    private String perfumeName;
    private List<String> genderName;
    private List<String> brandName;
    private Long minPrice;
    private Long maxPrice;
    private String sortOrder;
    private String sortBy;
    private List<String> notes;
}
