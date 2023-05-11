package com.planuri.rootonixsmartmirror.model.air;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("body")
    public Body body;
    @SerializedName("header")
    public Header header;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "Response{" +
                "body=" + body +
                ", header=" + header +
                '}';
    }
}
