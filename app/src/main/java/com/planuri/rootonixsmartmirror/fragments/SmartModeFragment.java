package com.planuri.rootonixsmartmirror.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.model.News;
import com.planuri.rootonixsmartmirror.model.SendWeatherListener;
import com.planuri.rootonixsmartmirror.service.WeatherService;
import com.planuri.rootonixsmartmirror.util.Consts;
import com.planuri.rootonixsmartmirror.util.PlanuriHttp;
import com.planuri.rootonixsmartmirror.util.Utils;
import com.planuri.rootonixsmartmirror.util.retrofit.NewsRetrofit;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmartModeFragment#} factory method to
 * create an instance of this fragment.
 */
public class SmartModeFragment extends BaseFragment {

    final String TAG = SmartModeFragment.class.getSimpleName();
    static final Logger log = LoggerFactory.getLogger( Consts.LOG_TIMEBASED );

    /* 날씨 코드에 따른 아이콘 분류 맵
    https://openweathermap.org/weather-conditions 참조 */
    static Map<String, Integer> m_mpWeatherIcons = null;
    static Map<String, Integer> m_mpWeatherIcons_main = null;
    static Map<String, RadioButton> m_mpDustIcons;

    List<WeatherItem> m_arrWeatherItem = new ArrayList<>();

    /*오늘의 날씨 표현 View*/
    ImageView smart_weather_main = null;
    TextView tv_temp_status;
    TextView tv_temp_weather_text;
    TextView wind_direction_info;

    /*주간 날씨 iv*/
    ImageView weather_icon1 = null;
    ImageView weather_icon2 = null;
    ImageView weather_icon3 = null;
    ImageView weather_icon4 = null;

    /*날짜 tv*/
    TextView week_date1_1;
    TextView week_date2_1;
    TextView week_date3_1;
    TextView week_date4_1;

    /*요일 tv*/
    TextView week_date1_2;
    TextView week_date2_2;
    TextView week_date3_2;
    TextView week_date4_2;

    /*최저 온도 tv*/
    TextView week_date1_3;
    TextView week_date2_3;
    TextView week_date3_3;
    TextView week_date4_3;

    /*최고 온도 tv*/
    TextView week_date1_4;
    TextView week_date2_4;
    TextView week_date3_4;
    TextView week_date4_4;

    /*주간 강수 확률 표시*/
    TextView week_date1_6;
    TextView week_date2_6;
    TextView week_date3_6;
    TextView week_date4_6;

    /*오늘의 최저 온도 tv*/
    TextView low_text2;

    /*오늘의 최고 온도 tv*/
    TextView high_text2;

    /*오늘의 강수량*/
    TextView fail_info;

    /*오늘의 습도*/
    TextView precipitation_info;


    ImageView m_imgTodayIcon        = null;
    TextView m_txtTodayTemperature  = null;
    TextView m_txtTodayRainRate     = null;

    ListView m_lstWeatherDow = null;
    WeatherDOWListAdapter m_lstWeatherDowAdapter = null;

    RadioButton m_imgDustGood     = null;
    RadioButton m_imgDustNormal   = null;
    RadioButton m_imgDustBad      = null;
    RadioButton m_imgDustVeryBad  = null;

    ProgressBar m_prgDustMeter = null;

