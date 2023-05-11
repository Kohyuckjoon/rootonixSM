package com.planuri.rootonixsmartmirror.model.water;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WaterData {
    @SerializedName("thingName")
    public String thingName;
    @SerializedName("attrs")
    public ArrayList<Attrs> attrs;
    @SerializedName("pagination")
    public Pagination pagination;

    @Override
    public String toString() {
        return "Response{" +
                "thingName='" + thingName + '\'' +
                ", attrs=" + attrs +
                ", pagination=" + pagination +
                '}';
    }
}