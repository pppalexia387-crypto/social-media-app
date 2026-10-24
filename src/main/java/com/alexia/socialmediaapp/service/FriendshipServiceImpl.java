package com.alexia.socialmediaapp.service;



import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.repository.Repository;

import java.util.Optional;

public class FriendshipServiceImpl implements FriendshipService{
    private final Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo;
    private final Repository<Integer, UserProfile> profileRepo;
    private final Validator<Friendship> friendshipValidator;

    public FriendshipServiceImpl(Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo,
                                 Repository<Integer, UserProfile> profileRepo,
                                 Validator<Friendship> friendshipValidator) {
        this.friendshipRepo = friendshipRepo;
        this.profileRepo = profileRepo;
        this.friendshipValidator = friendshipValidator;
    }
    private Integer findUserIdByUserName(String userName){
        for(UserProfile user: profileRepo.findAll())
            if(user.getUserName().equals(userName))
                return user.getId();
        throw new ServiceException("User with username '" + userName + "' not found.");
    }

    @Override
    public void addFriend(String user1, String user2) {
        Integer id1=findUserIdByUserName(user1);
        Integer id2= findUserIdByUserName(user2);

        Friendship friendship= new Friendship(id1,id2);
        friendshipValidator.validate(friendship);

        Optional<Friendship> result = friendshipRepo.save(friendship);
        if (result.isPresent()) {
            throw new ServiceException("These users are already friends.");
        }
    }

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

    @Override
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepo.findAll();
    }
}
