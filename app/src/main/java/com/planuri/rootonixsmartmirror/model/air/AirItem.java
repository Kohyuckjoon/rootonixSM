package com.planuri.rootonixsmartmirror.model.air;

import com.google.gson.annotations.SerializedName;

public class AirItem {
//    @SerializedName("so2Grade")
//    public String so2Grade;
//    @SerializedName("coFlag")
//    public String coFlag;
//    @SerializedName("khaiValue")
//    public String khaiValue;
//    @SerializedName("so2Value")
//    public String so2Value;
//    @SerializedName("coValue")
//    public String coValue;
//    @SerializedName("pm25Flag")
//    public String pm25Flag;
//    @SerializedName("pm10Flag")
//    public String pm10Flag;
//    @SerializedName("o3Grade")
//    public String o3Grade;
    @SerializedName("pm10Value")
    public String pm10Value;
//    @SerializedName("khaiGrade")
//    public String khaiGrade;
//    @SerializedName("pm25Value")
//    public String pm25Value;
//    @SerializedName("sidoName")
//    public String sidoName;
//    @SerializedName("no2Flag")
//    public String no2Flag;
//    @SerializedName("no2Grade")
//    public String no2Grade;
//    @SerializedName("o3Flag")
//    public String o3Flag;
//    @SerializedName("pm25Grade")
//    public String pm25Grade;
//    @SerializedName("so2Flag")
//    public String so2Flag;
//    @SerializedName("dataTime")
//    public String dataTime;
//    @SerializedName("coGrade")
//    public String coGrade;
//    @SerializedName("no2Value")
//    public String no2Value;
    @SerializedName("stationName")
    public String stationName;
//    @SerializedName("pm10Grade")
//    public String pm10Grade;
//    @SerializedName("o3Value")
//    public String o3Value;

//    public String getSo2Grade() {
//        return so2Grade;
//    }
//
//    public void setSo2Grade(String so2Grade) {
//        this.so2Grade = so2Grade;
//    }
//
//    public String getCoFlag() {
//        return coFlag;
//    }
//
//    public void setCoFlag(String coFlag) {
//        this.coFlag = coFlag;
//    }
//
//    public String getKhaiValue() {
//        return khaiValue;
//    }
//
//    public void setKhaiValue(String khaiValue) {
//        this.khaiValue = khaiValue;
//    }
//
//    public String getSo2Value() {
//        return so2Value;
//    }
//
//    public void setSo2Value(String so2Value) {
//        this.so2Value = so2Value;
//    }
//
//    public String getCoValue() {
//        return coValue;
//    }
//
//    public void setCoValue(String coValue) {
//        this.coValue = coValue;
//    }
//
//    public String getPm25Flag() {
//        return pm25Flag;
//    }
//
//    public void setPm25Flag(String pm25Flag) {
//        this.pm25Flag = pm25Flag;
//    }
//
//    public String getPm10Flag() {
//        return pm10Flag;
//    }
//
//    public void setPm10Flag(String pm10Flag) {
//        this.pm10Flag = pm10Flag;
//    }
//
//    public String getO3Grade() {
//        return o3Grade;
//    }
//
//    public void setO3Grade(String o3Grade) {
//        this.o3Grade = o3Grade;
//    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

//    public String getKhaiGrade() {
//        return khaiGrade;
//    }
//
//    public void setKhaiGrade(String khaiGrade) {
//        this.khaiGrade = khaiGrade;
//    }
//
//    public String getPm25Value() {
//        return pm25Value;
//    }
//
//    public void setPm25Value(String pm25Value) {
//        this.pm25Value = pm25Value;
//    }
//
//    public String getSidoName() {
//        return sidoName;
//    }
//
//    public void setSidoName(String sidoName) {
//        this.sidoName = sidoName;
//    }
//
//    public String getNo2Flag() {
//        return no2Flag;
//    }
//
//    public void setNo2Flag(String no2Flag) {
//        this.no2Flag = no2Flag;
//    }
//
//    public String getNo2Grade() {
//        return no2Grade;
//    }
//
//    public void setNo2Grade(String no2Grade) {
//        this.no2Grade = no2Grade;
//    }
//
//    public String getO3Flag() {
//        return o3Flag;
//    }
//
//    public void setO3Flag(String o3Flag) {
//        this.o3Flag = o3Flag;
//    }
//
//    public String getPm25Grade() {
//        return pm25Grade;
//    }
//
//    public void setPm25Grade(String pm25Grade) {
//        this.pm25Grade = pm25Grade;
//    }
//
//    public String getSo2Flag() {
//        return so2Flag;
//    }
//
//    public void setSo2Flag(String so2Flag) {
//        this.so2Flag = so2Flag;
//    }
//
//    public String getDataTime() {
//        return dataTime;
//    }
//
//    public void setDataTime(String dataTime) {
//        this.dataTime = dataTime;
//    }
//
//    public String getCoGrade() {
//        return coGrade;
//    }
//
//    public void setCoGrade(String coGrade) {
//        this.coGrade = coGrade;
//    }
//
//    public String getNo2Value() {
//        return no2Value;
//    }
//
//    public void setNo2Value(String no2Value) {
//        this.no2Value = no2Value;
//    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

//    public String getPm10Grade() {
//        return pm10Grade;
//    }
//
//    public void setPm10Grade(String pm10Grade) {
//        this.pm10Grade = pm10Grade;
//    }
//
//    public String getO3Value() {
//        return o3Value;
//    }
//
//    public void setO3Value(String o3Value) {
//        this.o3Value = o3Value;
//    }


    @Override
    public String toString() {
        return "AirItem{" +
                "pm10Value='" + pm10Value + '\'' +
                ", stationName='" + stationName + '\'' +
                '}';
    }
}
