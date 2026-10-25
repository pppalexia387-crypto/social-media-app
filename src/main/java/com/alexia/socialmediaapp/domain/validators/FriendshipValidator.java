package com.alexia.socialmediaapp.domain.validators;


import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getUser1Id().equals(entity.getUser2Id())) {
            throw new ValidationException("A user cannot be friends with themselves!");
        }

    }
}