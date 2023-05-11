package com.planuri.rootonixsmartmirror.model.air;

import com.google.gson.annotations.SerializedName;

public class Header {
    @SerializedName("resultMsg")
    String resultMsg;
    @SerializedName("resultCode")
    String resultCode;

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "Header{" +
                "resultMsg='" + resultMsg + '\'' +
                ", resultCode='" + resultCode + '\'' +
                '}';
    }
}
