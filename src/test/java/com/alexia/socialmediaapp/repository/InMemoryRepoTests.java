package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryRepoTests {
    private InMemoryRepo<Integer, UserAccount> repo;

    @BeforeEach
    void setUp(){
        repo = new InMemoryRepo<>();
    }

    @Test
    void save_ValidEntity_ShouldGenerateIdAndSave(){
        UserAccount user = new UserAccount("jon.gmail.com","0755193041","etc");
        Optional<UserAccount> res = repo.save(user);
        assertTrue(res.isEmpty());
        assertNotNull(user.getId());
        assertEquals(1, user.getId());
    }

    @Test
    void save_NullEntity_ShouldThrowException(){
        UserAccount user = null;
        RepoException exp = assertThrows(RepoException.class, () -> {
            repo.save(user);
        });
        assertEquals("Entity cannot be null!", exp.getMessage());
    }

    @Test
    void findAll_ShouldReturnAllEntities(){
        UserAccount user1 = new UserAccount("user1@email.com","0754123123" ,"pass1");
        UserAccount user2 = new UserAccount("user2@email.com","0743213231" ,"pass2");
        repo.save(user1);
        repo.save(user2);

        Collection<UserAccount> userList = (Collection<UserAccount>) repo.findAll();
        assertEquals(2, userList.size());
    }

    @Test
    void delete_ValidId_ShouldDeleteAndReturnEntity(){
        UserAccount user = new UserAccount("jon.gmail.com","0755193041","etc");
        repo.save(user);

        Collection<UserAccount> userList = (Collection<UserAccount>) repo.findAll();
        assertEquals(1, userList.size());

        Optional<UserAccount> res = repo.delete(user.getId());
        assertTrue(res.isPresent());
        assertEquals(user, res.get());

        userList = (Collection<UserAccount>) repo.findAll();
        assertEquals(0, userList.size());
    }

    @Test
    void delete_NullId_ShouldThrowException(){
        RepoException exp = assertThrows(RepoException.class, () -> {
            repo.delete(null);
        });
        assertEquals("Id cannot be null!", exp.getMessage());
    }

    @Test
    void delete_NotFoundId_ShouldThrowException(){
        RepoException exp = assertThrows(RepoException.class, () -> {
            repo.delete(999);
        });
        assertEquals("Id was not found!", exp.getMessage());
    }

    @Test
    void findOne_ValidId_ShouldReturnFoundEntity(){
        UserAccount user = new UserAccount("jon.gmail.com","0755193041","etc");
        repo.save(user);
        Optional<UserAccount> res = repo.findOne(user.getId());

        assertTrue(res.isPresent());
        assertEquals(user, res.get());
    }

    @Test
    void findOne_NullId_ShouldThrowException(){
        RepoException exp = assertThrows(RepoException.class, () -> {
            repo.findOne(null);
        });
        assertEquals("Id cannot be null!", exp.getMessage());
    }

    @Test
    void findOne_NotFoundId_ShouldReturnEmpty(){
        Optional<UserAccount> res = repo.findOne(999);
        assertTrue(res.isEmpty());
    }

    @Test
    void update_ValidEntity_ShouldUpdateAndReturnEmpty(){
        UserAccount user = new UserAccount("jon.gmail.com","0755193041","etc");
        repo.save(user);

        UserAccount updatedUser = new UserAccount("updated@gmail.com", "0000000000", "new info");
        updatedUser.setId(user.getId());

        Optional<UserAccount> res = repo.update(updatedUser);

        assertTrue(res.isEmpty());

        Optional<UserAccount> found = repo.findOne(user.getId());
        assertTrue(found.isPresent());
        assertEquals(updatedUser, found.get());
    }

    @Test
    void update_NotFoundEntity_ShouldReturnEntity(){
        UserAccount notSavedUser = new UserAccount("missing@gmail.com", "123", "info");
        notSavedUser.setId(999);

        Optional<UserAccount> res = repo.update(notSavedUser);

        assertTrue(res.isPresent());
        assertEquals(notSavedUser, res.get());
    }

    @Test
    void update_NullEntity_ShouldThrowException(){
        RepoException exp = assertThrows(RepoException.class, () -> {
            repo.update(null);
        });

        assertNotNull(exp.getMessage());
    }
}