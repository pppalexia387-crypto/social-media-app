package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.Validator;

import java.util.Optional;

public class FriendshipDbRepo implements Repository<Tuple<Integer, Integer>, Friendship> {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private Validator<Friendship> validator;

    public FriendshipDbRepo(String dbUrl, String dbUser, String dbPassword, Validator<Friendship> validator) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.validator = validator;
    }

    @Override
    public Iterable<Friendship> findAll() {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Integer, Integer> id) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<Friendship> delete(Tuple<Integer, Integer> id) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        throw new UnsupportedOperationException("Database operations not yet implemented.");
    }
}