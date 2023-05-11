package com.planuri.rootonixsmartmirror.dto;

/** 에코 테스트용 객체 */
public class TestDto {
    public boolean success;
    public int code;
    public String msg;

    public MyData data;

    public class MyData {
        public String msg;
    }

    public String toString() {
        return "{\n" +
                "\t\"success\":" + success + "," +
                "\t\"code\":" + code + "," +
                "\t\"msg\":" + msg + "," +
                "\t\"data\": {\n" +
                "\t\t\"msg\":" + data.msg +
                "\t}" +
                "\n}";
    }
}
