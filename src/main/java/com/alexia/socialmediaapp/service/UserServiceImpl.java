package com.alexia.socialmediaapp.service;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import com.alexia.socialmediaapp.repository.Repository;

import java.util.*;

/**
 * Implementation of the UserService interface that manages user accounts, profiles,
 * and their associated dependencies like friendships.
 */
public class UserServiceImpl implements UserService {
    private final Repository<Integer, UserAccount> accountRepo;
    private final Repository<Integer, UserProfile> profileRepo;
    private final Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo;
    private final UserAccountValidator accountValidator;
    private final UserProfileValidator profileValidator;

    /**
     * Constructs a new UserServiceImpl with the necessary repositories and validators.
     *
     * @param accountRepo the repository for managing UserAccount entities
     * @param profileRepo the repository for managing UserProfile entities
     * @param friendshipRepo the repository for managing Friendship entities
     * @param accountValidator the validator for UserAccount entities
     * @param profileValidator the validator for UserProfile entities
     */
    public UserServiceImpl(Repository<Integer, UserAccount> accountRepo,
                           Repository<Integer, UserProfile> profileRepo,
                           Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo,
                           UserAccountValidator accountValidator,
                           UserProfileValidator profileValidator) {
        this.accountRepo = accountRepo;
        this.profileRepo = profileRepo;
        this.friendshipRepo = friendshipRepo;
        this.accountValidator = accountValidator;
        this.profileValidator = profileValidator;
    }

    /**
     * Adds a new user to the application, creating both an account and a profile.
     *
     * @param email the email address of the user
     * @param phone the phone number of the user
     * @param name the full name of the user
     * @param passwordHash the hashed password of the user
     * @param userName the chosen username for the user
     * @throws ServiceException if the email or username is already taken
     * @throws ValidationException if the created user account or profile is invalid
     * @throws IllegalArgumentException if any of the provided entity fields are null when saving
     * @throws RepoException if a repository error occurs during the save operation
     */
    @Override
    public void addUser(String email, String phone, String name, String passwordHash, String userName) {
        for (UserAccount account : accountRepo.findAll()) {
            if (account.getEmail().equals(email)) {
                throw new ServiceException("Email is already taken!");
            }
        }

        for (UserProfile profile : profileRepo.findAll()) {
            if (profile.getUserName().equals(userName)) {
                throw new ServiceException("Username is already taken!");
            }
        }

        UserAccount userA = new UserAccount(email, phone, passwordHash);
        UserProfile userP = new UserProfile(name, userName);

        accountValidator.validate(userA);
        profileValidator.validate(userP);
        accountRepo.save(userA);
        userP.setId(userA.getId());
        profileRepo.save(userP);
    }

    /**
     * Deletes a user and all their associated friendships by their unique ID.
     *
     * @param id the unique identifier of the user to delete
     * @throws IllegalArgumentException if the provided id is null
     * @throws RepoException if a repository error occurs during deletion
     */
    @Override
    public void deleteUser(Integer id) {
        accountRepo.delete(id);
        profileRepo.delete(id);
        deleteAllFriendshipsForUser(id);
    }

    /**
     * Deletes a user and all their associated friendships by their username.
     *
     * @param userName the username of the user to delete
     * @throws ServiceException if no user is found with the given username
     * @throws IllegalArgumentException if the corresponding resolved id is null
     * @throws RepoException if a repository error occurs during deletion
     */
    @Override
    public void deleteUser(String userName) {
        Integer id = null;
        for (UserProfile profile : profileRepo.findAll()) {
            if (profile.getUserName().equals(userName)) {
                id = profile.getId();
            }
        }

        if (id == null) {
            throw new ServiceException("Cannot delete: User with username '" + userName + "' not found.");
        }
        accountRepo.delete(id);
        profileRepo.delete(id);
        deleteAllFriendshipsForUser(id);
    }

    /**
     * Private helper method to delete all friendships associated with a specific user ID.
     *
     * @param userId the ID of the user whose friendships should be deleted
     */
    private void deleteAllFriendshipsForUser(Integer userId) {
        List<Tuple<Integer, Integer>> friendshipsToDelete = new ArrayList<>();
        for (Friendship f : friendshipRepo.findAll()) {
            if (f.getUser1Id().equals(userId) || f.getUser2Id().equals(userId)) {
                friendshipsToDelete.add(new Tuple<>(f.getUser1Id(), f.getUser2Id()));
            }
        }

        for (Tuple<Integer, Integer> tuple : friendshipsToDelete) {
            friendshipRepo.delete(tuple);
        }
    }

    /**
     * Retrieves all user profiles stored in the repository.
     *
     * @return an iterable collection of all UserProfile entities
     * @throws RepoException if a repository error occurs while fetching data
     */
    @Override
    public Iterable<UserProfile> getAllUserProfiles() {
        return profileRepo.findAll();
    }

    /**
     * Retrieves the account details for a specific user.
     *
     * @param id the unique identifier of the user account
     * @return an Optional containing the UserAccount if found, or empty if not found
     * @throws IllegalArgumentException if the provided id is null
     * @throws RepoException if a repository error occurs while fetching data
     */
    @Override
    public Optional<UserAccount> getAccountDetails(Integer id) {
        return accountRepo.findOne(id);
    }

    /**
     * Retrieves the profile details for a specific user.
     *
     * @param id the unique identifier of the user profile
     * @return an Optional containing the UserProfile if found, or empty if not found
     * @throws IllegalArgumentException if the provided id is null
     * @throws RepoException if a repository error occurs while fetching data
     */
    @Override
    public Optional<UserProfile> getProfileDetails(Integer id) {
        return profileRepo.findOne(id);
    }

    /**
     * Verifies the integrity of the stored data, ensuring no duplicate usernames exist.
     *
     * @throws RepoException if corrupt data (like duplicate usernames) is found in the repository
     */
    public void verifyFileIntegrity() {
        Set<String> uniqueUsernames = new HashSet<>();

        for (UserProfile profile : profileRepo.findAll()) {
            if (!uniqueUsernames.add(profile.getUserName())) {
                throw new RepoException("Corrupt data: Duplicate username '" + profile.getUserName() + "' found in file.");
            }
        }
    }
}