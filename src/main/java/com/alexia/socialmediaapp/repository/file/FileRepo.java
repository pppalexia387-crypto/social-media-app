package com.alexia.socialmediaapp.repository;

import com.alexia.socialmediaapp.domain.Entity;
import com.alexia.socialmediaapp.domain.validators.Validator;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ValidationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileRepo<ID,T extends Entity<ID>> extends InMemoryRepo<ID, T> {
    private final Function<String,T> entityExtractor;
    private final Function<T,String> entityFormator;
    private final String fileName;
    private final Validator<T> validator;

    public FileRepo(Function<String, T> entityExtractor, Function<T, String> entityFormator, String fileName, Validator<T> validator) {
        super();
        this.entityExtractor = entityExtractor;
        this.entityFormator = entityFormator;
        this.fileName = fileName;
        this.validator = validator;
        loadData();
    }
    public FileRepo(Function<String, T> entityExtractor, Function<T, String> entityFormator, String fileName, Validator<T> validator,Function<Set<ID>,ID> idFunction) {
        super(idFunction);
        this.entityExtractor = entityExtractor;
        this.entityFormator = entityFormator;
        this.fileName = fileName;
        this.validator = validator;
        loadData();
    }
    public void loadData() {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            return;
        }
        AtomicInteger lineNr= new AtomicInteger(1);
        try (Stream<String> lines = Files.lines(path)) {
                lines.map(line-> {
                    int nr=lineNr.getAndIncrement();
                    try{
                        T entity= entityExtractor.apply(line);
                        this.validator.validate(entity);
                        return entity;
                    }catch(ValidationException e){
                        throw new RepoException("Corrupted data on line " + nr + " in file " + fileName + ": " + e.getMessage(), e);
                    }

                }).forEach(super::save);

        }
        catch(IOException exp){
            throw new RepoException(exp.getMessage(),exp);
        }


    }
    public void writeToFile(){
        Path path = Path.of(fileName);
        if(!Files.exists(path)){
            try{Files.createFile(path);}
            catch (IOException e){
                throw new RepoException(e.getMessage(),e);}

        }
        Stream<String> lines = StreamSupport.stream(super.findAll().spliterator(), false).map(entityFormator);

        try{
            Files.write(path,(Iterable<String>) lines::iterator);
        }
        catch(IOException e){
            throw new RepoException("Failed to save data to file: " + this.fileName, e);
        }
    }

    @Override
    public Optional<T> save(T entity) {
        Optional<T> res=super.save(entity);
        if(res.isEmpty())
            writeToFile();
        return res;
    }

    @Override
    public Optional<T> delete(ID id) {
        Optional<T> res=super.delete(id);
        if(res.isPresent())
            writeToFile();
        return res;
    }

    @Override
    public Optional<T> update(T entity) {
        Optional<T> res=super.update(entity);
        if(res.isEmpty())
            writeToFile();
        return res;
    }
};
