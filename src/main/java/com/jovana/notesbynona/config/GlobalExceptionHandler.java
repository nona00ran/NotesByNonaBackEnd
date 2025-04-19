package com.jovana.notesbynona.config;
import com.jovana.notesbynona.exceptions.EmailAlreadyExistsException;
import com.jovana.notesbynona.exceptions.PerfumeAlreadyExistsException;
import com.jovana.notesbynona.exceptions.UsernameAlreadyExistsException;
import com.jovana.notesbynona.model.error.APIErrorResponse;
import com.jovana.notesbynona.model.error.ValidationError;
import io.jsonwebtoken.JwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<APIErrorResponse> handleDataIntegrityViolationException(UsernameAlreadyExistsException ex) {
       List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("username", "Username already exists"));

        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.CONFLICT.value());
        apiErrorResponse.setError("Conflict");
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setTimestamp(LocalDateTime.now());
        apiErrorResponse.setValidationErrors(validationErrors);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<APIErrorResponse> handleDataIntegrityViolationException(EmailAlreadyExistsException ex) {
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("email", "Email already exists"));

        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.CONFLICT.value());
        apiErrorResponse.setError("Conflict");
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setTimestamp(LocalDateTime.now());
        apiErrorResponse.setValidationErrors(validationErrors);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PerfumeAlreadyExistsException.class)
    public ResponseEntity<APIErrorResponse> handleDataIntegrityViolationException(PerfumeAlreadyExistsException ex) {
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("perfume_name", "Perfume already exists"));

        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.CONFLICT.value());
        apiErrorResponse.setError("Conflict");
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setTimestamp(LocalDateTime.now());
        apiErrorResponse.setValidationErrors(validationErrors);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        apiErrorResponse.setError("Bad Request");
        apiErrorResponse.setMessage("Validation failed");
        apiErrorResponse.setTimestamp(LocalDateTime.now());
        apiErrorResponse.setValidationErrors(validationErrors);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIErrorResponse> handleInvalidLoginException(BadCredentialsException ex) {
        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiErrorResponse.setError("Unauthorized");
        apiErrorResponse.setMessage("Invalid username or password");
        apiErrorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleUserNotFoundException(UsernameNotFoundException ex) {
        return genericNotFoundRuntimeException(ex.getMessage(), ex);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<APIErrorResponse> handleSignatureException(JwtException ex) {
        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiErrorResponse.setError("Unauthorized");
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
    }


    private ResponseEntity<APIErrorResponse> genericNotFoundRuntimeException(String message, RuntimeException ex) {
        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        apiErrorResponse.setError("Not Found");
        apiErrorResponse.setMessage(message);
        apiErrorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        apiErrorResponse.setError("Bad Request");
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<APIErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        APIErrorResponse apiErrorResponse = new APIErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
        apiErrorResponse.setError("Payload Too Large");
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }

}
