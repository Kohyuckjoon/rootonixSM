package com.planuri.rootonixsmartmirror.model.air;

import com.google.gson.annotations.SerializedName;

public class AirData {
    @SerializedName("response")
    public Response response;

    @Override
    public String toString() {
        return "AirData{" +
                "response=" + response +
                '}';
    }
}
