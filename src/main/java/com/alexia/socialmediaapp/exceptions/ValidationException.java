package com.alexia.socialmediaapp.exceptions;

/**
 * Unchecked exception thrown when an entity fails business validation rules.
 */
public class ValidationException extends RuntimeException {
    /**
     * Constructs a new ValidationException with the specified detail message.
     * @param message the detail message detailing the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     * @param message the detail message detailing the validation failure
     * @param cause the underlying exception that triggered this validation error
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}