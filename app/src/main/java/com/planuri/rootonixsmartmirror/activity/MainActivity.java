package com.planuri.rootonixsmartmirror.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.planuri.rootonixsmartmirror.dialog.DialogCompletFragment;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.base.BaseActivity;
import com.planuri.rootonixsmartmirror.dialog.DialogFullScreenFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogwithdrawalCheckFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogwithdrawalFragment;
import com.planuri.rootonixsmartmirror.fragments.CareModeFragment;
import com.planuri.rootonixsmartmirror.fragments.CompEvaluationFragment;
import com.planuri.rootonixsmartmirror.fragments.CorrectionFragment;
import com.planuri.rootonixsmartmirror.fragments.CustomerRegistrationFragment;
import com.planuri.rootonixsmartmirror.fragments.FinishedAppointmentFragment;
import com.planuri.rootonixsmartmirror.fragments.ItemEvaluationFragment;
import com.planuri.rootonixsmartmirror.fragments.MeasurementDetailsFragment;
import com.planuri.rootonixsmartmirror.fragments.MeasurementFragment;
import com.planuri.rootonixsmartmirror.fragments.MeasurementUserDetailsFragment;
import com.planuri.rootonixsmartmirror.fragments.MirrorModeFragment;
import com.planuri.rootonixsmartmirror.fragments.ModCompFragment;
import com.planuri.rootonixsmartmirror.fragments.NewCustRegistFragment;
import com.planuri.rootonixsmartmirror.fragments.RegistrationFailedFragment;
import com.planuri.rootonixsmartmirror.fragments.SmartModeFragment;
import com.planuri.rootonixsmartmirror.fragments.SpinnerFragment;
import com.planuri.rootonixsmartmirror.fragments.UserEditFragment;
import com.planuri.rootonixsmartmirror.fragments.UserServiceFragment;
import com.planuri.rootonixsmartmirror.model.SendWeatherListener;
import com.planuri.rootonixsmartmirror.service.AirService;
import com.planuri.rootonixsmartmirror.service.DateService;
import com.planuri.rootonixsmartmirror.service.WaterService;
import com.planuri.rootonixsmartmirror.service.WeatherService;
import com.planuri.rootonixsmartmirror.util.Util;
import com.planuri.rootonixsmartmirror.util.rest.RestUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements SendWeatherListener {
    static Map<String, Integer> m_mpWeatherIcons_main = null;
    private final int SMART_MODE_FRAGMENT = 1;          //스마트모드 1
    private final int MIRROR_MODE_FRAGMENT = 2;         //미러모드 2
    private final int CARE_MODE_FRAGMENT = 3;           //케어모드 3
    //TEST CODE
    private final int USER_SERVICE_FRAGMENT = 4;        //회원 서비스 선택 화면
    private final int ITEM_EVALUATION_FRAGMENT = 5;     //항목별 평가
    private final int COMP_EVALUATION_FRAGMENT = 6;     //종합평가 화면
    private final int MEASUREMENT_DETAILSFRAGMENT = 7;  //측정내역 화면
    private final int CUSTOMER_REGISTRATION_FRAGMENT = 8;//고객 신규 등록1
    private final int NEW_CUST_REGIST_FRAGMENT = 9;     //고객 신규 등록2
    private final int FINISHED_APPOINTMENT_FRAGMENT = 10; //고객등록 완료 화면
    private final int REGISTRATION_FAILED_FRAGMENT = 11; //고객등록 완료 화면
    private final int MEASUREMENT_FRAGMENT = 12;        //카메라 측정화면
    private final int MEASUREMENT_USER_DETAILS_FRAGMENT = 13; //측정 내역 보기(가로뷰)
    private final int SPINNER_FRAGMENT = 14;
    private final int DIALOGCOMPLET_FRAGMENT = 15;      //Dialog_comp
    private final int CORRECTION_FRAGMENT = 16;         //고객 정보 수정 화면
    private final int MODCOMP_FRAGMENT = 17;            //Dialog_mod_comp (고객 정보 수정 화면 팝업)
    private final int DIALOGWITHDRAWAL_FRAGMENT = 18;   //Dialog(회원탈퇴 팝업)
    private final int DIALOGWITHDRAWALCHECK_FRAGMENT = 19; //Dialog(회원탈퇴 완료 팝업)
    private final int USEREDIT_FRAGMENT = 20;           //Dialog(정보 수정 동의 화면 팝업)
    private final int DIALOG_FULL_SCREEN_FRAGMENT = 21;    //항목별 평가 이미지 확대 화면(다이얼로그)

    private TextClock tvClock;
    int clockChangeCount = 0;
    int refreshCount = 0;
    int firstTime;

    private ImageView mirror_mode, smart_mode, care_mode;   //이미지 받아오기
    private ImageView new_customer_btn;
    WeatherService weatherService; //날씨 받아오기
    DateService dateService; //날짜받아오기
    WaterService waterService; //수질데이터 받아오기
    AirService airService; //미세먼지 받아오기
//    TextView tv_temp;   //온도 받아오기
    CountDownTimer sensorTimer = null;

    private TextView textView;
    private EditText editText;


    public void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("경고");
        builder.setMessage("비밀번호를 입력해주세요.");
        builder.setPositiveButton("예",null);
        builder.create().show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("55555", "dispatchKeyEvent " + event);
        if(mMeasurementFragment != null) {
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                mMeasurementFragment.onkeyevent(event);
            }
//            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
//                mMeasurementFragment.onkeyevent(event);
//            } else if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
//                mMeasurementFragment.onkeyevent(event);
//            } else if(event.getAction() == KeyEvent.ACTION_DOWN) {
//                mMeasurementFragment.onkeyevent(event);
//            }
        }
        return super.dispatchKeyEvent(event);
    }

    void makeFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.KEEP_SCREEN_ON);
    }

    /* 테스트 코드 */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        fragmentView(SMART_MODE_FRAGMENT);
