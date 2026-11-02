package com.alexia.socialmediaapp.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Utility class providing null-safe {@link Predicate}s for filtering or matching
 * {@link UserAccount} instances based on specific criteria.
 * <p>
 * These predicates can be used in streams or collections to safely find or filter
 * user accounts by their attributes without risking NullPointerExceptions.
 */
public final class UserAccountCriteria {

    // Private constructor to prevent instantiation of utility class
    private UserAccountCriteria() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Returns a predicate that tests whether a user account has the specified email address.
     *
     * @param email the email address to match against
     * @return a predicate evaluating to {@code true} if the user's email matches safely
     */
    public static Predicate<UserAccount> hasEmail(String email) {
        return user -> user != null && Objects.equals(user.getEmail(), email);
    }

    /**
     * Returns a predicate that tests whether a user account has the specified phone number.
     *
     * @param phone the phone number to match against
     * @return a predicate evaluating to {@code true} if the user's phone matches safely
     */
    public static Predicate<UserAccount> hasPhone(String phone) {
        return user -> user != null && Objects.equals(user.getPhoneNr(), phone);
    }

    /**
     * Returns a predicate that tests whether a user account has the specified password.
     *
     * @param password the password to match against
     * @return a predicate evaluating to {@code true} if the user's password matches safely
     */
    public static Predicate<UserAccount> hasPassword(String password) {
        return user -> user != null && Objects.equals(user.getPassword(), password);
    }

    /**
     * Returns a predicate that tests whether a user account was created at the exact specified timestamp.
     *
     * @param createdAt the creation timestamp to match against
     * @return a predicate evaluating to {@code true} if the user's creation date matches safely
     */
    public static Predicate<UserAccount> hasDate(Instant createdAt) {
        return user -> user != null && Objects.equals(user.getCreatedAt(), createdAt);
    }
}