package com.planuri.rootonixsmartmirror.model.water;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("attrValue")
    public String attrValue;
    @SerializedName("createdAt")
    public String createdAt;

    @Override
    public String toString() {
        return "Data{" +
                "attrValue='" + attrValue + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
