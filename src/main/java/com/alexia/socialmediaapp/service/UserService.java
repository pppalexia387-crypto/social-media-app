package com.alexia.socialmediaapp.service;

import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.exceptions.ServiceException;


import java.util.Optional;

/**
 * Service interface for managing user-related operations.
 * Orchestrates actions between the UserAccount and UserProfile repositories.
 */
public interface UserService {

    /**
     * Creates and saves a new user account and its associated public profile.
     *
     * @param email        the email address for the account
     * @param phone        the phone number for the account
     * @param name         the user's real name for the profile
     * @param passwordHash the encrypted password for the account
     * @param userName     the unique public handle for the profile
     * @throws exceptions.ServiceException if the provided data violates business rules
     */
    void addUser(String email, String phone, String name, String passwordHash, String userName);

    /**
     * Permanently deletes a user from the system, removing both their
     * private account data and public profile.
     *
     * @param id the unique identifier of the user to delete; must not be null
     * @throws ServiceException if the provided id is null
     */
    void deleteUser(Integer id);

    /**
     * Permanently deletes a user from the system, removing both their
     * private account data and public profile.
     *
     * @param username the unique username of the user to delete; must not be empty
     * @throws ServiceException if the provided username is empty
     */
    void deleteUser(String username);

    /**
     * Retrieves all public user profiles available in the network.
     *
     * @return an {@code Iterable} containing all user profiles
     */
    Iterable<UserProfile> getAllUserProfiles();

    /**
     * Retrieves the private account details for a specific user.
     *
     * @param id the unique identifier of the user account; must not be null
     * @return an {@code Optional} containing the user account if found, or an empty {@code Optional} if not found
     * @throws ServiceException if the provided id is null
     */
    Optional<UserAccount> getAccountDetails(Integer id);

    /**
     * Retrieves the profile details for a specific user.
     *
     * @param id the unique identifier of the user account; must not be null
     * @return an {@code Optional} containing the user account if found, or an empty {@code Optional} if not found
     * @throws ServiceException if the provided id is null
     */
    Optional<UserProfile> getProfileDetails(Integer id);
}