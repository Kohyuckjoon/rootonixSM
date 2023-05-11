package com.planuri.rootonixsmartmirror.model.water;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("offset")
    public int offset;
    @SerializedName("limit")
    public int limit;
    @SerializedName("total")
    public int total;

    @Override
    public String toString() {
        return "Pagination{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", total=" + total +
                '}';
    }
}
