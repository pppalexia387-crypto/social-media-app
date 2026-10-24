package com.alexia.socialmediaapp.domain.validators;


import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.exceptions.ValidationException;

public class UserAccountValidator implements Validator<UserAccount> {

    @Override
    public void validate(UserAccount entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        // email
        if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
            errors.append("Email cannot be empty! ");
        } else if (!entity.getEmail().contains("@") || !entity.getEmail().contains(".")) {
            errors.append("Email format is invalid! ");
        }

        // phone number
        if (entity.getPhoneNr() == null || entity.getPhoneNr().isEmpty()) {
            errors.append("Phone number cannot be empty! ");
        } else if (!entity.getPhoneNr().matches("\\d{10}")) {
            errors.append("Phone number must contain exactly 10 digits. ");
        }

        // password
        if (entity.getPassword() == null || entity.getPassword().length() < 5) {
            errors.append("Password must be at least 5 characters long. ");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString().trim()); // .trim() removes the trailing space
        }
    }
}