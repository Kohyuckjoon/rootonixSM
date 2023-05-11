package com.planuri.rootonixsmartmirror.service;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.model.Weather;
import com.planuri.rootonixsmartmirror.util.AppTrace;
import com.planuri.rootonixsmartmirror.util.Const;
import com.planuri.rootonixsmartmirror.util.log.LoggerInterface;
import com.planuri.rootonixsmartmirror.util.rest.RestUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService implements LoggerInterface {
    private final int MSG_SKY = 100;
    private final int MSG_WEATHER = 101;
    private final int MSG_PRECIP = 102;
    //TODO : TEST
    private final long NET_REQ_DELAY = 1 * 10 * 1000; // 10 초

    private TextView tvTemp;
//    private ImageView ivWeather;
    private TextView tvWeatherText; //MainActivity 오른쪽 상단 Cloudy >> 날씨 상태 TextView
    private TextView tvSmWeatherText; //SmartFragMent 주간 날씨 상단(온도아래) >> 날씨 상태 Textview

    /**
     * SMART MODE에서 날씨 출력해줄 ID값 불러오기
     * */
    private ImageView ivSmWeatherIcon; //오늘 날씨 아이콘


    ArrayList<Weather.Item> sky = new ArrayList<>();
    Activity mActivity;
    Handler mHandler;

    public WeatherService(Activity activity){
//        this.tvTemp = activity.findViewById(R.id.tv_temp);
//        this.ivWeather = activity.findViewById(R.id.iv_weather);
//        this.tvWeatherText = activity.findViewById(R.id.tv_weather_text);
        this.mActivity = activity;

        mHandler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SKY:
                        initializeSkyImpl();
                        break;
                    case MSG_WEATHER:
                        initializeNowWeatherImpl();
                        break;
                }
            }
        };
    }

    public void initializeSky(){
        mHandler.removeMessages(MSG_SKY);
        mHandler.sendEmptyMessage(MSG_SKY);
    }

    public void initializeSky(long delay){
        mHandler.removeMessages(MSG_SKY);
        mHandler.sendEmptyMessageDelayed(MSG_SKY, delay);
    }

    private void initializeSkyImpl(){
        RestUtils.getInstance().getSky(getSkyStatus);
    }

    public void initializeNowWeather(){
        mHandler.removeMessages(MSG_WEATHER);
        mHandler.sendEmptyMessage(MSG_WEATHER);
    }

    public void initializeNowWeather(long delay){
        mHandler.removeMessages(MSG_WEATHER);
        mHandler.sendEmptyMessageDelayed(MSG_WEATHER, delay);
    }

    private void initializeNowWeatherImpl(){
        RestUtils.getInstance().getNowWeather(mNowWeatherCallback);
    }

    private final Callback<Weather> getSkyStatus = new Callback<Weather>() {
        @Override
        public void onResponse(Call<Weather> call, Response<Weather> response) {
            if(response.isSuccessful()) {
                response.body().response.header.parseResultCode();
                if(response.body().response.header.resultCode.equals("00")) {
                    try {
                        for (int i=0; i<response.body().response.body.items.item.size(); i++){
                            String category = response.body().response.body.items.item.get(i).category;
                            if (category.equals("SKY")){ sky.add(response.body().response.body.items.item.get(i)); }
                        }

                        initializeNowWeather();
                    } catch (Exception e) {
                        mLogger.error("getSky Fail : " + e.toString());
                        mLogger.error("getSky Fail : " + e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    AppTrace.e("통신에러 : " + response.body().response.header.resultMsg);
                    mLogger.error("통신에러 : " + response.body().response.header.resultMsg);
                    initializeSky(NET_REQ_DELAY);
                }

            } else {
                try {
                    AppTrace.e(response.errorBody().string());
                    initializeSky(NET_REQ_DELAY);
                    mLogger.error("Sky Callback Fail : "+response.errorBody().string());
                } catch (Exception e) {
                    mLogger.error("Sky Callback Fail : "+e.toString());
                    mLogger.error("Sky Callback Fail : "+e.getMessage());
                    AppTrace.e(e);
                    initializeSky(NET_REQ_DELAY);
                }
            }
        }

        @Override
        public void onFailure(Call<Weather> call, Throwable t) {
            AppTrace.e(t);
            initializeSky(NET_REQ_DELAY);
        }
    };

    private final Callback<Weather> mNowWeatherCallback = new Callback<Weather>() {
        @Override
        public void onResponse(Call<Weather> call, Response<Weather> response) {
            if(response.isSuccessful()) {
                response.body().response.header.parseResultCode();
                if(response.body().response.header.resultCode.equals("00")) {
                    response.body().response.body.parseData();
                    String temperatureStr = Math.round(response.body().response.body.T1H) + "℃";
                    Log.d("TAG", temperatureStr);
                    mLogger.info("TAG : " + temperatureStr);
                    StringBuilder skyStr =  new StringBuilder();
                    AppTrace.d("PTY : " + response.body().response.body.PTY);
                    AppTrace.d("SKY : " + Integer.parseInt(sky.get(0).fcstValue));
                    mLogger.info("PTY : " + response.body().response.body.PTY);
                    mLogger.info("SKY : " + Integer.parseInt(sky.get(0).fcstValue));

                    switch (response.body().response.body.PTY) {
                        case Const.PRECIPITATION_GOOD :
                            switch (Integer.parseInt(sky.get(0).fcstValue)) {
                                case Const.SKY_CLOUDY :
                                    skyStr.append("구름 많음");
//                                    ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_vcloudy_background_dark));
//                                    tvWeatherText.setText("" + "Cloudy");
                                    break;
                                case Const.SKY_GRAY :
                                    skyStr.append("흐림");
//                                    ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_cloudy_background_dark));
//                                    tvWeatherText.setText("" + "Cloudy");
                                    break;
                                case Const.SKY_GOOD:
                                    skyStr.append("맑음");
//                                    ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_sunny_background_dark));
//                                    tvWeatherText.setText("" + "Sunny");
                                    break;
                            }
                            break;
                        case Const.PRECIPITATION_RAIN :
                            skyStr.append("비");
//                            ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_rain_background_dark));
//                            tvWeatherText.setText("" + "Rain");
                            break;
                        case Const.PRECIPITATION_RAIN_SNOW:
                            skyStr.append("진눈개비");
//                            ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_snow_background_dark));
//                            tvWeatherText.setText("" + "Snowing");
                            break;
                        case Const.PRECIPITATION_SNOW:
                            skyStr.append("눈");
//                            ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_snow_background_dark));
//                            tvWeatherText.setText("" + "Snow");
                            break;
                        case Const.PRECIPITATION_SHOWER:
                            skyStr.append("소나기");
//                            ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_rain_background_dark));
//                            tvWeatherText.setText("" + "Rain");
                            break;
                        case Const.PRECIPITATION_FINE_RAIN:
                            skyStr.append("가랑비");
//                            ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_rain_background_dark));
//                            tvWeatherText.setText("" + "Rain");
                            break;
                        case Const.PRECIPITATION_SLEET:
                            skyStr.append("가랑눈/비");
//                            ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_rain_background_dark));
//                            tvWeatherText.setText("" + "Rain");
                            break;
                        case Const.PRECIPITATION_FINE_SNOW:
                            skyStr.append("가랑눈");
//                            ivWeather.setImageDrawable(ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.weather_snow_background_dark));
//                            tvWeatherText.setText("" + "Snow");
                            break;
                    }
//                    tvTemp.setText(temperatureStr);

                } else {
                    AppTrace.e("통신에러 : " + response.body().response.header.resultMsg);
                    initializeNowWeather(NET_REQ_DELAY);
                }

            } else {
                try {
                    AppTrace.e(response.errorBody().string());
                    initializeNowWeather(NET_REQ_DELAY);
                } catch (Exception e) {
                    AppTrace.e(e);
                }
            }
        }

        @Override
        public void onFailure(Call<Weather> call, Throwable t) {
            AppTrace.e(t);
            initializeNowWeather(NET_REQ_DELAY);
        }
    };
}