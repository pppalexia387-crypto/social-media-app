package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class FriendshipFileRepo extends AbstractFileRepo<Tuple<Integer,Integer>, Friendship> {
    public FriendshipFileRepo(String fileName, FriendshipValidator validator) {
        super(fileName,validator);
    }

    @Override
    protected Friendship extractEntity(String line) {
        String[] parts = line.split(";", -1);
        try{
        Integer id1= Integer.parseInt(parts[0]);
        Integer id2= Integer.parseInt(parts[1]);
        Instant friendsFrom= Instant.parse(parts[2]);
        return new Friendship(id1,id2,friendsFrom);}
        catch (NumberFormatException exp){
            throw new RepoException("Corrupted ID format in file: " + parts[0] +"/"+parts[1],exp);
        }
        catch (DateTimeParseException e) {
            throw new RepoException("Corrupted timestamp format in file.", e);
        }
    }

    @Override
    protected String createStringFromEntity(Friendship entity) {
        return entity.getUser1Id()+";"+entity.getUser2Id()+";"+entity.getFriendsFrom();
    }
}
