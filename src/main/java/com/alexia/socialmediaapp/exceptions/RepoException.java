package com.alexia.socialmediaapp.exceptions;

/**
 * Unchecked exception thrown when a repository persistence operation fails.
 */
public class RepoException extends RuntimeException {
    /**
     * Constructs a new RepoException with the specified detail message.
     * @param message the detail message explaining the repository failure
     */
    public RepoException(String message) {
        super(message);
    }

    /**
     * Constructs a new RepoException with the specified detail message and cause.
     * @param message the detail message explaining the repository failure
     * @param cause the underlying exception that triggered this error
     */
    public RepoException(String message, Throwable cause) {
        super(message, cause);
    }
}
