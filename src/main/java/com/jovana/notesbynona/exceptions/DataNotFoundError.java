package com.jovana.notesbynona.exceptions;

public class DataNotFoundError extends RuntimeException {
    public DataNotFoundError(String message) {
        super(message);
    }
}
