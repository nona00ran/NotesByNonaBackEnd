package com.jovana.notesbynona.model.review;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
public class ReviewRetrieveRequest {
    private Long userId;
    private String username;
    private boolean commentsFlag;
    private Long perfumeId;
    private Integer rating;
    private String sortOrder; // "asc" for ascending, "desc" for descending
    private String sortBy;
}
