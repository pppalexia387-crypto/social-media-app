package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountFileRepoTest {

    @TempDir
    Path tempDir;

    private Path tempFile;
    private UserAccountValidator validator;

    @BeforeEach
    void setUp() {
        tempFile = tempDir.resolve("test_accounts.txt");
        validator = new UserAccountValidator();
    }

    @Test
    @DisplayName("Should successfully load valid accounts from file")
    void constructor_ValidFile_LoadsData() throws IOException {
        String validData = """
                1;alex@gmail.com;0740123456;pass123;2026-09-16T15:55:55.555555Z
                2;maria@io.com;0750123456;secure456;2026-10-16T15:55:55.555555Z
                """;
        Files.writeString(tempFile, validData);

        UserAccountFileRepo repo = new UserAccountFileRepo(tempFile.toString(), validator);
        long count = StreamSupport.stream(repo.findAll().spliterator(), false).count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should throw RepoException when file is missing columns")
    void constructor_CorruptData_MissingColumns_ThrowsException() throws IOException {
        String corruptData = """
                1;alex@gmail.com;0740123456;pass123
                """;
        Files.writeString(tempFile, corruptData);

        RepoException exception = assertThrows(RepoException.class, () -> {
            new UserAccountFileRepo(tempFile.toString(), validator);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("5 columns"));
    }

    @Test
    @DisplayName("Should throw RepoException when ID is corrupted")
    void constructor_CorruptData_BadId_ThrowsException() throws IOException {
        String corruptData = """
                abc;alex@gmail.com;0740123456;pass123;2023-10-28T10:00:00
                """;
        Files.writeString(tempFile, corruptData);

        RepoException exception = assertThrows(RepoException.class, () -> {
            new UserAccountFileRepo(tempFile.toString(), validator);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("id format"));
    }

    @Test
    @DisplayName("Should throw RepoException when timestamp is corrupted")
    void constructor_CorruptData_BadTimestamp_ThrowsException() throws IOException {
        String corruptData = """
                1;alex@gmail.com;0740123456;pass123;Not-A-Date
                """;
        Files.writeString(tempFile, corruptData);

        RepoException exception = assertThrows(RepoException.class, () -> {
            new UserAccountFileRepo(tempFile.toString(), validator);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("timestamp format"));
    }
}