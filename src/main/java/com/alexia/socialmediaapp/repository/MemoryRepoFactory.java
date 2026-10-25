package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;

public class MemoryRepoFactory implements RepoFactory{
    @Override
    public Repository<Integer, UserProfile> createUserProfileRepo(UserProfileValidator validator) {
        return new InMemoryRepo<>();
    }

    @Override
    public Repository<Integer, UserAccount> createUserAccountRepo(UserAccountValidator validator) {
        return new InMemoryRepo<>();
    }

    @Override
    public Repository<Tuple<Integer, Integer>, Friendship> createFriendshipRepo(FriendshipValidator validator) {
        return new InMemoryRepo<>();
    }
}
