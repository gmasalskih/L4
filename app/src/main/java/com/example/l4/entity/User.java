package com.example.l4.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    public String login;

    @SerializedName("avatar_url")
    public String avatarUrl;
}