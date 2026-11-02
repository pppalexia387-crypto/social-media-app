package com.alexia.socialmediaapp.domain.validators;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        if (entity.getUser1Id().equals(entity.getUser2Id())) {
            errors.append("A user cannot be friends with themselves! ");
        }

        if (entity.getFriendsFrom() == null) {
            errors.append("Friendship date cannot be null. ");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString().trim());
        }
    }
}