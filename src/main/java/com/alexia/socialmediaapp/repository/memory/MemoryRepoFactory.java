package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.Validator;

import java.util.Set;

public class MemoryRepoFactory implements RepoFactory{
    @Override
    public Repository<Integer, UserProfile> createUserProfileRepo(Validator<UserProfile> validator) {
        return new InMemoryRepo<>((Set<Integer> keys)-> keys.stream().mapToInt(id->id).max().orElse(0)+1);
    }

    @Override
    public Repository<Integer, UserAccount> createUserAccountRepo(Validator<UserAccount> validator) {
        return new InMemoryRepo<>((Set<Integer> keys)-> keys.stream().mapToInt(id->id).max().orElse(0)+1);
    }

    @Override
    public Repository<Tuple<Integer, Integer>, Friendship> createFriendshipRepo(Validator<Friendship> validator) {
        return new InMemoryRepo<>();
    }
}