//        makeFullScreen();
//        initialize();
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        networkReceiver();
//    }

//    public void networkReceiver() {
//        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkRequest.Builder builder = new NetworkRequest.Builder();
//        manager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(@NonNull Network network) {
//                super.onAvailable(network);
//                try {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //네트워크 연결시
//                            ivNetworkDisconnected.setVisibility(View.INVISIBLE);
//                        }
//                    });
//
//                    if (networkReconnected) {
//                        networkReconnected = false;
//                        Util.restartApp(getApplicationContext());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("MainActivity>>>", e.getMessage());
//                }
//            }
//
//            @Override
//            public void onLost(@NonNull Network network) {
//                mLogger.error("Network Disconnected");
//                networkReconnected = true;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //네트워크 단절시
//                        ivNetworkDisconnected.setVisibility(View.VISIBLE);
//                    }
//                });
//                super.onLost(network);
//            }
//        });
//    }

    /* 실행 코드 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            setTheme(R.style.SplashTheme);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            fragmentView(SMART_MODE_FRAGMENT);
//            fragmentView(MEASUREMENT_FRAGMENT);
            makeFullScreen();
            initialize();

            RestUtils.getInstance().initialize(getApplicationContext());

//            newsService = new NewsService(this);
            airService = new AirService(this);
            weatherService = new WeatherService(this);
            waterService = new WaterService(this);
            dateService = new DateService(this);
//            contentService = new ContentService(this);
            Calendar calendar = Calendar.getInstance();
            firstTime = calendar.get(Calendar.HOUR);

//            ivMascot = findViewById(R.id.iv_mascot);
//            ivNetworkDisconnected = findViewById(R.id.iv_network_disconnected);
//            tvMascot = findViewById(R.id.tv_mascot);
            tvClock = findViewById(R.id.tv_clock);
            tvClock.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String currentTime = tvClock.getText().toString();
                    Log.e("THIS TIME", currentTime);
                    refreshCount = 0;
                    if (currentTime.equals("00:00") || currentTime.equals("12:00")){
                        if (clockChangeCount == 0){
                            dateService.initializeDate();
                            Log.e(">>>>>>", "date updated");
                        }
                        clockChangeCount+=1;
                    } else {
                        Log.e(">>>>>>", "not need date update");
                        clockChangeCount = 0;
                    }

                    if (refreshCount == 0){
                        Calendar calendar = Calendar.getInstance();
                        int checkHour = calendar.get(Calendar.HOUR);
                        if (checkHour != firstTime){
                            Log.e(">>>>>시간대 변경됨 ", "최초조회시간 : "+firstTime+" / 현재시간 : "+checkHour);
                            weatherService.initializeSky();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    waterService.getWaterSensor();
                                }
                            }).start();

                            firstTime = checkHour;
                        } else {
                            Log.e(">>>>>", "No Need Weather Updated");
                        }

//                        newsService.getNews();
                        airService.getAir();
//                        waterService.getWaterSensor();
                    }
                    refreshCount+=1;
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });

//            tvClock.setOnClickListener(v -> {
//                Intent intent = new Intent(MainActivity.this, UpgradeActivity.class);
//                startActivity(intent);
//            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    waterService.getWaterSensor();
                }
            }).start();

            airService.getAir();
//            newsService.getNews();
            weatherService.initializeSky();
//            contentService.initializeContents();
            dateService.initializeDate();
        } catch (Exception e){
            e.printStackTrace();
//            mLogger.error(e.getMessage());
            Util.restartApp(getApplicationContext());
        }
    }

    MeasurementFragment mMeasurementFragment;
    /**
     * 프래그먼트 호출
     */
    public void fragmentView(int fragment) {
        mMeasurementFragment = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment) {
            case 1:
                SmartModeFragment smartModeFragment = new SmartModeFragment();
                transaction.replace(R.id.fl_main, smartModeFragment);
                transaction.commit();
                break;
            case 2:
                MirrorModeFragment mirrorModeFragment = new MirrorModeFragment();
                transaction.replace(R.id.fl_main, mirrorModeFragment);
                transaction.commit();
                break;
            case 3:
                CareModeFragment careModeFragment = new CareModeFragment();
                transaction.replace(R.id.fl_main, careModeFragment);
                transaction.commit();
                break;
            case 4:
                UserServiceFragment userServiceFragment = new UserServiceFragment();
                transaction.replace(R.id.fl_main, userServiceFragment);
                transaction.commit();
                break;
            case 5:
                ItemEvaluationFragment itemEvaluationFragment = new ItemEvaluationFragment();
                transaction.replace(R.id.fl_main, itemEvaluationFragment);
                transaction.commit();
                break;
            case 6:
                CompEvaluationFragment compEvaluationFragment = new CompEvaluationFragment();
                transaction.replace(R.id.fl_main, compEvaluationFragment);
                transaction.commit();
                break;
            case 7:
                MeasurementDetailsFragment measurementDetailsfragment = new MeasurementDetailsFragment();
                transaction.replace(R.id.fl_main, measurementDetailsfragment);
                transaction.commit();
                break;
            case 8: //고객 신규 등록 1번째 화면
                CustomerRegistrationFragment customerRegistrationFragment = new CustomerRegistrationFragment();
                transaction.replace(R.id.fl_main, customerRegistrationFragment);
                transaction.commit();
                break;
            case 9: //고객 신규 등록 2번째 화면
                NewCustRegistFragment newCustRegistFragment = new NewCustRegistFragment();
                transaction.replace(R.id.fl_main, newCustRegistFragment);
                transaction.commit();
                break;
            case 10: //고객 신규 등록 3번째 화면
                FinishedAppointmentFragment finishedAppointmentFragment = new FinishedAppointmentFragment();
                transaction.replace(R.id.fl_main, finishedAppointmentFragment);
                transaction.commit();
                break;
            case 11:
                RegistrationFailedFragment registrationFailedFragment = new RegistrationFailedFragment();
                transaction.replace(R.id.fl_main, registrationFailedFragment);
                transaction.commit();
                break;
            case 12: //카메라 측정화면
                MeasurementFragment measurementFragment = new MeasurementFragment();
                mMeasurementFragment = measurementFragment;
                transaction.replace(R.id.fl_main, measurementFragment);
                transaction.commit();
                break;
            case 13: //측정 내역 보기(가로뷰)measurement_user_details_fragment
                MeasurementUserDetailsFragment measurementUserDetailsFragment = new MeasurementUserDetailsFragment();
                transaction.replace(R.id.fl_main, measurementUserDetailsFragment);
                transaction.commit();
                break;
            case 14: //측정 내역 보기(가로뷰)measurement_user_details_fragment
                SpinnerFragment spinnerFragment = new SpinnerFragment();
                transaction.replace(R.id.fl_main, spinnerFragment);
                transaction.commit();
                break;
            case 15:
                DialogCompletFragment dialogCompletFragment = new DialogCompletFragment();
                transaction.replace(R.id.fl_main, dialogCompletFragment);
                transaction.commit();
                break;
            case 16:
                CorrectionFragment correctionFragment = new CorrectionFragment();
                transaction.replace(R.id.fl_main, correctionFragment);
                transaction.commit();
                break;
            case 17:
                ModCompFragment modCompFragment = new ModCompFragment();
                transaction.replace(R.id.fl_main, modCompFragment);
                transaction.commit();
                break;
            case 18:
                DialogwithdrawalFragment dialogwithdrawalFragment = new DialogwithdrawalFragment();
                transaction.replace(R.id.fl_main, dialogwithdrawalFragment);
                transaction.commit();
                break;
            case 19:
                DialogwithdrawalCheckFragment dialogwithdrawalCheckFragment = new DialogwithdrawalCheckFragment();
                transaction.replace(R.id.fl_main, dialogwithdrawalCheckFragment);
                transaction.commit();
                break;
            case 20:
                UserEditFragment userEditFragment = new UserEditFragment();
                transaction.replace(R.id.fl_main, userEditFragment);
                transaction.commit();
                break;
            case 21:
                DialogFullScreenFragment dialogFullScreenFragment = new DialogFullScreenFragment();
                transaction.replace(R.id.fl_main, dialogFullScreenFragment);
                transaction.commit();
                break;
        }
    }



    /**
     * 메인 하단 버튼(3EA)
     */
    public void initialize(){
        mirror_mode = findViewById(R.id.mirror_mode);
        smart_mode = findViewById(R.id.smart_mode_on);
        care_mode = findViewById(R.id.care_mode);

        new_customer_btn = findViewById(R.id.new_customer_btn);

        //mirror_mode Click
        mirror_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentView(MIRROR_MODE_FRAGMENT);
                mirror_mode.setImageResource(R.drawable.mirror_mode_on);
                smart_mode.setImageResource(R.drawable.smart_mode);
                care_mode.setImageResource(R.drawable.care_mode);
            }
        });
        //smart_mode Click
        smart_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentView(SMART_MODE_FRAGMENT);
                mirror_mode.setImageResource(R.drawable.mirror_mode);
                smart_mode.setImageResource(R.drawable.smart_mode_on);
                care_mode.setImageResource(R.drawable.care_mode);
            }
        });
        //care_mode Click
        care_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentView(CARE_MODE_FRAGMENT);
                mirror_mode.setImageResource(R.drawable.mirror_mode);
                smart_mode.setImageResource(R.drawable.smart_mode);
                care_mode.setImageResource(R.drawable.care_mode_on);
            }
        });
        //Test Code
