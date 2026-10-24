package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;

public class UserProfileFileRepo extends AbstractFileRepo<Integer, UserProfile>{

    public UserProfileFileRepo(String fileName, UserProfileValidator validator) {
        super(fileName,validator);
    }

    @Override
    protected UserProfile extractEntity(String line) {
        String[] parts=line.split(";");
        Integer id= Integer.parseInt(parts[0]);
        String name=parts[1];
        String userName=parts[2];
        UserProfile userP= new UserProfile(name,userName);
        userP.setId(id);
        return userP;
    }

    @Override
    protected String createStringFromEntity(UserProfile entity) {

        return entity.getId()+";"+entity.getName()+";"+entity.getUserName();
    }
}
