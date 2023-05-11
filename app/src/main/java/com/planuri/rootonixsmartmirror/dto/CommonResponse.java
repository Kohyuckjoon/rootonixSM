package com.planuri.rootonixsmartmirror.dto;

/** 실패 / 성공 응답 객체 */
public class CommonResponse {

    public boolean success;
    public int code;
    public String msg;

    public CommonResponse(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
    public CommonResponse() {}
}
