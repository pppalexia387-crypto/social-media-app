package com.alexia.socialmediaapp.domain;

import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant; // Or LocalDateTime, depending on what you used

import static org.junit.jupiter.api.Assertions.*;

class UserAccountValidatorTest {

    private UserAccountValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserAccountValidator();
    }

    @Test
    @DisplayName("Should pass validation when all UserAccount fields are valid")
    void validate_ValidAccount_DoesNotThrow() {
        UserAccount validAccount = new UserAccount("alex.popescu@gmail.com",
                "0755193041",
                "securePass123",false,
                Instant.now()
        );

        assertDoesNotThrow(() -> validator.validate(validAccount));
    }

    @Test
    @DisplayName("Should throw ValidationException when email does not contain @")
    void validate_InvalidEmail_ThrowsException() {
        UserAccount invalidEmailAccount = new UserAccount(
                "alex.popescugmail.com", // Missing @
                "0755193041",
                "securePass123",false,
                Instant.now()
        );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(invalidEmailAccount)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("email"));
    }

    @Test
    @DisplayName("Should throw ValidationException when phone number is too short")
    void validate_InvalidPhone_ThrowsException() {
        UserAccount invalidPhoneAccount = new UserAccount(
                "alex.popescu@gmail.com",
                "123",
                "securePass123",false,
                Instant.now()
        );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(invalidPhoneAccount)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("phone"));
    }

    @Test
    @DisplayName("Should throw ValidationException when password is empty")
    void validate_EmptyPassword_ThrowsException() {
        UserAccount emptyPasswordAccount = new UserAccount(
                "alex.popescu@gmail.com",
                "0755193041",
                "", false,
                Instant.now()
        );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(emptyPasswordAccount)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("password"));
    }
}