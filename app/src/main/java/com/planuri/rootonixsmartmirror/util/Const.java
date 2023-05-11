package com.planuri.rootonixsmartmirror.util;

public class Const {
//    public static final String WATER_URL = "https://61.75.178.52:28088/";
    public static final String WATER_URL = "http://210.97.42.250:8088/"; //테스트 URL
    public static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/";
    public static final String NEWS_URL = "http://news.planuri.co.kr:20000/api/";
    public static final String AIR_URL = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/";
    public static final long HTTP_CONNECT_TIMEOUT = 10L;
    public static final long HTTP_WRITE_TIMEOUT = 10L;
    public static final long HTTP_READ_TIMEOUT = 30L;

    public static final String TAG = "PLNU";

    public static final String ID = "dev@planuri.co.kr";
    public static final String PW = "planuri01!";

    public static final int SKY_GOOD = 1;
    public static final int SKY_CLOUDY = 3;
    public static final int SKY_GRAY = 4;
    public static final int PRECIPITATION_GOOD = 0;
    public static final int PRECIPITATION_RAIN = 1;
    public static final int PRECIPITATION_RAIN_SNOW = 2;
    public static final int PRECIPITATION_SNOW = 3;
    public static final int PRECIPITATION_SHOWER = 4;
    public static final int PRECIPITATION_FINE_RAIN = 5;
    public static final int PRECIPITATION_SLEET = 6;
    public static final int PRECIPITATION_FINE_SNOW = 7;

    public static final String COMMUNITY_CENTER_SENSOR = "WFIS_MEAS.DF01";
    public static final String RIVER_SENSOR_1 = "WFIS_MEAS.RV01";
    public static final String RIVER_SENSOR_2 = "WFIS_MEAS.RV02";

    //부산광역시 강서구 강동동
    public static final int REAL_X = 95;
    public static final int REAL_Y = 76;
}