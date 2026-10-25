package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;

import java.util.Optional;

public class UserProfileDbRepo implements Repository<Integer, UserProfile> {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private UserProfileValidator validator;

    public UserProfileDbRepo(String dbUrl, String dbUser, String dbPassword, UserProfileValidator validator) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public Iterable<UserProfile> findAll() {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserProfile> findOne(Integer integer) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserProfile> save(UserProfile entity) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserProfile> delete(Integer integer) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserProfile> update(UserProfile entity) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }
}