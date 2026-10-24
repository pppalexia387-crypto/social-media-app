package com.alexia.socialmediaapp.service;

import java.util.List;

public interface NetworkService {
    /**
     * Calculates the number of connected components in the graph.
     */
    int getNumberOfCommunities();
    /**
     * Finds the most sociable community.
     * Returns a list of the User IDs in that specific community.
     */
    List<Integer> getMostSociableCommunity();

}
