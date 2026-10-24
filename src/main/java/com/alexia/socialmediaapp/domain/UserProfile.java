package com.alexia.socialmediaapp.domain;

public class UserProfile extends Entity<Integer> {
    private String name;
    private String userName;
    private String bio;
    private String avatarUrl;
    private boolean isPrivate;
    private boolean isVerified;

    private int followersCount;
    private int followingCount;
    private int postsCount;

    public UserProfile(String name, String userName) {
        this.name = name;
        this.userName = userName;
        this.bio="";
    }

    public UserProfile(String name, String userName, String bio, String avatarUrl, boolean isPrivate, boolean isVerified, int followersCount, int followingCount, int postsCount) {
        this.name = name;
        this.userName = userName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.isPrivate = isPrivate;
        this.isVerified = isVerified;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.postsCount = postsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }


    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }


}
