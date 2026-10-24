package com.alexia.socialmediaapp.service;



import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.repository.InMemoryRepo;
import com.alexia.socialmediaapp.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final InMemoryRepo<Integer, UserAccount> accountRepo;
    private final InMemoryRepo<Integer, UserProfile> profileRepo;
    private final UserAccountValidator accountValidator;
    private final UserProfileValidator profileValidator;
    private final Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo;

    public UserServiceImpl(InMemoryRepo<Integer,UserAccount> accountRepo, InMemoryRepo<Integer,UserProfile> profileRepo, UserAccountValidator accountValidator, UserProfileValidator profileValidator, Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo){
        this.accountRepo=accountRepo;
        this.profileRepo=profileRepo;
        this.accountValidator=accountValidator;
        this.profileValidator=profileValidator;
        this.friendshipRepo = friendshipRepo;
    }

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

        UserAccount userA= new UserAccount(email,phone,passwordHash);
        UserProfile userP = new UserProfile(name,userName);

        accountValidator.validate(userA);
        profileValidator.validate(userP);
        accountRepo.save(userA);
        userP.setId(userA.getId());
        profileRepo.save(userP);

    }

    @Override
    public void deleteUser(Integer id) {
        accountRepo.delete(id);
        profileRepo.delete(id);
        deleteAllFriendshipsForUser(id);
    }

    @Override
    public void deleteUser(String userName) {
        Integer id=null;
        for(UserProfile profile : profileRepo.findAll())
            if(profile.getUserName().equals(userName))
                id=profile.getId();

        if (id == null) {
            throw new ServiceException("Cannot delete: User with username '" + userName + "' not found.");
        }
        accountRepo.delete(id);
        profileRepo.delete(id);
        deleteAllFriendshipsForUser(id);
    }

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

    @Override
    public Iterable<UserProfile> getAllUserProfiles() {
        return profileRepo.findAll();
    }

    @Override
    public Optional<UserAccount> getAccountDetails(Integer id) {
        return accountRepo.findOne(id);
    }

    @Override
    public Optional<UserProfile> getProfileDetails(Integer id) {
        return profileRepo.findOne(id);
    }


}
