package com.alexia.socialmediaapp.domain;

import java.time.Instant;

public class Friendship extends Entity<Tuple<Integer,Integer>> {

    private final Instant friendsFrom;
    public Friendship(Integer id1, Integer id2){
        Integer left=Math.min(id1,id2);
        Integer right= Math.max(id1,id2);

        this.setId(new Tuple<>(left,right));
        this.friendsFrom= Instant.now();

    }
    public Friendship(Integer id1, Integer id2,Instant friendsFrom){
        Integer left=Math.min(id1,id2);
        Integer right= Math.max(id1,id2);

        this.setId(new Tuple<>(left,right));
        this.friendsFrom= friendsFrom;

    }
    public Integer getUser1Id() {
        return this.getId().getLeft();
    }

    public Integer getUser2Id() {
        return this.getId().getRight();
    }

    public Instant getFriendsFrom() {
        return friendsFrom;
    }

}

