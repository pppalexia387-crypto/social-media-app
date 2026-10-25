package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class UserAccountFileRepo extends AbstractFileRepo<Integer, UserAccount> {


    public UserAccountFileRepo(String fileName, UserAccountValidator validator) {
        super(fileName,validator);
    }

    @Override
    protected UserAccount extractEntity(String line) {
        String[] parts = line.split(";", -1);
        try{
        Integer id= Integer.parseInt(parts[0]);
        String email=parts[1];
        String phone=parts[2];
        String password= parts[3];
        Instant createdAt= Instant.parse(parts[4]);
        UserAccount userA= new UserAccount(email,phone,password,false,createdAt);
        userA.setId(id);
        return userA;}
        catch (NumberFormatException exp){
            throw new RepoException("Corrupted ID format in file: " + parts[0],exp);
        }
        catch (DateTimeParseException e) {
            throw new RepoException("Corrupted timestamp format in file.", e);
        }
    }

    @Override
    protected String createStringFromEntity(UserAccount entity) {
        return entity.getId()+";"+entity.getEmail()+ ";" + entity.getPhoneNr() + ";" +entity.getPassword()+";"+ entity.getCreatedAt();
    }
}
