package com.alexia.socialmediaapp.service;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.repository.Repository;

import java.util.*;

/**
 * Implementation of the NetworkService interface.
 * Analyzes the social network graph to find connected communities and determine the most sociable community.
 */
public class NetworkServiceImpl implements NetworkService {
    private final Repository<Integer, UserProfile> profileRepo;
    private final Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo;

    /**
     * Constructs a NetworkServiceImpl with the required repositories.
     *
     * @param profileRepo the repository for retrieving user profiles
     * @param friendshipRepo the repository for retrieving friendships
     */
    public NetworkServiceImpl(Repository<Integer, UserProfile> profileRepo,
                              Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo) {
        this.profileRepo = profileRepo;
        this.friendshipRepo = friendshipRepo;
    }

    /**
     * Builds an adjacency list representing the social network graph.
     *
     * @return a map where each key is a user ID and the value is a list of their friends' user IDs
     */
    private Map<Integer, List<Integer>> buildAdjacencyList() {
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        for (UserProfile user : profileRepo.findAll()) {
            adjList.put(user.getId(), new ArrayList<>());
        }

        for (Friendship f : friendshipRepo.findAll()) {
            if (adjList.containsKey(f.getUser1Id()) && adjList.containsKey(f.getUser2Id())) {
                adjList.get(f.getUser1Id()).add(f.getUser2Id());
                adjList.get(f.getUser2Id()).add(f.getUser1Id());
            }
        }
        return adjList;
    }

    /**
     * Calculates the total number of connected communities within the social network.
     * A community is defined as a connected component in the friendship graph.
     *
     * @return the total number of distinct communities
     * @throws RepoException if a repository error occurs while retrieving user or friendship data
     */
    @Override
    public int getNumberOfCommunities() {
        Map<Integer, List<Integer>> adjList = buildAdjacencyList();
        Set<Integer> visited = new HashSet<>();
        int communityCount = 0;

        for (Integer userId : adjList.keySet()) {
            if (!visited.contains(userId)) {
                communityCount++;
                dfs(userId, visited, adjList);
            }
        }
        return communityCount;
    }

    /**
     * Performs a depth-first search to traverse a connected component.
     *
     * @param node the current user ID being visited
     * @param visited the set of already visited user IDs
     * @param adjList the adjacency list representing the network
     */
    private void dfs(Integer node, Set<Integer> visited, Map<Integer, List<Integer>> adjList) {
        visited.add(node);
        for (Integer neighbor : adjList.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, adjList);
            }
        }
    }

    /**
     * Identifies the most sociable community in the network.
     * The most sociable community is defined as the connected component containing the longest path.
     *
     * @return a list of user IDs representing the members of the most sociable community
     * @throws RepoException if a repository error occurs while retrieving user or friendship data
     */
    @Override
    public List<Integer> getMostSociableCommunity() {
        Map<Integer, List<Integer>> adjList = buildAdjacencyList();
        Set<Integer> visited = new HashSet<>();

        List<Integer> mostSociableCommunity = new ArrayList<>();
        int absoluteMaxPath = -1;

        for (Integer userId : adjList.keySet()) {
            if (!visited.contains(userId)) {
                List<Integer> currentCommunity = new ArrayList<>();
                extractCommunity(userId, visited, adjList, currentCommunity);

                int currentMaxPath = 0;
                for (Integer startNode : currentCommunity) {
                    int pathLength = longestPathFromNode(startNode, new HashSet<>(), adjList);
                    if (pathLength > currentMaxPath) {
                        currentMaxPath = pathLength;
                    }
                }

                if (currentMaxPath > absoluteMaxPath) {
                    absoluteMaxPath = currentMaxPath;
                    mostSociableCommunity = currentCommunity;
                }
            }
        }
        return mostSociableCommunity;
    }

    /**
     * Extracts all nodes belonging to a single connected component.
     *
     * @param node the starting user ID
     * @param visited the set of already visited user IDs
     * @param adjList the adjacency list representing the network
     * @param community the list storing the IDs of the extracted community members
     */
    private void extractCommunity(Integer node, Set<Integer> visited, Map<Integer, List<Integer>> adjList, List<Integer> community) {
        visited.add(node);
        community.add(node);
        for (Integer neighbor : adjList.get(node)) {
            if (!visited.contains(neighbor)) {
                extractCommunity(neighbor, visited, adjList, community);
            }
        }
    }

    /**
     * Calculates the longest path in a graph starting from a specific node using backtracking.
     *
     * @param node the starting user ID
     * @param pathVisited the set of user IDs visited in the current path
     * @param adjList the adjacency list representing the network
     * @return the length of the longest path starting from the given node
     */
    private int longestPathFromNode(Integer node, Set<Integer> pathVisited, Map<Integer, List<Integer>> adjList) {
        pathVisited.add(node);
        int maxDepth = 0;

        for (Integer neighbor : adjList.get(node)) {
            if (!pathVisited.contains(neighbor)) {
                int depth = longestPathFromNode(neighbor, pathVisited, adjList);
                if (depth > maxDepth) {
                    maxDepth = depth;
                }
            }
        }

        pathVisited.remove(node);
        return maxDepth + 1;
    }
}