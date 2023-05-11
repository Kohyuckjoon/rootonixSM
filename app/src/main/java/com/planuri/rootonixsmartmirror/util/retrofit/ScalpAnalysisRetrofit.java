package com.planuri.rootonixsmartmirror.util.retrofit;

import com.planuri.rootonixsmartmirror.util.api.RestAIServer;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** 루토닉스 AI 서버와의 연결 및 요청 */
public class ScalpAnalysisRetrofit {
    private static final String TAG = ScalpAnalysisRetrofit.class.getSimpleName();

    private static final String BASE_URL = "http://ai.rootonix.com:5010/";

    private static ScalpAnalysisRetrofit INSTANCE;

    private Retrofit retrofit;
    private RestAIServer restAIServer;

    static public ScalpAnalysisRetrofit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ScalpAnalysisRetrofit();
        }
        return INSTANCE;
    };

    private ScalpAnalysisRetrofit() {

        OkHttpClient httpClient = new OkHttpClient().newBuilder() //인증서 무시
                .connectTimeout(10, TimeUnit.SECONDS) //연결 타임아웃 시간 설정
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create()) //note: JSON 컨버팅 할 수 있게 설정
                .baseUrl(BASE_URL)
                .build();

        restAIServer = retrofit.create(RestAIServer.class); //note: retrofit과 AI Server API 인터페이스 연결
    }

    public RestAIServer getAISvrInterface() {
        return restAIServer;
    }   // API Interface
}
