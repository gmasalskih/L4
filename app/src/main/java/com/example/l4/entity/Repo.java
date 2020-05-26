
package com.example.l4.entity;

import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("stargazers_count")
    public Integer stargazersCount;

    @SerializedName("watchers_count")
    public Integer watchersCount;

    @SerializedName("language")
    public String language;

    @SerializedName("forks_count")
    public Integer forksCount;
}
