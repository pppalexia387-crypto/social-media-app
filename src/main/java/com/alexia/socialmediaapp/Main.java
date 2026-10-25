package com.alexia.socialmediaapp;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import com.alexia.socialmediaapp.repository.*;
import com.alexia.socialmediaapp.service.*;
import com.alexia.socialmediaapp.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        try {
            // validators
            UserProfileValidator profileValidator = new UserProfileValidator();
            UserAccountValidator accountValidator = new UserAccountValidator();
            FriendshipValidator friendshipValidator = new FriendshipValidator();

            // factory - configuration and switch logic is abstracted away
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