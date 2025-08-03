package com.example.demo.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.admin.exception.ReportAlreadyDeletedException;
import com.example.demo.admin.exception.ReportNotFoundException;
import com.example.demo.admin.exception.SanctionNotFoundException;
import com.example.demo.admin.exception.UnauthorizedReportAccessException;
import com.example.demo.review.exception.ReviewAlreadyExistsException;
import com.example.demo.review.exception.ReviewNotFoundException;
import com.example.demo.review.exception.UnauthorizedReviewAccessException;
import com.example.demo.search.exception.SearchException;


@ControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException → 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ex.getMessage());
    }
    // IllegalStateException → 409 Conflict
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)  // 409
            .body(ex.getMessage());
    }
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<String> handleReportNotFound(ReportNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ReportAlreadyDeletedException.class)
    public ResponseEntity<String> handleReportAlreadyDeleted(ReportAlreadyDeletedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedReportAccessException.class)
    public ResponseEntity<String> handleUnauthorizedReportAccess(UnauthorizedReportAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
    @ExceptionHandler(SanctionNotFoundException.class)
    public ResponseEntity<String> handleSanctionNotFound(SanctionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(SearchException.class)
    public ResponseEntity<Map<String, String>> handleSearchException(SearchException e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(UnauthorizedReviewAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedReviewAccess(UnauthorizedReviewAccessException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReviewNotFound(ReviewNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleReviewAlreadyExists(ReviewAlreadyExistsException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new com.example.demo.global.exception.ErrorResponse("REVIEW_EXISTS", e.getMessage()));
    }
}
