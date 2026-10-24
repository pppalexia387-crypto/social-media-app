package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Entity;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ValidationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractFileRepo<ID, T extends Entity<ID>> extends InMemoryRepo<ID, T> {
    private final String fileName;
    private Validator<T> validator;
    /**
     * creates an entity from a line of the file
     *
     * @param line - a line of the file
     * @return the entity described by the line
     */
    protected abstract T extractEntity(String line);

    /**
     * converts an entity into a line of the file
     *
     * @param entity - the entity to be converted
     * @return the line of text that describes the entity
     */
    protected abstract String createStringFromEntity(T entity);

    public AbstractFileRepo(String fileName, Validator<T> validator) {
        super();
        this.fileName = fileName;
        this.validator = validator;
        loadData();

    }

    
    public void loadData(){
        Path path= Paths.get(fileName);
        if(!Files.exists(path))
            return;
        try{
            List<String> lines= Files.readAllLines(path);
            for(String line : lines){
                if(line.trim().isEmpty())
                    continue;

                try{
                    validator.validate(extractEntity(line));
                    super.save(extractEntity(line));
                }
                catch (ValidationException exp){
                    throw new RepoException(exp.getMessage());
                }
            }

        }
        catch (IOException exp){
            throw new RepoException(exp.getMessage());
        }

    }
    protected void writeToFile() {
        Path path = Paths.get(fileName);

        try {
            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            List<String> lines = new ArrayList<>();
            for (T entity : super.findAll()) {
                lines.add(createStringFromEntity(entity));
            }

            Files.write(path, lines);

        } catch (IOException e) {
            throw new RepoException(e.getMessage());
        }
    }

    @Override
    public Optional<T> update(T entity) {
        Optional<T> res=super.update(entity);
        if(res.isEmpty())
            writeToFile();
        return res;
    }

    @Override
    public Optional<T> delete(ID id) {
        Optional<T> res= super.delete(id);
        if(res.isPresent())
            writeToFile();
        return res;
    }

    @Override
    public Optional<T> save(T entity) {
        Optional<T> res=super.save(entity);
        if(res.isEmpty())
            writeToFile();
        return res;
    }


}
