
package com.example.l4.entity;

import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("stargazers_count")
    public String stargazersCount;

    @SerializedName("watchers_count")
    public String watchersCount;

    @SerializedName("language")
    public String language;

    @SerializedName("forks_count")
    public String forksCount;

    @SerializedName("html_url")
    public String htmlUrl;
}