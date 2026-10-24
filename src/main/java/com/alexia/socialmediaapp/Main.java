package com.alexia.socialmediaapp;


import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.repository.FriendshipFileRepo;
import com.alexia.socialmediaapp.repository.InMemoryRepo;
import com.alexia.socialmediaapp.repository.UserAccountFileRepo;
import com.alexia.socialmediaapp.repository.UserProfileFileRepo;
import com.alexia.socialmediaapp.service.FriendshipServiceImpl;
import com.alexia.socialmediaapp.service.NetworkServiceImpl;
import com.alexia.socialmediaapp.service.UserServiceImpl;
import com.alexia.socialmediaapp.ui.ConsoleUI;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //validators
        UserAccountValidator accountValidator= new UserAccountValidator();
        UserProfileValidator profileValidator= new UserProfileValidator();
        FriendshipValidator friendshipValidator= new FriendshipValidator();

        //repos in memory
        InMemoryRepo<Integer, UserAccount> accountRepo= new InMemoryRepo<>();
        InMemoryRepo<Integer, UserProfile> profileRepo = new InMemoryRepo<>();
        InMemoryRepo<Tuple<Integer,Integer>, Friendship> friendshipRepo= new InMemoryRepo<>();

        //repos in file
        FriendshipFileRepo friendshipInFileRepo= new FriendshipFileRepo("data/friendships.csv",friendshipValidator);
        UserProfileFileRepo profileInFileRepo = new UserProfileFileRepo("data/user_profiles.csv", profileValidator);
        UserAccountFileRepo accountInFileRepo = new UserAccountFileRepo("data/user_accounts.csv", accountValidator);




        //services
        UserServiceImpl userService = new UserServiceImpl(accountRepo,profileRepo,accountValidator,profileValidator,friendshipRepo);
        FriendshipServiceImpl friendshipService= new FriendshipServiceImpl(friendshipRepo,profileRepo,friendshipValidator);
        NetworkServiceImpl networkService= new NetworkServiceImpl(profileRepo,friendshipRepo);
        //console
        ConsoleUI console = new ConsoleUI(userService,friendshipService,networkService);
        console.run();


    }
}
