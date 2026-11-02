package com.alexia.socialmediaapp.domain;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Utility class providing null-safe {@link Predicate}s for filtering or matching
 * {@link UserProfile} instances based on specific criteria.
 * <p>
 * These predicates are useful for searching or filtering user profiles
 * safely within streams or collections.
 */
public final class UserProfileCriteria {

    private UserProfileCriteria() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Returns a predicate that tests whether a user profile has the specified display name.
     *
     * @param name the name to match against
     * @return a predicate evaluating to {@code true} if the profile's name matches safely
     */
    public static Predicate<UserProfile> hasName(String name) {
        return profile -> profile != null && Objects.equals(profile.getName(), name);
    }

    /**
     * Returns a predicate that tests whether a user profile has the specified username.
     *
     * @param username the username to match against
     * @return a predicate evaluating to {@code true} if the profile's username matches safely
     */
    public static Predicate<UserProfile> hasUserName(String username) {
        return profile -> profile != null && Objects.equals(profile.getUserName(), username);
    }
}