package com.alexia.socialmediaapp.repository;
import com.alexia.socialmediaapp.domain.Entity;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.RepoException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
public class InMemoryRepo<ID, T extends Entity<ID>> implements Repository<ID, T> {
    protected Map<ID, T> entities = new ConcurrentHashMap<>();
    private Integer idC=1;



    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

    //to do: add findallbyid in friendshipRepo
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

        if(entity.getId()==null){
            entity.setId((ID) idC);
            idC++;
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
