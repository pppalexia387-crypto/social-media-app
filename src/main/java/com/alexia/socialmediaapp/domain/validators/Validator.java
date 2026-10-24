package com.alexia.socialmediaapp.domain.validators;


import com.alexia.socialmediaapp.exceptions.ValidationException;

/**
 * Strategy interface for validating domain items
 *
 * @param <T> the type of the entity to validate
 */
public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
