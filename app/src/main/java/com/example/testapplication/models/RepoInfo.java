package com.example.testapplication.models;

/**
 * Created by vlad on 11/30/16.
 */

public class RepoInfo {
    private long id;
    private String name;
    private String language;
    private String description;
    private RepoOwner owner;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public RepoOwner getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }
}
