package com.example.demo.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.admin.exception.ReportAlreadyDeletedException;
import com.example.demo.admin.exception.ReportNotFoundException;
import com.example.demo.admin.exception.SanctionNotFoundException;
import com.example.demo.admin.exception.UnauthorizedReportAccessException;

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
}
