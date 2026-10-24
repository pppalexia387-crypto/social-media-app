package com.alexia.socialmediaapp.service;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.repository.InMemoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTests {

    private InMemoryRepo<Integer, UserAccount> accountRepo;
    private InMemoryRepo<Integer, UserProfile> profileRepo;
    private InMemoryRepo<Tuple<Integer, Integer>, Friendship> friendshipRepo;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        accountRepo = new InMemoryRepo<>();
        profileRepo = new InMemoryRepo<>();
        friendshipRepo = new InMemoryRepo<>();

        UserAccountValidator accountValidator = new UserAccountValidator();
        UserProfileValidator profileValidator = new UserProfileValidator();

        userService = new UserServiceImpl(accountRepo, profileRepo, accountValidator, profileValidator, friendshipRepo);
    }

    @Test
    void addUser_ValidData_ShouldSaveUser() {
        userService.addUser("test@email.com", "0755123456", "Test Name", "password", "testUser");

        Collection<UserAccount> accounts = (Collection<UserAccount>) accountRepo.findAll();
        Collection<UserProfile> profiles = (Collection<UserProfile>) profileRepo.findAll();

        assertEquals(1, accounts.size());
        assertEquals(1, profiles.size());
    }

    @Test
    void addUser_DuplicateEmail_ShouldThrowException() {
        userService.addUser("test@email.com", "0755123456", "Test Name", "password", "testUser1");

        ServiceException exp = assertThrows(ServiceException.class, () -> {
            userService.addUser("test@email.com", "0744111222", "Other Name", "pass2", "testUser2");
        });

        assertEquals("Email is already taken!", exp.getMessage());
    }

    @Test
    void addUser_DuplicateUsername_ShouldThrowException() {
        userService.addUser("test1@email.com", "0755123456", "Test Name", "password", "testUser");

        ServiceException exp = assertThrows(ServiceException.class, () -> {
            userService.addUser("test2@email.com", "0744111222", "Other Name", "pass2", "testUser");
        });

        assertEquals("Username is already taken!", exp.getMessage());
    }

    @Test
    void deleteUserById_ValidId_ShouldDeleteUserAndFriendships() {
        UserAccount acc1 = new UserAccount("e1@email.com", "0711111111", "p1");
        accountRepo.save(acc1);
        UserProfile prof1 = new UserProfile("N1", "user1");
        prof1.setId(acc1.getId());
        profileRepo.save(prof1);

        UserAccount acc2 = new UserAccount("e2@email.com", "0722222222", "p2");
        accountRepo.save(acc2);
        UserProfile prof2 = new UserProfile("N2", "user2");
        prof2.setId(acc2.getId());
        profileRepo.save(prof2);

        UserAccount acc3 = new UserAccount("e3@email.com", "0733333333", "p3");
        accountRepo.save(acc3);
        UserProfile prof3 = new UserProfile("N3", "user3");
        prof3.setId(acc3.getId());
        profileRepo.save(prof3);

        friendshipRepo.save(new Friendship(acc1.getId(), acc2.getId()));
        friendshipRepo.save(new Friendship(acc2.getId(), acc3.getId()));

        userService.deleteUser(acc1.getId());

        assertTrue(accountRepo.findOne(acc1.getId()).isEmpty());
        assertTrue(profileRepo.findOne(acc1.getId()).isEmpty());

        Collection<Friendship> friendships = (Collection<Friendship>) friendshipRepo.findAll();
        assertEquals(1, friendships.size());
    }

    @Test
    void deleteUserByUsername_ValidUsername_ShouldDeleteUserAndFriendships() {
        UserAccount acc1 = new UserAccount("e1@email.com", "0711111111", "p1");
        accountRepo.save(acc1);
        UserProfile prof1 = new UserProfile("N1", "user1ToFind");
        prof1.setId(acc1.getId());
        profileRepo.save(prof1);

        UserAccount acc2 = new UserAccount("e2@email.com", "0722222222", "p2");
        accountRepo.save(acc2);
        UserProfile prof2 = new UserProfile("N2", "user2");
        prof2.setId(acc2.getId());
        profileRepo.save(prof2);

        friendshipRepo.save(new Friendship(acc1.getId(), acc2.getId()));

        userService.deleteUser("user1ToFind");

        assertTrue(accountRepo.findOne(acc1.getId()).isEmpty());
        assertTrue(profileRepo.findOne(acc1.getId()).isEmpty());

        Collection<Friendship> friendships = (Collection<Friendship>) friendshipRepo.findAll();
        assertEquals(0, friendships.size());
    }

    @Test
    void deleteUserByUsername_NotFound_ShouldThrowException() {
        ServiceException exp = assertThrows(ServiceException.class, () -> {
            userService.deleteUser("missingUsername");
        });

        assertTrue(exp.getMessage().contains("Cannot delete: User with username 'missingUsername' not found."));
    }

    @Test
    void getAccountDetails_ValidId_ShouldReturnAccount() {
        UserAccount acc = new UserAccount("test@email.com", "0700", "pass");
        accountRepo.save(acc);

        Optional<UserAccount> res = userService.getAccountDetails(acc.getId());

        assertTrue(res.isPresent());
        assertEquals(acc, res.get());
    }
}