package com.alexia.socialmediaapp.domain;

import java.util.function.Predicate;

public class FriendshipCriteria {
    public static Predicate<Friendship> hasFirstUser(Integer userId) {
        return f -> f.getUser1Id().equals(userId);
    }
    public static Predicate<Friendship> hasSecondUser(Integer userId) {
        return f -> f.getUser2Id().equals(userId);
    }
}
