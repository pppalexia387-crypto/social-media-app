package com.alexia.socialmediaapp.service;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import com.alexia.socialmediaapp.repository.Repository;

import java.util.Optional;

/**
 * Implementation of the FriendshipService interface that manages friendship connections
 * between users in the social network.
 */
public class FriendshipServiceImpl implements FriendshipService {
    private final Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo;
    private final Repository<Integer, UserProfile> profileRepo;
    private final Validator<Friendship> friendshipValidator;

    /**
     * Constructs a FriendshipServiceImpl with the required repositories and validator.
     *
     * @param friendshipRepo the repository for managing Friendship entities
     * @param profileRepo the repository for retrieving UserProfile entities
     * @param friendshipValidator the validator for Friendship entities
     */
    public FriendshipServiceImpl(Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo,
                                 Repository<Integer, UserProfile> profileRepo,
                                 Validator<Friendship> friendshipValidator) {
        this.friendshipRepo = friendshipRepo;
        this.profileRepo = profileRepo;
        this.friendshipValidator = friendshipValidator;
    }

    /**
     * Helper method to find a user's ID based on their username.
     *
     * @param userName the username to search for
     * @return the unique ID of the matching user
     * @throws ServiceException if no user is found with the given username
     * @throws RepoException if a repository error occurs while retrieving user data
     */
    private Integer findUserIdByUserName(String userName) {
        for (UserProfile user : profileRepo.findAll()) {
            if (user.getUserName().equals(userName)) {
                return user.getId();
            }
        }
        throw new ServiceException("User with username '" + userName + "' not found.");
    }

    /**
     * Creates a new friendship between two users identified by their usernames.
     *
     * @param user1 the username of the first user
     * @param user2 the username of the second user
     * @throws ServiceException if either user does not exist or if they are already friends
     * @throws ValidationException if the resulting friendship entity is invalid
     * @throws IllegalArgumentException if the constructed entity is null
     * @throws RepoException if a repository error occurs during the save operation
     */
    @Override
    public void addFriend(String user1, String user2) {
        Integer id1 = findUserIdByUserName(user1);
        Integer id2 = findUserIdByUserName(user2);

        Friendship friendship = new Friendship(id1, id2);
        friendshipValidator.validate(friendship);

        Optional<Friendship> result = friendshipRepo.save(friendship);
        if (result.isPresent()) {
            throw new ServiceException("These users are already friends.");
        }
    }

    /**
     * Removes an existing friendship between two users identified by their usernames.
     *
     * @param userName1 the username of the first user
     * @param userName2 the username of the second user
     * @throws ServiceException if either user does not exist or if the friendship does not exist
     * @throws IllegalArgumentException if the constructed ID tuple is null
     * @throws RepoException if a repository error occurs during the deletion operation
     */
    @Override
    public void removeFriend(String userName1, String userName2) {
        Integer id1 = findUserIdByUserName(userName1);
        Integer id2 = findUserIdByUserName(userName2);

        Tuple<Integer, Integer> friendshipId = new Tuple<>(Math.min(id1, id2), Math.max(id1, id2));
        Optional<Friendship> result = friendshipRepo.delete(friendshipId);
        if (result.isEmpty()) {
            throw new ServiceException("Friendship does not exist.");
        }
    }

    /**
     * Retrieves all friendships currently stored in the application.
     *
     * @return an iterable collection of all Friendship entities
     * @throws RepoException if a repository error occurs while retrieving friendship data
     */
    @Override
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepo.findAll();
    }

    /**
     * Verifies the referential integrity of the friendship data, ensuring no friendships
     * exist for deleted or non-existent user IDs.
     *
     * @throws RepoException if corrupt data (friendships with missing users) is found in the repository
     */
    public void verifyFileIntegrity() {
        for (Friendship f : friendshipRepo.findAll()) {
            if (profileRepo.findOne(f.getUser1Id()).isEmpty() ||
                    profileRepo.findOne(f.getUser2Id()).isEmpty()) {
                throw new RepoException("Corrupt data: Friendship contains deleted or fake user IDs.");
            }
        }
    }
}