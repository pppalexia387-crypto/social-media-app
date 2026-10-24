package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Entity;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.util.Optional;

/**
 * Generic CRUD operations repository for managing entities.
 *
 * @param <ID> the type of the identifier used by the entity
 * @param <T>  the type of the entity being managed, which must extend {@code Entity<ID>}
 */
public interface Repository<ID, T extends Entity<ID>> {

        /**
         * Retrieves all entities managed by the repository.
         *
         * @return an {@code Iterable} containing all entities
         */
        Iterable<T> findAll();

        /**
         * Retrieves an entity by its identifier.
         *
         * @param id the identifier of the entity to be returned; must not be null
         * @return an {@code Optional} containing the entity if it exists, or an empty {@code Optional} if not found
         * @throws RepoException if the provided id is null
         */
        Optional<T> findOne(ID id);

        /**
         * Saves a new entity to the repository.
         *
         * @param entity the entity to be saved; must not be null
         * @return an empty {@code Optional} if the entity was saved successfully,
         *         or an {@code Optional} containing the entity if an entity with the same id already exists
         * @throws RepoException if the provided entity is null
         */
        Optional<T> save(T entity);

        /**
         * Removes the entity with the specified identifier.
         *
         * @param id the identifier of the entity to be removed; must not be null
         * @return an {@code Optional} containing the removed entity if it was successfully deleted,
         *         or an empty {@code Optional} if no entity with the given id was found
         * @throws RepoException if the provided id is null
         */
        Optional<T> delete(ID id);

        /**
         * Updates an existing entity in the repository.
         *
         * @param entity the entity containing updated data; must not be null
         * @return an empty {@code Optional} if the entity was successfully updated,
         *         or an {@code Optional} containing the entity if no entity with the given id was found
         * @throws RepoException if the provided entity is null
         */
        Optional<T> update(T entity);

}