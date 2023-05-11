package com.planuri.rootonixsmartmirror.util;

import android.util.Log;

import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomRestClient {

    private static final String TAG = CustomRestClient.class.getSimpleName();

//    private static final String BASE_URL = "http://api.rootonix.com:8080/";
    private static final String BASE_URL = "http://1.226.26.68:8080/";  //For Test
    private static CustomRestClient INSTANCE;

    private Retrofit retrofit;
    private RestApiInterface restApiInterface;

    static public CustomRestClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomRestClient();
        }
        return INSTANCE;
    };

    private CustomRestClient() {

        OkHttpClient httpClient = new OkHttpClient().newBuilder() //인증서 무시
                .connectTimeout(10, TimeUnit.SECONDS) //연결 타임아웃 시간 설정
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create()) //note: JSON 컨버팅 할 수 있게 설정
                .baseUrl(BASE_URL)
                .build();

        restApiInterface = retrofit.create(RestApiInterface.class); //note: retrofit과 REST API 인터페이스 연결
    }

    public RestApiInterface getApiInterface() {
        return restApiInterface;
    }   // API Interface

    /** Http code 500 오류 응답을 CommonResponse 로 변환 */
    public CommonResponse getErrorResponse(Response response) {

        Converter<ResponseBody, CommonResponse> errorConverter =
                retrofit.responseBodyConverter(CommonResponse.class, new Annotation[0]);

        CommonResponse errorResponse = new CommonResponse(false, -1, "오류 코드 변환 예외가 발생했습니다.");

        try {
            Log.e(TAG, response.errorBody().string());
            errorResponse = errorConverter.convert(response.errorBody());

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return errorResponse;
    }
}
