package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileFileRepoTest {

    @TempDir
    Path tempDir;

    private Path tempFile;
    private UserProfileValidator validator;

    @BeforeEach
    void setUp() {
        tempFile = tempDir.resolve("test_profiles.txt");
        validator = new UserProfileValidator();
    }

    @Test
    @DisplayName("Should successfully load valid users from file on startup")
    void constructor_ValidFile_LoadsData() throws IOException {
        String validData = """
                1;Alex Popescu;alex.pop
                2;Maria Ionescu;maria.io
                """;
        Files.writeString(tempFile, validData);

        UserProfileFileRepo repo = new UserProfileFileRepo(tempFile.toString(), validator);
        long count = StreamSupport.stream(repo.findAll().spliterator(), false).count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should throw RepoException when file has missing columns")
    void constructor_CorruptData_MissingColumns_ThrowsException() throws IOException {
        String corruptData = """
                1;Alex Popescu;alex.pop
                2;Maria Ionescu
                """;
        Files.writeString(tempFile, corruptData);

        RepoException exception = assertThrows(RepoException.class, () -> {
            new UserProfileFileRepo(tempFile.toString(), validator);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("3 columns"));
    }

    @Test
    @DisplayName("Should throw RepoException when ID is not an integer")
    void constructor_CorruptData_BadId_ThrowsException() throws IOException {
        String corruptData = """
                abc;Alex Popescu;alex.pop
                """;
        Files.writeString(tempFile, corruptData);

        RepoException exception = assertThrows(RepoException.class, () -> {
            new UserProfileFileRepo(tempFile.toString(), validator);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("id format"));
    }
}