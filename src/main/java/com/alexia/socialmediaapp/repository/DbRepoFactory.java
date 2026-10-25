package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;

public class DbRepoFactory implements RepoFactory {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public DbRepoFactory(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    @Override
    public Repository<Integer, UserProfile> createUserProfileRepo(UserProfileValidator validator) {
        return new UserProfileDbRepo(dbUrl, dbUser, dbPassword, validator);
    }

    @Override
    public Repository<Integer, UserAccount> createUserAccountRepo(UserAccountValidator validator) {
        return new UserAccountDbRepo(dbUrl, dbUser, dbPassword, validator);
    }

    @Override
    public Repository<Tuple<Integer, Integer>, Friendship> createFriendshipRepo(FriendshipValidator validator) {
        return new FriendshipDbRepo(dbUrl, dbUser, dbPassword, validator);
    }
}