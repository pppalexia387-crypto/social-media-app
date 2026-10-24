package com.alexia.socialmediaapp.domain;

import java.time.Instant;

public class UserAccount extends Entity<Integer> {
    private String email;
    private String phoneNr;
    private String passwordHash;
    private boolean mfaEnabled;
    private final Instant createdAt;

    public UserAccount(String email, String phoneNr, String passwordHash) {
        this.email = email;
        this.phoneNr = phoneNr;
        this.passwordHash = passwordHash;
        this.mfaEnabled = false;
        this.createdAt = Instant.now();
    }
    public UserAccount(String email, String phoneNr, String passwordHash, boolean mfaEnabled, Instant createdAt) {
        this.email = email;
        this.phoneNr = phoneNr;
        this.passwordHash = passwordHash;
        this.mfaEnabled = mfaEnabled;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
