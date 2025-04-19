package com.jovana.notesbynona.model.error;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class APIErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private List<ValidationError> validationErrors;
}
