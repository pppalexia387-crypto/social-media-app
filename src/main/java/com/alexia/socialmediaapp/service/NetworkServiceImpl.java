package com.alexia.socialmediaapp.service;



import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.Tuple;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.repository.Repository;

import java.util.*;

public class NetworkServiceImpl implements NetworkService{
    private final Repository<Integer, UserProfile> profileRepo;
    private final Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo;

    public NetworkServiceImpl(Repository<Integer, UserProfile> profileRepo,
                              Repository<Tuple<Integer, Integer>, Friendship> friendshipRepo) {
        this.profileRepo = profileRepo;
        this.friendshipRepo = friendshipRepo;
    }

    private Map<Integer,List<Integer>> buildAdjacencyList(){
        Map<Integer, List<Integer>> adjList= new HashMap<>();
        for(UserProfile user : profileRepo.findAll()){
           adjList.put(user.getId(), new ArrayList<>());
        }

        for (Friendship f : friendshipRepo.findAll()) {
            if (adjList.containsKey(f.getUser1Id()) && adjList.containsKey(f.getUser2Id())) {
                adjList.get(f.getUser1Id()).add(f.getUser2Id());
                adjList.get(f.getUser2Id()).add(f.getUser1Id());
            }
        }
        return adjList;}

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

        private void dfs(Integer node, Set<Integer> visited, Map<Integer, List<Integer>> adjList) {
            visited.add(node);
            for (Integer neighbor : adjList.get(node)) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, visited, adjList);
                }
            }
        }

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

        private void extractCommunity(Integer node, Set<Integer> visited, Map<Integer, List<Integer>> adjList, List<Integer> community) {
            visited.add(node);
            community.add(node);
            for (Integer neighbor : adjList.get(node)) {
                if (!visited.contains(neighbor)) {
                    extractCommunity(neighbor, visited, adjList, community);
                }
            }
        }

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