    //MainActivity 전달
    SendWeatherListener sendWeatherListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sendWeatherListener = (SendWeatherListener) context;
    }

    /*메인 날씨*/
    static {
        m_mpWeatherIcons_main = new HashMap<>();
        m_mpWeatherIcons_main.put("01d", R.drawable.weather_sunny); m_mpWeatherIcons_main.put("02d", R.drawable.weather_cloudy);
        m_mpWeatherIcons_main.put("03d", R.drawable.weather_very_cloudy); m_mpWeatherIcons_main.put("04d", R.drawable.weather_very_cloudy);
        m_mpWeatherIcons_main.put("09d", R.drawable.weather_rain); m_mpWeatherIcons_main.put("10d", R.drawable.weather_rain);
        m_mpWeatherIcons_main.put("11d", R.drawable.weather_lightning); m_mpWeatherIcons_main.put("13d", R.drawable.weather_snow);
        m_mpWeatherIcons_main.put("50d", R.drawable.weather_icon_61); m_mpWeatherIcons_main.put("01n", R.drawable.weather_sunny);
        m_mpWeatherIcons_main.put("02n", R.drawable.weather_cloudy); m_mpWeatherIcons_main.put("03n", R.drawable.weather_very_cloudy);
        m_mpWeatherIcons_main.put("04n", R.drawable.weather_very_cloudy); m_mpWeatherIcons_main.put("09n", R.drawable.weather_rain);
        m_mpWeatherIcons_main.put("10n", R.drawable.weather_rain); m_mpWeatherIcons_main.put("11n", R.drawable.weather_lightning);
        m_mpWeatherIcons_main.put("13n", R.drawable.weather_snow); m_mpWeatherIcons_main.put("50n", R.drawable.weather_icon_61);
    }

    /*주간 날씨*/
    static {
        m_mpWeatherIcons = new HashMap<>();
        m_mpWeatherIcons.put("01d", R.drawable.weather_icon_week_1); m_mpWeatherIcons.put("02d", R.drawable.weather_icon_week_2);
        m_mpWeatherIcons.put("03d", R.drawable.weather_icon_week_3); m_mpWeatherIcons.put("04d", R.drawable.weather_icon_week_3);
        m_mpWeatherIcons.put("09d", R.drawable.weather_icon_week_4); m_mpWeatherIcons.put("10d", R.drawable.weather_icon_week_4);
        m_mpWeatherIcons.put("11d", R.drawable.weather_icon_week_6); m_mpWeatherIcons.put("13d", R.drawable.weather_icon_week_4);
        m_mpWeatherIcons.put("50d", R.drawable.weather_icon_61); m_mpWeatherIcons.put("01n", R.drawable.weather_icon_week_1);
        m_mpWeatherIcons.put("02n", R.drawable.weather_icon_week_2); m_mpWeatherIcons.put("03n", R.drawable.weather_icon_week_3);
        m_mpWeatherIcons.put("04n", R.drawable.weather_icon_week_3); m_mpWeatherIcons.put("09n", R.drawable.weather_icon_week_4);
        m_mpWeatherIcons.put("10n", R.drawable.weather_icon_week_4); m_mpWeatherIcons.put("11n", R.drawable.weather_icon_week_6);
        m_mpWeatherIcons.put("13n", R.drawable.weather_icon_week_5); m_mpWeatherIcons.put("50n", R.drawable.weather_icon_61);
    }
    private String m_strLatitude;
    private String m_strLongitude;
    private String m_strIpAddr;
    private String m_strPMGrade;

    <T extends View> T findViewById(@IdRes int resId) { return getView().findViewById(resId); }

    Handler m_Handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    /** new title */
    private TextView news1;
    private TextView news2;
    private TextView news3;
    private TextView news4;
    private TextView news5;
    private TextView news6;
    private TextView news7;
    private TextView news8;

    private TextView tvTemp;

    private VideoView mMainVideo;

    public SmartModeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_smart_mode, container, false);

        /** news id값 받아오기 */
        news1 = view.findViewById(R.id.tv_news_1);
        news2 = view.findViewById(R.id.tv_news_2);
        news3 = view.findViewById(R.id.tv_news_3);
        news4 = view.findViewById(R.id.tv_news_4);
        news5 = view.findViewById(R.id.tv_news_5);
        news6 = view.findViewById(R.id.tv_news_6);
        news7 = view.findViewById(R.id.tv_news_7);
        news8 = view.findViewById(R.id.tv_news_8);

        requestIpAddress();
        requestWeatherInfo();

        return view;
    }

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_daily, container, false);
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * 주간 날씨 작업하기
         * */
        WeatherService weatherService; //날씨 받아오기
        weatherService = new WeatherService(this.getActivity());
        weatherService.initializeSky();

        /**
         * 주간 날씨(오늘)
         */
        smart_weather_main = findViewById(R.id.smart_weather_main);
        tv_temp_status = findViewById(R.id.tv_temp_status);
        tv_temp_weather_text = findViewById(R.id.tv_temp_weather_text);
        wind_direction_info = findViewById(R.id.wind_direction_info);


        /**
         * 주간 날씨
         * 다음날~4일치
         */
        weather_icon1 = findViewById(R.id.weather_icon1);
        weather_icon2 = findViewById(R.id.weather_icon2);
        weather_icon3 = findViewById(R.id.weather_icon3);
        weather_icon4 = findViewById(R.id.weather_icon4);

        /** 날짜*/
        week_date1_1 = findViewById(R.id.week_date1_1);
        week_date2_1 = findViewById(R.id.week_date2_1);
        week_date3_1 = findViewById(R.id.week_date3_1);
        week_date4_1 = findViewById(R.id.week_date4_1);

        /** 요일표시*/
        week_date1_2 = findViewById(R.id.week_date1_2);
        week_date2_2 = findViewById(R.id.week_date2_2);
        week_date3_2 = findViewById(R.id.week_date3_2);
        week_date4_2 = findViewById(R.id.week_date4_2);

        /** 최저온도 표시*/
        week_date1_3 = findViewById(R.id.week_date1_3);
        week_date2_3 = findViewById(R.id.week_date2_3);
        week_date3_3 = findViewById(R.id.week_date3_3);
        week_date4_3 = findViewById(R.id.week_date4_3);

        /** 최고온도 표시*/
        week_date1_4 = findViewById(R.id.week_date1_4);
        week_date2_4 = findViewById(R.id.week_date2_4);
        week_date3_4 = findViewById(R.id.week_date3_4);
        week_date4_4 = findViewById(R.id.week_date4_4);

        /** 강수확률 표시*/
        week_date1_6 = findViewById(R.id.week_date1_6);
        week_date2_6 = findViewById(R.id.week_date2_6);
        week_date3_6 = findViewById(R.id.week_date3_6);
        week_date4_6 = findViewById(R.id.week_date4_6);

        /**오늘의 최저 온도 tv*/
        low_text2 = findViewById(R.id.low_text2);
        /**오늘의 최고 온도 tv*/
        high_text2 = findViewById(R.id.high_text2);
        /**오늘의 강수량*/
        fail_info = findViewById(R.id.fail_info);
        /**오늘의 습도*/
        precipitation_info = findViewById(R.id.precipitation_info);

        m_imgTodayIcon = findViewById(R.id.img_today_weather_icon);
        m_txtTodayTemperature = findViewById(R.id.txt_today_temperature);
        m_txtTodayRainRate = findViewById(R.id.txt_today_rain);

