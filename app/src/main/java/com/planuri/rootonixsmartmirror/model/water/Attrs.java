package com.planuri.rootonixsmartmirror.model.water;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Attrs {
    @SerializedName("attrKey")
    public String attrKey;
    @SerializedName("attrValues")
    public ArrayList<Data> attrValues;

    @Override
    public String toString() {
        return "Attrs{" +
                "attrKey='" + attrKey + '\'' +
                ", attrValues=" + attrValues +
                '}';
    }
}
