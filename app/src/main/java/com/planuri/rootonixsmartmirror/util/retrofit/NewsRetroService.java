package com.planuri.rootonixsmartmirror.util.retrofit;

import android.util.Xml;

import com.google.gson.JsonObject;
import com.planuri.rootonixsmartmirror.model.News;
import com.planuri.rootonixsmartmirror.model.Weather;
import com.planuri.rootonixsmartmirror.model.water.AuthRequire;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsRetroService {
    @POST("v1.1/main/action/itaUserLogin")
    @Headers({"Content-Type:application/json", "X-CSRF-TOKEN:UL"})
    Call<JsonObject> auth(@Body AuthRequire authRequire);

    //TODO : 실 데이터 수집 후 시간도 @Query를 사용해 넣을 것
    @GET("v1.0/sites/{siteId}/things/WFIS_MEAS.RV01/attrs")
    @Headers("Content-Type:application/json")
    Call<JsonObject> getWaterStatus(@Header("X-CSRF-TOKEN")String token,
                                    @Path("siteId")String siteId,
                                    @Query("timeForm")String timeForm);

    @GET("getUltraSrtFcst?serviceKey=XTLp37nIxVOvqHpdzKg1V9yPyt2G8sg2BuNmn17ROmMJxe9FHbpin6UzkwTwC9A6RGFJS%2Bg6igMUU7IWjB1GBw%3D%3D&pageNo=-&numOfRows=100")
    Call<Weather> getWeather(@Query("dataType") String dataType,
                             @Query("base_date") String base_date, @Query("base_time") String base_time,
                             @Query("nx") int nx, @Query("ny") int ny);

    @GET("getUltraSrtNcst?serviceKey=XTLp37nIxVOvqHpdzKg1V9yPyt2G8sg2BuNmn17ROmMJxe9FHbpin6UzkwTwC9A6RGFJS%2Bg6igMUU7IWjB1GBw%3D%3D")
    Call<Weather> getNowWeather(@Query("dataType") String dataType,
                                @Query("base_date") String base_date, @Query("base_time") String base_time,
                                @Query("nx") int nx, @Query("ny") int ny);

    @GET("getAreaRiseSetInfo?serviceKey=XTLp37nIxVOvqHpdzKg1V9yPyt2G8sg2BuNmn17ROmMJxe9FHbpin6UzkwTwC9A6RGFJS%2Bg6igMUU7IWjB1GBw%3D%3D")
    Call<Xml> getSunSetRise(@Query("locdate") String locdate, @Query("locdate") String location);

    @GET("news")
    Call<ArrayList<News>> getNews();

    @GET("getCtprvnRltmMesureDnsty?serviceKey=XTLp37nIxVOvqHpdzKg1V9yPyt2G8sg2BuNmn17ROmMJxe9FHbpin6UzkwTwC9A6RGFJS%2Bg6igMUU7IWjB1GBw%3D%3D&returnType=json&numOfRows=100&pageNo=1&sidoName=부산&ver=1.0")
    Call<JsonObject> getAirPollution();

    @GET("getUltraSrtFcst?serviceKey=XTLp37nIxVOvqHpdzKg1V9yPyt2G8sg2BuNmn17ROmMJxe9FHbpin6UzkwTwC9A6RGFJS%2Bg6igMUU7IWjB1GBw%3D%3D&pageNo=-&numOfRows=100")
    Call<Weather> getSky(@Query("dataType") String dataType,
                         @Query("base_date") String base_date, @Query("base_time") String base_time,
                         @Query("nx") int nx, @Query("ny") int ny);

}