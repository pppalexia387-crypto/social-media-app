package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.*;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.function.Function;

public class FileRepoFactory implements RepoFactory {
    private final String profileFileName;
    private final String accountFileName;
    private final String friendshipFileName;

    public FileRepoFactory(String profileFileName, String accountFileName, String friendshipFileName) {
        this.profileFileName = profileFileName;
        this.accountFileName = accountFileName;
        this.friendshipFileName = friendshipFileName;
    }

    @Override
    public Repository<Integer, UserProfile> createUserProfileRepo(Validator<UserProfile> validator) {
        return new FileRepo<>(line -> {
            String[] parts = line.split(";", -1);
            if (parts.length != 3) {
                throw new RepoException("Corrupt file format: Expected 3 columns but found "
                        + parts.length + " on line: " + line);
            }
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
        }, entity -> {
            return entity.getId() + ";" + entity.getName() + ";" + entity.getUserName();

        },profileFileName,validator,(Set<Integer> existingKeys)-> existingKeys.stream().mapToInt(id-> id).max().orElse(0));
}





    @Override
    public Repository<Integer, UserAccount> createUserAccountRepo(Validator<UserAccount> validator) {
        return new FileRepo<>(line->{
            String[] parts = line.split(";", -1);

            if (parts.length != 5) {
                throw new RepoException("Corrupt file format: Expected 5 columns but found "
                        + parts.length + " on line: " + line);
            }

            try {
                Integer id = Integer.parseInt(parts[0]);
                String email = parts[1];
                String phone = parts[2];
                String password = parts[3];
                Instant timestamp = Instant.parse(parts[4]);

                UserAccount account = new UserAccount(email, phone, password, false,timestamp);
                account.setId(id);
                return account;

            } catch (NumberFormatException e) {
                throw new RepoException("Corrupted ID format in file: " + parts[0], e);
            } catch (DateTimeParseException e) {
                throw new RepoException("Corrupted timestamp format in file: " + parts[4], e);
            }
        },entity->{
            return entity.getId()+";"+entity.getEmail()+ ";" + entity.getPhoneNr() + ";" +entity.getPassword()+";"+ entity.getCreatedAt();
        },accountFileName,validator,(Set<Integer> existingKeys)-> existingKeys.stream().mapToInt(id -> id).max().orElse(0) + 1);
    }

    @Override
    public Repository<Tuple<Integer,Integer>, Friendship> createFriendshipRepo(Validator<Friendship> validator) {
        return new FileRepo<>(line->{
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
        },entity->{
            return entity.getUser1Id()+";"+entity.getUser2Id()+";"+entity.getFriendsFrom();

        },friendshipFileName,validator,null);
    }
}
