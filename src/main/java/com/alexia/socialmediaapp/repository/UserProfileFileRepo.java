package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.time.format.DateTimeParseException;

public class UserProfileFileRepo extends AbstractFileRepo<Integer, UserProfile>{

    public UserProfileFileRepo(String fileName, UserProfileValidator validator) {
        super(fileName,validator);
    }

    @Override
    protected UserProfile extractEntity(String line) {
        String[] parts = line.split(";", -1);
        try {
            Integer id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String userName = parts[2];
            UserProfile userP = new UserProfile(name, userName);
            userP.setId(id);
            return userP;
        } catch (NumberFormatException e) {
            throw new RepoException("Corrupted ID format in file: " + parts[0], e);
        }
        catch (DateTimeParseException e) {
            throw new RepoException("Corrupted timestamp format in file.", e);
        }
    }

    @Override
    protected String createStringFromEntity(UserProfile entity) {
        return entity.getId() + ";" + entity.getName() + ";" + entity.getUserName();
    }
}