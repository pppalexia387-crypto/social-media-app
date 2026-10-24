package com.alexia.socialmediaapp.domain.validators;


import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.exceptions.ValidationException;

public class UserProfileValidator implements Validator<UserProfile> {

    @Override
    public void validate(UserProfile entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        //name
        if (entity.getName() == null || entity.getName().trim().isEmpty())
            errors.append("Name cannot be empty");
        else if (!entity.getName().matches("^[a-zA-Z\\s\\-']+$")) {
            errors.append("Real name can only contain letters, spaces, hyphens, and apostrophes! ");

            //username
            if (entity.getUserName() == null || entity.getUserName().trim().isEmpty()) {
                errors.append("Username cannot be empty. ");
            } else if (entity.getUserName().contains(" ")) {
                errors.append("Username cannot contain spaces. ");
            }
            //bio
            if (entity.getBio() != null && entity.getBio().length() > 150) {
                errors.append("Bio cannot exceed 150 characters. ");
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors.toString().trim());
            }
        }
    }
}