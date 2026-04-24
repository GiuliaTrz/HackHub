package com.project.hackhub.handler;

import com.project.hackhub.exceptions.MultipleWinnersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles the case where multiple teams are tied for the highest grade in a hackathon,
     * and a judge attempts to trigger the automatic winner selection process.
     * @param ex the exception to be handled
     * @return a response entity with a conflict status and
     * a message listing the tied teams, prompting the judge to use the manual selection endpoint.
     * @author Chiara Marinucci
     */
    @ExceptionHandler(MultipleWinnersException.class)
    public ResponseEntity<String> handleConflict(MultipleWinnersException ex) {
        String names = ex.getTiedTeams().stream()
                .map(t -> t.getName() + " (Team ID: " + t.getId() + ")")
                .collect(java.util.stream.Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Multiple winners tied: " + names + ". Please use the manual endpoint.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
