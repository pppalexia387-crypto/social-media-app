package com.alexia.socialmediaapp.exceptions;

/**
 * Unchecked exception thrown when a business logic or service layer operation fails.
 */
public class ServiceException extends RuntimeException {

    /**
     * Constructs a new ServiceException with the specified detail message.
     * @param message the detail message explaining the service failure
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServiceException with the specified detail message and cause.
     * @param message the detail message explaining the service failure
     * @param cause the underlying exception that triggered this error
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}