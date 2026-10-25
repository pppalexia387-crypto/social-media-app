package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.*;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;

public class    FileRepoFactory implements RepoFactory{
    private final String profileFileName;
    private final String accountFileName;
    private final String friendshipFileName;

    public FileRepoFactory(String profileFileName, String accountFileName, String friendshipFileName) {
        this.profileFileName = profileFileName;
        this.accountFileName = accountFileName;
        this.friendshipFileName = friendshipFileName;
    }

    @Override
    public Repository<Integer, UserProfile> createUserProfileRepo(UserProfileValidator validator) {
        return new UserProfileFileRepo(profileFileName, validator);
    }

    @Override
    public Repository<Integer, UserAccount> createUserAccountRepo(UserAccountValidator validator) {
        return new UserAccountFileRepo(accountFileName,validator);
    }

    @Override
    public Repository<Tuple<Integer,Integer>, Friendship> createFriendshipRepo(FriendshipValidator validator) {
        return new FriendshipFileRepo(friendshipFileName,validator);
    }
}
