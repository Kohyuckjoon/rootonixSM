package com.planuri.rootonixsmartmirror.util.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.planuri.rootonixsmartmirror.BuildConfig;
import com.planuri.rootonixsmartmirror.util.Const;
import com.planuri.rootonixsmartmirror.util.gson_converter.DateDeserializer;
import com.planuri.rootonixsmartmirror.util.gson_converter.DateSerializer;
import com.planuri.rootonixsmartmirror.util.gson_converter.DateTimeDeserializer;
import com.planuri.rootonixsmartmirror.util.gson_converter.DateTimeSerializer;
import com.planuri.rootonixsmartmirror.util.gson_converter.TimeDeserializer;
import com.planuri.rootonixsmartmirror.util.gson_converter.TimeSerializer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRetrofit {
    private static NewsRetrofit INSTANCE;

    private NewsRetroService mService;

    public static NewsRetroService getNewsRetroInstance(){
        if (INSTANCE == null) {
            INSTANCE = new NewsRetrofit();
        }
        return INSTANCE.mService;
    };

    private NewsRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE
        );

        OkHttpClient httpClient = new OkHttpClient().newBuilder() //인증서 무시
//                .connectTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS) //연결 타임아웃 시간 설정
//                .writeTimeout(Constants.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(Constants.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Const.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS) //연결 타임아웃 시간 설정
                .writeTimeout(Const.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Const.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTimeDeserializer())
                .registerTypeAdapter(Date.class, new DateTimeSerializer())
                .registerTypeAdapter(java.sql.Date.class, new DateDeserializer())
                .registerTypeAdapter(java.sql.Date.class, new DateSerializer())
                .registerTypeAdapter(java.sql.Time.class, new TimeDeserializer())
                .registerTypeAdapter(java.sql.Time.class, new TimeSerializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Const.NEWS_URL)
                .build();

        mService = retrofit.create(NewsRetroService.class);
    }
}
