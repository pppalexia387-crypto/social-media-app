package com.alexia.socialmediaapp.domain.validators;


import com.alexia.socialmediaapp.exceptions.ValidationException;

/**
 * Strategy interface for validating domain items
 *
 * @param <T> the type of the entity to validate
 */

@FunctionalInterface
public interface Validator<T> {
    default Validator<T> andThen(Validator<T> other) {
        if(other==null)
            throw new IllegalArgumentException("Validator cannot be null!");
        return entity -> {
            this.validate(entity);
            other.validate(entity);
        };
    }

    void validate(T entity) throws ValidationException;
}
