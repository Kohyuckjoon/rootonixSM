package com.planuri.rootonixsmartmirror.model.water;

import com.google.gson.annotations.SerializedName;

public class AuthRequire {
    @SerializedName("userId")
    public String userId;
    @SerializedName("userPassword")
    public String userPassword;

    public AuthRequire(){};

    public AuthRequire(String userId, String userPassword){
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "AuthRequire{" +
                "userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