//        care_mode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragmentView(COMP_EVALUATION_FRAGMENT);
//                mirror_mode.setImageResource(R.drawable.mirror_mode);
//                smart_mode.setImageResource(R.drawable.smart_mode);
//                care_mode.setImageResource(R.drawable.care_mode_on);
//            }
//        });
    }

    /**
     * 약관 동의 Check
     */
//    public void onCheckboxClicked(View view){
//        boolean checked = ((CheckBox)view).isChecked();
//
//        CheckBox checkbox_cheese1 = findViewById(R.id.checkbox_cheese1);
//        CheckBox checkbox_cheese2 = findViewById(R.id.checkbox_cheese2);
//        ImageView next_btn = findViewById(R.id.next);
//
//        switch (view.getId()){
//            case R.id.next:
//                if(!checkbox_cheese1.isChecked()){
//                    Toast.makeText(getApplicationContext(), "고객이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
//                }else if(!checkbox_cheese2.isChecked()){
//                    Toast.makeText(getApplicationContext(), "고객이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.checkbox_cheese1:
//                if (!checked) {
//                    Toast.makeText(getApplicationContext(), "고객이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case  R.id.checkbox_cheese2:
//                if (!checked) {
//                    Toast.makeText(getApplicationContext(), "개인정보취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }

    @Override
    public void sendWeaterMessage(int temp, String code, final Map<String, Integer> weatherMap) {
//        sendWeatherListener.sendWeaterMessage("20", code);
        System.out.println("temp >>> " + temp);
        System.out.println("icon >>> " + code);

        ImageView iv_weather = findViewById(R.id.iv_weather);
        TextView tv_weather_text = findViewById(R.id.tv_weather_text);
        TextView tv_temp_status_main = findViewById(R.id.tv_temp_status_main);
//        TextView tv_temp = findViewById(R.id.tv_temp);

        /** 날씨 아이콘 출력하기*/
        iv_weather.setImageResource(m_mpWeatherIcons_main.get(code));

        /** 날씨 텍스트 출력*/
        if(code.equals("01d")){
            tv_weather_text.setText("Sunny");
        }else if(code.equals("02d")){
            tv_weather_text.setText("Cloudy");
        }else if(code.equals("03d")){
            tv_weather_text.setText("Cloudy");
        }else if(code.equals("04d")){
            tv_weather_text.setText("Cloudy");
        }else if(code.equals("09d")){
            tv_weather_text.setText("Rainy");
        }else if(code.equals("10d")){
            tv_weather_text.setText("Rainy");
        }else if(code.equals("11d")){
            tv_weather_text.setText("Lightning");
        }else if(code.equals("13d")){
            tv_weather_text.setText("Snow");
        }else if(code.equals("50d")){
            tv_weather_text.setText("Cloudy");
        }else if(code.equals("01n")){
            tv_weather_text.setText("Sunny");
        }else if(code.equals("02n")){
            tv_weather_text.setText("Sunny");
        }else if(code.equals("03n")){
            tv_weather_text.setText("Cloudy");
        }else if(code.equals("04n")){
            tv_weather_text.setText("Cloudy");
        }else if(code.equals("09n")){
            tv_weather_text.setText("Rainy");
        }else if(code.equals("10n")){
            tv_weather_text.setText("Rainy");
        }else if(code.equals("11n")){
            tv_weather_text.setText("Lightning");
        }else if(code.equals("13n")){
            tv_weather_text.setText("Snow");
        }else if(code.equals("50n")){
            tv_weather_text.setText("Cloudy");
        }

        /** 온도 출력하기*/
        tv_temp_status_main.setText("" + temp);
    }

    /*메인 날씨*/
    static {
        m_mpWeatherIcons_main = new HashMap<>();
        m_mpWeatherIcons_main.put("01d", R.drawable.weather_sunny_background_dark); m_mpWeatherIcons_main.put("02d", R.drawable.weather_cloudy_background_dark);
        m_mpWeatherIcons_main.put("03d", R.drawable.weather_vcloudy_background_dark); m_mpWeatherIcons_main.put("04d", R.drawable.weather_vcloudy_background_dark);
        m_mpWeatherIcons_main.put("09d", R.drawable.weather_rain_background_dark); m_mpWeatherIcons_main.put("10d", R.drawable.weather_rain_background_dark);
        m_mpWeatherIcons_main.put("11d", R.drawable.weather_lightning_background_dark); m_mpWeatherIcons_main.put("13d", R.drawable.weather_snow_background_dark);
        m_mpWeatherIcons_main.put("50d", R.drawable.weather_icon_61); m_mpWeatherIcons_main.put("01n", R.drawable.weather_sunny_background_dark);
        m_mpWeatherIcons_main.put("02n", R.drawable.weather_sunny_background_dark); m_mpWeatherIcons_main.put("03n", R.drawable.weather_cloudy_background_dark);
        m_mpWeatherIcons_main.put("04n", R.drawable.weather_cloudy_background_dark); m_mpWeatherIcons_main.put("09n", R.drawable.weather_rain_background_dark);
        m_mpWeatherIcons_main.put("10n", R.drawable.weather_rain_background_dark); m_mpWeatherIcons_main.put("11n", R.drawable.weather_lightning_background_dark);
        m_mpWeatherIcons_main.put("13n", R.drawable.weather_snow_background_dark); m_mpWeatherIcons_main.put("50n", R.drawable.weather_icon_61);
    }
}