package com.alexia.socialmediaapp.domain;

import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileValidatorTest {

    private UserProfileValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserProfileValidator();
    }

    @Test
    @DisplayName("Should pass validation when name and username are correct")
    void validate_ValidUserProfile_DoesNotThrow() {
        UserProfile validProfile = new UserProfile("Alex Popescu", "alex.pop");

        assertDoesNotThrow(() -> validator.validate(validProfile));
    }

    @Test
    @DisplayName("Should throw ValidationException when name is empty")
    void validate_EmptyName_ThrowsException() {
        UserProfile profileWithEmptyName = new UserProfile("", "maria.io");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(profileWithEmptyName)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("name"));
    }

    @Test
    @DisplayName("Should throw ValidationException when username is null")
    void validate_NullUsername_ThrowsException() {
        UserProfile profileWithNullUsername = new UserProfile("Andrei Radu", null);

        assertThrows(
                ValidationException.class,
                () -> validator.validate(profileWithNullUsername)
        );
    }
}