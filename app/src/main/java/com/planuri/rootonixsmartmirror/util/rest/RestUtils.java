package com.planuri.rootonixsmartmirror.util.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.planuri.rootonixsmartmirror.model.Weather;
import com.planuri.rootonixsmartmirror.util.AppTrace;
import com.planuri.rootonixsmartmirror.util.Const;
import com.planuri.rootonixsmartmirror.util.retrofit.NewsRetroService;

import java.util.Calendar;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestUtils {
    private Context mContext;
    private Retrofit mWeatherRetrofit;
    private Retrofit mApiRetrofit;

    private NewsRetroService mWeatherRequest;

    private RestUtils() {}

    /**
     *
     * @param context 메모리 누수 방지를 위해 getApplicationContext()를 사용해야 함
     */
    public void initialize(Context context) {
        mContext = context;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        mWeatherRetrofit = new Retrofit.Builder()
                .baseUrl(Const.WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mWeatherRequest = mWeatherRetrofit.create(NewsRetroService.class);
    }

    public void getNowWeather(Callback<Weather> weatherCallback){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        //1월 : 0
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DATE);
        //24시간제
        //정각에는 현재 시간 데이터를 조회할 수 없음
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        if(min < 30) {
            if(hour != 0) {
                hour -= 1;
            }
        }
        String base_date;
        if(month < 10) {
            base_date = "" + year + "0" + month;
        } else {
            base_date = "" + year + month;
        }

        if(date < 10) {
            base_date += "0" + date;
        } else {
            base_date += date;
        }

        String base_time;
        if (hour < 10){
            base_time = "" + "0" + hour + "00";
        } else {
            base_time = "" + hour + "00";
        }

        int nx = Const.REAL_X;
        int ny = Const.REAL_Y;
        mWeatherRequest.getNowWeather("JSON", base_date, base_time, nx, ny).enqueue(weatherCallback);
    }

    public void getWeather(Callback<Weather> weatherCallback) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        //1월 : 0
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DATE);
        //24시간제
        //정각에는 현재 시간 데이터를 조회할 수 없음
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        if(min < 45) {
            if(hour != 0) {
                hour -= 1;
            }
        }
        String base_date;
        if(month < 10) {
            base_date = "" + year + "0" + month;
        } else {
            base_date = "" + year + month;
        }

        if(date < 10) {
            base_date += "0" + date;
        } else {
            base_date += date;
        }

        String base_time;
        if (hour < 10){
            base_time = "" + "0" + hour + "30";
        } else {
            base_time = "" + hour + "30";
        }

        int nx = 55;
        int ny = 127;

        AppTrace.d("base_date : " + base_date + ", base_time : " + base_time + ", nx : " + nx + ", ny : " + ny);
        mWeatherRequest.getWeather("JSON", base_date, base_time, nx, ny).enqueue(weatherCallback);
    }

    public void getYesterdayTemp(Callback<Weather> weatherCallback){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        //1월 : 0
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DATE) - 1;
        //24시간제
        //정각에는 현재 시간 데이터를 조회할 수 없음
        int hour = now.get(Calendar.HOUR_OF_DAY) + 2;
        int min = now.get(Calendar.MINUTE);
        if(min < 45) {
            if(hour != 0) {
                hour -= 1;
            }
        }
        String base_date;
        if(month < 10) {
            base_date = "" + year + "0" + month;
        } else {
            base_date = "" + year + month;
        }

        if(date < 10) {
            base_date += "0" + date;
        } else {
            base_date += date;
        }

        String base_time;
        if (hour < 10){
            base_time = "" + "0" + hour + "30";
        } else {
            base_time = "" + hour + "30";
        }

        int nx = 55;
        int ny = 127;

        AppTrace.d("base_date : " + base_date + ", base_time : " + base_time + ", nx : " + nx + ", ny : " + ny);
        mWeatherRequest.getWeather("JSON", base_date, base_time, nx, ny).enqueue(weatherCallback);
    }

    public void getSky(Callback<Weather> weatherCallback) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        //1월 : 0
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DATE);
        //24시간제
        //정각에는 현재 시간 데이터를 조회할 수 없음
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        if(min < 45) {
            if(hour != 0) {
                hour -= 1;
            }
        }
        String base_date;
        if(month < 10) {
            base_date = "" + year + "0" + month;
        } else {
            base_date = "" + year + month;
        }

        if(date < 10) {
            base_date += "0" + date;
        } else {
            base_date += date;
        }

        String base_time;
        if (hour < 10){
            base_time = "" + "0" + hour + "30";
        } else {
            base_time = "" + hour + "30";
        }

        int nx = Const.REAL_X;
        int ny = Const.REAL_Y;

        AppTrace.d("base_date : " + base_date + ", base_time : " + base_time + ", nx : " + nx + ", ny : " + ny);
        mWeatherRequest.getSky("JSON", base_date, base_time, nx, ny).enqueue(weatherCallback);
    }

    public static RestUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final RestUtils INSTANCE = new RestUtils();
    }
}
