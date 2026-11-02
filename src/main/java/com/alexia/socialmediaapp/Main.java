package com.alexia.socialmediaapp;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import com.alexia.socialmediaapp.repository.*;
import com.alexia.socialmediaapp.service.*;
import com.alexia.socialmediaapp.ui.ConsoleUI;

import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        try {
            //validators
            Validator<UserProfile> profileValidator = new UserProfileValidator();
            Validator<UserAccount> accountValidator = new UserAccountValidator();
            Validator<Friendship> friendshipValidator = new FriendshipValidator();

            //lambda validators
            Validator<UserProfile> profile = entity -> {
                if(entity.getUserName().length() > 25)
                    throw new ValidationException("Username cannot exceed 25 characters!");
            };

            Validator<UserAccount> account = entity -> {
                if (!entity.getPassword().matches(".*[A-Z].*")) {
                    throw new ValidationException("Password must contain an uppercase letter!");
                }
                if (!entity.getPassword().matches(".*[!#$<>_].*")) {
                    throw new ValidationException("Password must contain a special character (!, #, $, _, <, or >)!");
                }
            };

            Validator<Friendship> friend = entity -> {
                if(entity.getFriendsFrom().isAfter(Instant.now()))
                    throw new ValidationException("A friendship cannot be from the future!");
            };

            //final validators
            profileValidator = profileValidator.andThen(profile);
            accountValidator = accountValidator.andThen(account);
            friendshipValidator = friendshipValidator.andThen(friend);

            // factory
            RepoFactory factory = RepoFactory.createFactory("config.properties");

            // repos
            Repository<Integer, UserProfile> profileRepo = factory.createUserProfileRepo(profileValidator);
            Repository<Integer, UserAccount> accountRepo = factory.createUserAccountRepo(accountValidator);
            Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo = factory.createFriendshipRepo(friendshipValidator);

            // service
            UserServiceImpl userService = new UserServiceImpl(accountRepo, profileRepo, friendshipRepo, accountValidator, profileValidator);
            FriendshipServiceImpl friendshipService = new FriendshipServiceImpl(friendshipRepo, profileRepo, friendshipValidator);
            NetworkServiceImpl networkService = new NetworkServiceImpl(profileRepo, friendshipRepo);

            // integrity checks
            userService.verifyFileIntegrity();
            friendshipService.verifyFileIntegrity();

            // console
            ConsoleUI consoleUI = new ConsoleUI(userService, friendshipService, networkService);
            consoleUI.run();

        } catch (ValidationException | RepoException | ServiceException | IllegalArgumentException | UnsupportedOperationException exp) {
            System.out.println("Application Error: " + exp.getMessage());
            System.exit(1);
        }
    }
}