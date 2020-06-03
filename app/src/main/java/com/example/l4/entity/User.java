package com.example.l4.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user", primaryKeys = {"login"})
public class User {
    @ColumnInfo(name = "login")
    @SerializedName("login")
    @NonNull
    public String login;

    @ColumnInfo(name = "avatar_url")
    @SerializedName("avatar_url")
    public String avatarUrl;
}