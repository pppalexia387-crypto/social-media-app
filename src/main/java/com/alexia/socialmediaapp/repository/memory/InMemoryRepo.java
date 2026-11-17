package com.alexia.socialmediaapp.repository;
import com.alexia.socialmediaapp.domain.Entity;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryRepo<ID, T extends Entity<ID>> implements Repository<ID, T> {
    protected Map<ID, T> entities = new ConcurrentHashMap<>();
    private final Function<Set<ID>, ID> idGenerator;

    public InMemoryRepo(Function<Set<ID>, ID> idGenerator) {
        this.idGenerator = idGenerator;
    }

    public InMemoryRepo() {
        this.idGenerator = null;
    }
    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

    @Override
    public Optional<T> findOne(ID id) {
        if(id==null)
            throw new RepoException("Id cannot be null!");
        return Optional.ofNullable(entities.get(id));


    }

    @Override
    public Optional<T> save(T entity) {
        if(entity==null)
                throw new RepoException("Entity cannot be null!");
        if(entity.getId() != null && entities.containsKey(entity.getId()))
            return Optional.of(entity);

        if (entity.getId() == null && idGenerator != null) {
            entity.setId(idGenerator.apply(entities.keySet()));
        }

        entities.put(entity.getId(), entity);
        return Optional.empty();
    }

    @Override
    public Optional<T> delete(ID id) {
        if(id==null)
            throw new RepoException("Id cannot be null!");
        if(!entities.containsKey(id))
            throw new RepoException("Id was not found!");
        T deleted= entities.get(id);
        entities.remove(id);
        return Optional.of(deleted);

    }

    @Override
    public Optional<T> update(T entity) {
        if(entity==null)
            throw new RepoException("Entity cannot be null!");
        if(entity.getId()==null)
            throw new RepoException("Id cannot be null!");
        if(entities.containsKey(entity.getId())){
            entities.replace(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }





}
