package com.example.testapplication.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vlad on 11/30/16.
 */

public class RepoOwner {
    private long id;
    private String login;

    @SerializedName("avatar_url")
    private String avatarPath;

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarPath() {
        return avatarPath;
    }
}
