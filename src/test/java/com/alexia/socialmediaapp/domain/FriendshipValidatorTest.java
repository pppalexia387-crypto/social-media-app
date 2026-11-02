package com.alexia.socialmediaapp.domain;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipValidatorTest {

    private FriendshipValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FriendshipValidator();
    }

    @Test
    @DisplayName("Should pass validation when friendship data is correct")
    void validate_ValidFriendship_DoesNotThrow() {
        Friendship validFriendship = new Friendship(1, 2, Instant.now());

        assertDoesNotThrow(() -> validator.validate(validFriendship));
    }

    @Test
    @DisplayName("Should throw ValidationException when a user tries to be friends with themselves")
    void validate_SameUserIds_ThrowsException() {
        Friendship selfFriendship = new Friendship(1, 1, Instant.now());

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(selfFriendship)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("same") ||
                exception.getMessage().toLowerCase().contains("themselves"));
    }

    @Test
    @DisplayName("Should throw ValidationException when timestamp is null")
    void validate_NullTimestamp_ThrowsException() {
        Friendship nullTimeFriendship = new Friendship(3, 4, null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(nullTimeFriendship)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("date") ||
                exception.getMessage().toLowerCase().contains("time"));
    }
}