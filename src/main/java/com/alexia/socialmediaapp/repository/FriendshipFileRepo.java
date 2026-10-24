package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;

import java.time.Instant;

public class FriendshipFileRepo extends AbstractFileRepo<Tuple<Integer,Integer>, Friendship> {
    public FriendshipFileRepo(String fileName, FriendshipValidator validator) {
        super(fileName,validator);
    }

    @Override
    protected Friendship extractEntity(String line) {
        String[] parts= line.split(";");
        Integer id1= Integer.parseInt(parts[0]);
        Integer id2= Integer.parseInt(parts[1]);
        Instant friendsFrom= Instant.parse(parts[2]);
        return new Friendship(id1,id2,friendsFrom);
    }

    @Override
    protected String createStringFromEntity(Friendship entity) {
        return entity.getUser1Id()+";"+entity.getUser2Id()+";"+entity.getFriendsFrom();
    }
}
