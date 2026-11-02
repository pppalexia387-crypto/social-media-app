package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.Validator;

import java.util.Optional;

public class UserAccountDbRepo implements Repository<Integer, UserAccount> {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private Validator<UserAccount> validator;

    public UserAccountDbRepo(String dbUrl, String dbUser, String dbPassword, Validator<UserAccount> validator) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public Iterable<UserAccount> findAll() {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserAccount> findOne(Integer id) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserAccount> save(UserAccount entity) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserAccount> delete(Integer id) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<UserAccount> update(UserAccount entity) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }
}