//        m_lstWeatherDow = findViewById(R.id.lst_weathers);
        m_lstWeatherDowAdapter = new WeatherDOWListAdapter();
//        m_lstWeatherDow.setAdapter(m_lstWeatherDowAdapter);
        m_lstWeatherDowAdapter.notifyDataSetChanged();

        m_imgDustGood    = findViewById(R.id.img_dust_good);
        m_imgDustNormal  = findViewById(R.id.img_dust_normal);
        m_imgDustBad     = findViewById(R.id.img_dust_bad);
        m_imgDustVeryBad = findViewById(R.id.img_dust_verybad);
        m_mpDustIcons = new HashMap<>();
        m_mpDustIcons.put("1", m_imgDustGood);
        m_mpDustIcons.put("2", m_imgDustNormal);
        m_mpDustIcons.put("3", m_imgDustBad);
        m_mpDustIcons.put("4", m_imgDustVeryBad);

        m_prgDustMeter = findViewById(R.id.prg_dust_level);

        /** 사용법 동영상 설정 및 리스너 등록 */
        mMainVideo = (VideoView) view.findViewById(R.id.mainVideoView);

        // 사용법 동영상 URI
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.howtouse3);
        mMainVideo.setVideoURI(uri);

        //동영상 준비 완료 리스너
        mMainVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // 준비 완료되면 비디오 재생
                mediaPlayer.start();
            }
        });

        //동영상 재상 완료 리스너
        mMainVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)  {
                //동영상 완료되면 다시 처음부터 재생
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });

        initializeData();
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(cm.getActiveNetworkInfo().isConnected()){
                        requestIpAddress();
                        return;
                    }

                    try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }).start();
    }

    void requestIpAddress(){
        Log.d(TAG, "requestIpAddress");
        new PlanuriHttp(getActivity())
                .setUrl("https://api.ipify.org/?format=json")
                .setRequestMethod(PlanuriHttp.METHOD_GET)
                .setListener(new PlanuriHttp.APIListener() {
                    @Override
                    public void onSuccess(String res) {
                        try {
                            JSONObject jsnRoot = new JSONObject(res);
                            if(jsnRoot.has("ip")){
                                m_strIpAddr = jsnRoot.getString("ip");
                                System.out.println("m_strIpAddr >>> " + m_strIpAddr);
//                            requestGPSInfomation();
                                requestWeatherInfo();
                            } else {

                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                    }

                    @Override public void onFailed(Exception e, Map map) { }
                }).execute();
    }

    void requestGPSInfomation(){
        if(m_strLatitude != null && m_strLongitude != null){
        } else {
            new PlanuriHttp(getActivity())
                    .setRequestMethod(PlanuriHttp.METHOD_GET)
                    .setUrl("http://api.ipstack.com/"+m_strIpAddr+"?access_key=242756548eebb9cbe54afdb99426c6b8")
                    .setListener(new PlanuriHttp.APIListener() {
                        @Override
                        public void onSuccess(String res) {
                            final String lat = "latitude";
                            final String lon = "longitude";
                            try {
                                JSONObject jsnRoot = new JSONObject(res);
                                if(jsnRoot.has(lat) && jsnRoot.has(lon)){
                                    m_strLatitude = jsnRoot.getString(lat);
                                    m_strLongitude= jsnRoot.getString(lon);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while(true){
                                                requestWeatherInfo();
//                                                requestDustInfo();
                                                Utils.threadSleep(3600 * 1000);
                                            }
                                        }
                                    }).start();
                                } else { }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(Exception e, Map map) { }
                    }).execute();
        }
    }

    private void requestDustInfo() {
        new PlanuriHttp(getActivity())
                .setRequestMethod(PlanuriHttp.METHOD_GET)
                .setUrl("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?" +
                        "serviceKey=aV8MEeX%2FCFj2gqdHvs%2FmQihLygVsSRAG8ygLGKtt%2BRnxiY8XetnS5YHLpKPFIOF1vmyZKlcwNIViUXw61QlK0A%3D%3D" +
                        "&numOfRows=1"+
                        "&pageNo=1"+
                        "&ver=1.3"+
                        "&dataTerm=DAILY"+
                        "&stationName=용산구")
                .setListener(new PlanuriHttp.APIListener() {
                    @Override
                    public void onSuccess(String res) {
                        Log.d(TAG, res);
                        SAXBuilder saxBuilder = new SAXBuilder();
                        InputStream input = null;
                        org.jdom2.Document document = null;
                        try {
                            input = new ByteArrayInputStream(res.getBytes("UTF-8"));
                            document = saxBuilder.build(input);
                        } catch (JDOMException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        org.jdom2.Element root = document.getRootElement();
                        String resCode = root.getChild("header").getChild("resultCode").getText();
                        if(resCode.equals("00")){
                            m_strPMGrade = root.getChild("body").getChild("items").getChild("item").getChild("pm25Grade").getText();
                            if(m_strPMGrade != null && m_mpDustIcons.containsKey(m_strPMGrade)){
                                Log.d(TAG, "미세먼지 아이콘 적용");
                                for(Map.Entry<String, RadioButton> e : m_mpDustIcons.entrySet()){
                                    e.getValue().setSelected(false);
                                }
                                m_mpDustIcons.get(m_strPMGrade).setSelected(true);
                                m_prgDustMeter.setProgress(Integer.valueOf(m_strPMGrade));
                            } else {
                                String msg = "미세먼지 아이콘 적용 실패 : 해당 코드에 맞는 날씨 아이콘이 없습니다.";
                                Log.d(TAG, msg);
                                log.info(msg);
                            }
                        } else {
                            String resMsg = root.getChild("header").getChild("resultMsg").getText();
                            String msg = String.format("미세먼지 데이터 불러오기 실패(%s : %s)", resCode, resMsg);
                            Log.d(TAG, msg);
                            log.info(msg);
                        }


                    }

                    @Override
                    public void onFailed(Exception e, Map map) {

                    }
                }).execute();
    }

    private void initializeData() { getNews(); }

    /**
     * 뉴스 불러오기
     */
    private void getNews() {
        NewsRetrofit.getNewsRetroInstance().getNews().enqueue(new Callback<ArrayList<News>>() {
            @Override
            public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {
                if(response.body() == null) Log.w("TEST", "response is null");

                Log.d("TEST", response.body().toString());

                /** 받아온 뉴스정보 담기 */
                news1.setText(response.body().get(0).title);
                news2.setText(response.body().get(1).title);
                news3.setText(response.body().get(2).title);
                news4.setText(response.body().get(3).title);
                news5.setText(response.body().get(4).title);
                news6.setText(response.body().get(5).title);
                news7.setText(response.body().get(6).title);
                news8.setText(response.body().get(7).title);
            }

            @Override
            public void onFailure(Call<ArrayList<News>> call, Throwable t) {
                Log.e("TEST", call.toString());
            }
        });
    }




    /*기상청_신규 동네예보정보조회서비스 요청*/
    private void requestWeatherInfo() {
        new PlanuriHttp(getActivity())
                .setRequestMethod(PlanuriHttp.METHOD_GET)
                .setUrl("http://api.openweathermap.org/data/2.5/forecast?&APPID=495c1b121c7134872e52b3ac1532b933&lat=37.5985&lon=126.9783&mode=json&units=metric")
                .setListener(new PlanuriHttp.APIListener() {
                    @Override
                    public void onSuccess(String res) {
                        Calendar cal = Calendar.getInstance();
                        String format = "yyyy.MM.dd E요일";
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        System.out.println("format >>> " + Locale.KOREA);
                        SimpleDateFormat dayOfWeek = new SimpleDateFormat("E요일", Locale.KOREA);

                        Date dates = null;
//                    week_date4_2.setText(dayOfWeek.format(dates).toString());     //측정요일

                        Log.d(TAG, res);
                        Date now = new Date();
                        System.out.println("now >>> " + now);
                        long nowlong = now.getTime();
                        final long hour3 = 10800000L;
                        try{
                            JSONObject jsnRoot = new JSONObject(res); //res 인자로 넣어 jsonObject를 생성한다.
                            ArrayList<String> list = new ArrayList<>();
                            int cnt = jsnRoot.has("cnt")? jsnRoot.getInt("cnt"):0;

                            if(cnt > 0){
                                JSONArray jsnArr = jsnRoot.getJSONArray("list");
                                System.out.println("jsnArr >>> " + jsnArr);
                                m_arrWeatherItem.clear();
                                for(int i=0; i < cnt; i++){
                                    JSONObject jsnItem = jsnArr.getJSONObject(i);
                                    JSONObject itemMain = jsnItem.getJSONObject("main");
                                    JSONObject itemWind = jsnItem.getJSONObject("wind");
                                    JSONObject itemRain = jsnItem.has("rain")? jsnItem.getJSONObject("rain"):null;
                                    JSONObject itemClouds = jsnItem.has("clouds")? jsnItem.getJSONObject("clouds"):null;
                                    JSONObject itemWeather = jsnItem.getJSONArray("weather").getJSONObject(0);

                                    /**
                                     * 당일 정보/ 날씨
                                     */
                                    if(i == 0) { // i = 0일때, 당일 오후 12시 기준 / 그리고 첫번째 날씨는 메인에 뿌려짐
                                        String code = itemWeather.getString("icon");
                                        sendWeatherListener.sendWeaterMessage(itemMain.getInt("temp"), code, m_mpWeatherIcons_main);
//                                    m_imgTodayIcon.setImageResource(m_mpWeatherIcons.get(code));
                                        smart_weather_main.setImageResource(m_mpWeatherIcons_main.get(code)); //메인 이미지
                                        tv_temp_status.setText("" + itemMain.getInt("temp"));
                                        tv_temp_weather_text.setText("" + itemWeather.getString("description"));
                                        low_text2.setText("" + itemMain.getInt("temp_min")); //최저 온도
                                        high_text2.setText("" + itemMain.getInt("temp_max")); //최고 온도
                                        wind_direction_info.setText("" + itemWind.getInt("speed")); //풍향

                                        /* 당일 강수량의 정보*/
                                        if (itemRain != null){
                                            fail_info.setText("" + itemRain.getInt("3h")); //강수량
                                        } else {
                                            fail_info.setText("0"); //강수량
                                        }

                                        /*오늘의 습도*/
                                        precipitation_info.setText("" + itemMain.getInt("humidity"));

                                        System.out.println("itemWind >>> " + itemWind);

//                                    m_txtTodayTemperature.setText(String.format("%.0f", itemMain.getDouble("temp")));
//                                    m_txtTodayRainRate.setText(String.format("%.0f％", itemMain.getDouble("humidity")));
                                    } else if (i == 8){ // i = 8일때, +1일 후 오후 12시 기준 / 그리고 두번째 날씨는 서브에 뿌려짐
                                        String code = itemWeather.getString("icon");

                                        /* 당일 +1일 후 날씨 아이콘*/
                                        weather_icon1.setImageResource(m_mpWeatherIcons.get(code)); //메인

                                        /** 당일 +1일 후 */
                                        cal.add(cal.DATE, 1);
                                        String date = sdf.format(cal.getTime());
                                        week_date1_1.setText("" + date.substring(5,10)); // 날짜 출력
                                        System.out.println("date1 >>?? "+ date.substring(11,14));
                                        itemMain.getDouble("temp_min");
                                        itemMain.getDouble("temp_max");

                                        /** 당일 +1일 후 요일 출력하기 */
                                        week_date1_2.setText("" + now.toString().substring(0,4));
                                        if (date.substring(11,14).equals("일요일")){
                                            week_date1_2.setText("SUN");
                                        } else if (date.substring(11,14).equals("월요일")){
                                            week_date1_2.setText("MON");
                                        } else if (date.substring(11,14).equals("화요일")){
                                            week_date1_2.setText("TUE");
                                        } else if (date.substring(11,14).equals("수요일")){
                                            week_date1_2.setText("WED");
                                        } else if (date.substring(11,14).equals("목요일")){
                                            week_date1_2.setText("THU");
                                        } else if (date.substring(11,14).equals("금요일")){
                                            week_date1_2.setText("FRI");
                                        } else if (date.substring(11,14).equals("토요일")){
                                            week_date1_2.setText("SAT");
                                        }

                                        /** 최저 온도*/
                                        week_date1_3.setText("" + itemMain.getInt("temp_min"));
                                        /** 최고 온도*/
                                        week_date1_4.setText("" + itemMain.getInt("temp_max"));

                                        /**
                                         * 첫번째 강수확률1
                                         */
                                        if (itemRain != null){
                                            week_date1_6.setText("" + itemRain.getInt("3h")); //강수량
                                        } else {
                                            week_date1_6.setText("0"); //강수량
                                        }

                                        /**
                                         * itemClouds
                                         */
//                                        if (itemClouds != null){
//                                            week_date1_6.setText("" + itemClouds.getInt("all")); //강수량
//                                        } else {
//                                            week_date1_6.setText("0"); //강수량
//                                        }


                                    } else if (i == 16){ // i = 16일때, +2일 후 오후 12시 기준 / 그리고 두번째 날씨는 서브에 뿌려짐
                                        String code = itemWeather.getString("icon");

                                        /* 당일 +1일 후 날씨 아이콘*/
                                        weather_icon2.setImageResource(m_mpWeatherIcons.get(code)); //메인

                                        /* 당일 +2일 후*/
                                        cal.add(cal.DATE, 1);
                                        String date = sdf.format(cal.getTime());
                                        week_date2_1.setText("" + date.substring(5,10)); // 날짜 출력
                                        System.out.println("date2 >> "+ date);
                                        System.out.println("date2 >> "+ cal.DATE);

                                        /* 당일 +2일 후 요일 출력하기*/
                                        week_date2_2.setText("" + now.toString().substring(0,4));
                                        if (date.substring(11,14).equals("일요일")){
                                            week_date2_2.setText("SUN");
                                        } else if (date.substring(11,14).equals("월요일")){
                                            week_date2_2.setText("MON");
                                        } else if (date.substring(11,14).equals("화요일")){
                                            week_date2_2.setText("TUE");
                                        } else if (date.substring(11,14).equals("수요일")){
                                            week_date2_2.setText("WED");
                                        } else if (date.substring(11,14).equals("목요일")){
                                            week_date2_2.setText("THU");
                                        } else if (date.substring(11,14).equals("금요일")){
                                            week_date2_2.setText("FRI");
                                        } else if (date.substring(11,14).equals("토요일")){
                                            week_date2_2.setText("SAT");
                                        }

                                        /** 최저 온도*/
                                        week_date2_3.setText("" + itemMain.getInt("temp_max"));

                                        /** 최고 온도*/
                                        week_date2_4.setText("" + itemMain.getInt("temp_max"));

                                        /** 두번째 강수확률*/
                                        if (itemRain != null){
                                            week_date2_6.setText("" + itemRain.getInt("3h")); //강수량
                                        } else {
                                            week_date2_6.setText("0"); //강수량
                                        }

                                    } else if (i == 24){ // i = 24일때, +3일 후 오후 12시 기준 / 그리고 세번째 날씨는 서브에 뿌려짐
                                        String code = itemWeather.getString("icon");

                                        /* 당일 +1일 후 날씨 아이콘*/
                                        weather_icon3.setImageResource(m_mpWeatherIcons.get(code)); //메인

                                        /* 당일 +3일 후*/
                                        cal.add(cal.DATE, 1);
                                        String date = sdf.format(cal.getTime());
                                        week_date3_1.setText("" + date.substring(5,10)); // 날짜 출력

                                        /* 당일 +3일 후 요일 출력하기*/
                                        week_date3_2.setText("" + now.toString().substring(0,4));
                                        if (date.substring(11,14).equals("일요일")){
                                            week_date3_2.setText("SUN");
                                        } else if (date.substring(11,14).equals("월요일")){
                                            week_date3_2.setText("MON");
                                        } else if (date.substring(11,14).equals("화요일")){
                                            week_date3_2.setText("TUE");
                                        } else if (date.substring(11,14).equals("수요일")){
                                            week_date3_2.setText("WED");
                                        } else if (date.substring(11,14).equals("목요일")){
                                            week_date3_2.setText("THU");
                                        } else if (date.substring(11,14).equals("금요일")){
                                            week_date3_2.setText("FRI");
                                        } else if (date.substring(11,14).equals("토요일")){
                                            week_date3_2.setText("SAT");
                                        }

                                        /** 최저 온도*/
                                        week_date3_3.setText("" + itemMain.getInt("temp_max"));

                                        /** 최고 온도*/
                                        week_date3_4.setText("" + itemMain.getInt("temp_max"));

                                        /** 세번째 강수확률*/
                                        if (itemRain != null){
                                            week_date3_6.setText("" + itemRain.getInt("3h")); //강수량
                                        } else {
                                            week_date3_6.setText("0"); //강수량
                                        }

                                    } else if (i == 32){ // i = 32일때, +4일 후 오후 12시 기준 / 그리고 네번째 날씨는 서브에 뿌려짐
                                        String code = itemWeather.getString("icon");

                                        /* 당일 +4일 후 날씨 아이콘*/
                                        weather_icon4.setImageResource(m_mpWeatherIcons.get(code)); //메인

                                        /* 당일 +2일 후*/
                                        cal.add(cal.DATE, 1);
                                        String date = sdf.format(cal.getTime());
                                        week_date4_1.setText("" + date.substring(5,10)); // 날짜 출력

                                        /* 당일 +4일 후 요일 출력하기*/
                                        week_date4_2.setText("" + now.toString().substring(0,4));
                                        if (date.substring(11,14).equals("일요일")){
                                            week_date4_2.setText("SUN");
                                        } else if (date.substring(11,14).equals("월요일")){
                                            week_date4_2.setText("MON");
                                        } else if (date.substring(11,14).equals("화요일")){
                                            week_date4_2.setText("TUE");
                                        } else if (date.substring(11,14).equals("수요일")){
                                            week_date4_2.setText("WED");
                                        } else if (date.substring(11,14).equals("목요일")){
                                            week_date4_2.setText("THU");
                                        } else if (date.substring(11,14).equals("금요일")){
                                            week_date4_2.setText("FRI");
                                        } else if (date.substring(11,14).equals("토요일")){
                                            week_date4_2.setText("SAT");
                                        }

                                        /** 최저 온도*/
                                        week_date4_3.setText("" + itemMain.getInt("temp_max"));

                                        /** 최고 온도*/
                                        week_date4_4.setText("" + itemMain.getInt("temp_max"));

                                        /** 네번째 강수확률*/
                                        if (itemRain != null){
                                            week_date4_6.setText("" + itemRain.getInt("3h")); //강수량
                                        } else {
                                            week_date4_6.setText("0"); //강수량
                                        }
                                    }
                                }
                                m_lstWeatherDowAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void onFailed(Exception e, Map map) {  }
                }).execute();
    }

//    @Override
//    public void sendWeaterMessage() {
//
//    }

    class WeatherDOWListAdapter extends BaseAdapter {

        @Override public int getCount() { return m_arrWeatherItem.size(); }
        @Override public Object getItem(int position) { return (m_arrWeatherItem.size() > position)? m_arrWeatherItem.get(position):null; }
        @Override public long getItemId(int position) { return (m_arrWeatherItem.size() > position)? m_arrWeatherItem.hashCode():-1; }
        <T extends WeatherItem> T get(int position) { return  ((m_arrWeatherItem.size() > position))? (T)m_arrWeatherItem.get(position):null; }

        @Override public View getView(int i, View v, ViewGroup p) {
            if(v == null){
                LayoutInflater il = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = il.inflate(R.layout.item_weather_of_day, p, false);
            }
            if(get(i) != null) {
                WeatherItem item = m_arrWeatherItem.get(i);
                ImageView tempWeatherIcon = v.findViewById(R.id.img_weather_icon);
                tempWeatherIcon.setImageResource(m_mpWeatherIcons.get(item .iconCode));
                TextView tempMaxView = v.findViewById(R.id.txt_temperature_max);
                tempMaxView.setText(item .tempMax);
                TextView tempMinView = v.findViewById(R.id.txt_temperature_min);
                tempMinView.setText(item .tempMin);
                TextView DOWView = v.findViewById(R.id.txt_day_of_week);
                DOWView.setText(Utils.getFormattedDate("EEE", item .date));
                TextView DOMView = v.findViewById(R.id.txt_day_of_month);
                DOMView.setText(Utils.getFormattedDate("dd", item .date));
                TextView HumView = v.findViewById(R.id.txt_rain_humidity);
                HumView.setText(item .humidity);
            }

            return v;
        }
    }

    class WeatherItem {
        Date date;
        String iconCode;
        String tempMax;
        String tempMin;
        String humidity;

        public WeatherItem(JSONObject obj){
            try {
                //Log.d(TAG, "WeatherItem conststruct : " + obj.toString());
                JSONObject main = obj.getJSONObject("main");
                JSONObject weather = obj.getJSONArray("weather").getJSONObject(0);
                date = new Date(obj.getLong("dt") * 1000L);
                iconCode = weather.getString("icon");
                tempMax  = String.format("%.0f", main.getDouble("temp_max"));
                tempMin  = String.format("%.0f", main.getDouble("temp_min"));
                humidity = String.format("%.0f", main.getDouble("humidity"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

}
