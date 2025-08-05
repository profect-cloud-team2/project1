package com.example.demo.global.exception;

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
import com.example.demo.store.exception.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("INVALID_ARGUMENT", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("ILLEGAL_STATE", ex.getMessage()));
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReportNotFound(ReportNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("REPORT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(ReportAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleReportAlreadyDeleted(ReportAlreadyDeletedException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("REPORT_ALREADY_DELETED", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedReportAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedReportAccess(UnauthorizedReportAccessException ex) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse("REPORT_UNAUTHORIZED", ex.getMessage()));
    }

    @ExceptionHandler(SanctionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSanctionNotFound(SanctionNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("SANCTION_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(SearchException.class)
    public ResponseEntity<ErrorResponse> handleSearchException(SearchException e) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse("SEARCH_ERROR", e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedReviewAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedReviewAccess(UnauthorizedReviewAccessException ex) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse("REVIEW_UNAUTHORIZED", ex.getMessage()));
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotFound(ReviewNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("REVIEW_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleReviewAlreadyExists(ReviewAlreadyExistsException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("REVIEW_EXISTS", e.getMessage()));
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStoreNotFound(StoreNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("STORE_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(StoreAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleStoreAlreadyExists(StoreAlreadyExistsException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("STORE_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedStoreAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedStoreAccess(UnauthorizedStoreAccessException ex) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse("STORE_UNAUTHORIZED", ex.getMessage()));
    }

    @ExceptionHandler(StoreClosureStateException.class)
    public ResponseEntity<ErrorResponse> handleStoreClosureState(StoreClosureStateException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("STORE_CLOSURE_STATE_INVALID", ex.getMessage()));
    }

    @ExceptionHandler(StoreForceDeleteException.class)
    public ResponseEntity<ErrorResponse> handleStoreForceDelete(StoreForceDeleteException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("STORE_FORCE_DELETE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(InvalidStoreStatusException.class)
    public ResponseEntity<?> handleInvalidStoreStatus(InvalidStoreStatusException ex) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "INVALID_STORE_STATUS",
            "message", ex.getMessage()
        ));
    }
}
