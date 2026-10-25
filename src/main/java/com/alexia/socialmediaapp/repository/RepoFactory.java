package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserAccount;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.FriendshipValidator;
import com.alexia.socialmediaapp.domain.validators.UserAccountValidator;
import com.alexia.socialmediaapp.domain.validators.UserProfileValidator;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Factory interface for abstracting repository creation.
 */
public interface RepoFactory {
    /**
     * Creates a repository for managing user profiles.
     * @param validator the validator used to enforce user profile rules
     * @return a configured repository instance for UserProfile entities
     */
    Repository<Integer, UserProfile> createUserProfileRepo(UserProfileValidator validator);

    /**
     * Creates a repository for managing user accounts.
     * @param validator the validator used to enforce user account rules
     * @return a configured repository instance for UserAccount entities
     */
    Repository<Integer, UserAccount> createUserAccountRepo(UserAccountValidator validator);

    /**
     * Creates a repository for managing friendships.
     * @param validator the validator used to enforce friendship rules
     * @return a configured repository instance for Friendship entities
     */
    Repository<Tuple<Integer, Integer>, Friendship> createFriendshipRepo(FriendshipValidator validator);
    /**
     * Reads the specified configuration file and instantiates the appropriate
     * RepoFactory implementation (Memory, File, or Database).
     *
     * @param configFileName the name of the configuration file located in the resources folder
     * @return a configured instance of RepoFactory based on the 'repo.type' property
     * @throws RepoException if the configuration file is not found, cannot be read,
     *                       is missing required properties, or contains an invalid repository type
     */
        static RepoFactory createFactory(String configFileName) {
            Properties props = new Properties();

            try (InputStream input = RepoFactory.class.getClassLoader().getResourceAsStream(configFileName)) {
                if (input == null) {
                    throw new RepoException("Unable to find '" + configFileName + "' in the resources folder.");
                }
                props.load(input);
            } catch (IOException e) {
                throw new RepoException("Error reading config file: " + e.getMessage(), e);
            }

            String typeStr = props.getProperty("repo.type");
            if (typeStr == null) {
                throw new RepoException("Missing 'repo.type' property in configuration.");
            }

            RepoType repoType;
            try {
                repoType = RepoType.valueOf(typeStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RepoException("Unknown 'repo.type' value in config: '" + typeStr + "'", e);
            }

            switch (repoType) {
                case MEMORY: {
                    return new MemoryRepoFactory();
                }
                case FILE: {
                    String profileFileName = props.getProperty("file.profiles");
                    String accountFileName = props.getProperty("file.accounts");
                    String friendshipFileName = props.getProperty("file.friendships");
                    return new FileRepoFactory(profileFileName, accountFileName, friendshipFileName);
                }
                case DATABASE: {
                    String dbUrl = props.getProperty("db.url");
                    String dbUser = props.getProperty("db.user");
                    String dbPass = props.getProperty("db.password");
                    return new DbRepoFactory(dbUrl, dbUser, dbPass);
                }
                default: {
                    return new MemoryRepoFactory();
                }
            }
        }
    }
