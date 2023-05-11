package com.planuri.rootonixsmartmirror.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("id")
    public String id;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("title")
    public String title;
    @SerializedName("date")
    public String date;
    @SerializedName("description")
    public String description;
    @SerializedName("context")
    public String context;
    @SerializedName("newsUrl")
    public String newsUrl;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
