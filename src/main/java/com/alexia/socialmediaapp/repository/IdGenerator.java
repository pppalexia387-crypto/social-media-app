package com.alexia.socialmediaapp.repository;

import java.util.Set;

public interface IdGenerator<ID>{
    void initialize(Set<ID> existingIds);
    ID getNextID();
}
