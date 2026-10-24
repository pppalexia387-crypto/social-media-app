package com.alexia.socialmediaapp.service;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.exceptions.ServiceException;


public interface FriendshipService {

    /**
     * Creates a new friendship between two users.
     *
     * @param user1 the username of the first user
     * @param user2 the username of the second user
     * @throws exceptions.ServiceException if the friendship is invalid
     * @throws ServiceException if either user does not exist
     */
    void addFriend(String user1, String user2);

    /**
     * Removes an existing friendship between two users.
     *
     * @param user1 the username of the first user
     * @param user2 the username of the second user
     * @throws ServiceException  if either user or the friendship does not exist
     */
    void removeFriend(String user1, String user2);

    /**
     * Retrieves all active friendships in the network.
     *
     * @return an {@code Iterable} of all friendships
     */
    Iterable<Friendship> getAllFriendships();
}
