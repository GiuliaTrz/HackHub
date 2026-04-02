package com.project.hackhub.exceptions;

/**
 * Custom Exception to handle the anavailability of a user in a specific reservation
 *
 * @author Giorgia Branchesi
 */
public class UserNotAvailableException extends RuntimeException {

    public UserNotAvailableException(String message) {
        super(message);
    }
}