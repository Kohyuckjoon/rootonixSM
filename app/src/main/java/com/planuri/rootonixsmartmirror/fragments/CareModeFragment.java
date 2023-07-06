package com.planuri.rootonixsmartmirror.fragments;

import android.app.Activity;
import android.app.Person;
import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.RegKioskUserDto;
import com.planuri.rootonixsmartmirror.dto.ResLoginDto;
import com.planuri.rootonixsmartmirror.dto.ResUsersDto;
import com.planuri.rootonixsmartmirror.fragments.adapter.MeasurementDetailsAdapter;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.TelSearchUserItem;
import com.planuri.rootonixsmartmirror.service.DateService;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.Utils;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CareModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CareModeFragment extends BaseFragment {
    private final int CUSTOMER_REGISTRATION_FRAGMENT = 8;//고객 신규 등록1
    private ImageView new_customer_btn;
    private EditText editText;
    private TextView textView;
    View view;
    private ImageView search_Results;
    Activity mActivity;

    Button button;

    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface
    private static final String TAG = CareModeFragment.class.getSimpleName();

    RecyclerView recyclerView_list; //RecyclerView를 사용
    MeasurementDetailsAdapter adapter; //Adapter 사용
    GridLayoutManager layoutManager;

    void makeFullScreen(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.KEEP_SCREEN_ON);
    }

    private void addCustom(){

    }

    public CareModeFragment() {}

    // TODO: Rename and change types and number of parameters
    public static CareModeFragment newInstance(String param1, String param2) {
        CareModeFragment fragment = new CareModeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    /**
     * 프래그먼트가 처음 실행될때 수행하는 생명 주기
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_care_mode, container, false);
        makeFullScreen();

        // 레이아웃
        ViewGroup fragmentLayout = (ViewGroup) view.findViewById(R.id.searchUserLayout);
        fragmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //클릭 시 키보드 내리기
                hideKeyboard();
            }
        });

        /** adapter에 담을 내용을 셋팅 */
        adapter = new MeasurementDetailsAdapter(getActivity().getApplicationContext());

        /** RecyclerView Grid 아이템 클릭 이벤트*/
        adapter.setOnGridItemClickListener(new MeasurementDetailsAdapter.OnGridItemClickListener() {
            @Override
            public void onGridItemClicked(View v, int position, TelSearchUserItem items) {
                // TODO : 아이템 클릭 이벤트를 처리.

//                Toast.makeText(getActivity(), "clicked:" + position + ", item: " + items.kuserName, Toast.LENGTH_SHORT).show();

//                ((MainActivity)getActivity()).fragmentView(12);

                System.out.println("items.kuserName >>>>>>>>>! " + items.kuserName);
                System.out.println("items.kuserTel >>>>>>>>>!" + items.kuserTel);

                /**
                 * 로그인 시도
                 */
                CallApi.RequestLogin("" + items.kuserName, "" + items.kuserTel).enqueue(new Callback<ResLoginDto>() {

                    @Override
                    public void onResponse(Call<ResLoginDto> call, Response<ResLoginDto> response) {
                        if(response.isSuccessful() && response.body() != null) {/** HTTP 호출 성공인 Code 200을 받았을 경우(실제 200~300이지만 200만 사용)*/
                            Log.d("aaa", response.body().toString() + "");

                            ResLoginDto loginDto = response.body();

                            TelSearchUserItem searchItem = new TelSearchUserItem();

                            /**데이터 내용 확인*/
                            System.out.println("response.body() " + response.body());
                            System.out.println("loginDto.msg >>>>>" + loginDto.msg);
                            System.out.println("loginDto.success >>>>>" + loginDto.success);
                            System.out.println("loginDto.code >>>>>" + loginDto.code);

                            /**
                             * 넘어온 데이터 담기
                             * loginDto.data.createDate, kuserName, kuserTel, kuserBirthDate, kuserSex
                             */
                            LoginKioskUser.getInstance().setKioskUser(loginDto.data);

                            /**로그인 상태 변경*/
                            LoginKioskUser.getInstance().setLogin(true);

                            ((MainActivity)getActivity()).fragmentView(4);
                            Toast.makeText(getActivity(), items.kuserName + "님, 환영합니다.", Toast.LENGTH_SHORT).show();
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResLoginDto> call, Throwable t) {

                    }
                });
            }
        });

        //고객 신규 등록 버튼
        ImageView new_customer_btn = (ImageView) view.findViewById(R.id.new_customer_btn);

        //검색된 내용을 저장
        EditText telEntry = view.findViewById(R.id.telEntry);

        //결과창
//        TextView resultText = (TextView) view.findViewById(R.id.resultText);
//        System.out.println("resultText >>> " + resultText);

        /**
         * LinearLayoutManager 사용시 아래 주석 해제
         */
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_list.setLayoutManager(layoutManager);*/

        /**
         * GridLayoutManager 사용
         */
        //recyclerView setting
        recyclerView_list = (RecyclerView) view.findViewById(R.id.kuser_name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView_list.setLayoutManager(layoutManager);


        /**
         * 고객 신규 등록 1번째 화면
         */
        new_customer_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(8);
            }
        });


        /**
         * 전화번호 검색
         */
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        System.out.println("year >>> " + year);

        Button telSearchBtn = (Button) view.findViewById(R.id.telSearchBtn);

        ImageView noResultsFound = (ImageView) view.findViewById(R.id.no_results_found);

//        TextView telSearchBtn = (TextView) view.findViewById(R.id.telSearchBtn);



        /**검색 버튼 클릭시 */
        telSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //키보드 내리기
                hideKeyboard();
                makeFullScreen();
//                Log.d("", resultText + "");
                String strTelNum = telEntry.getText().toString();   //끝 4자리 전화번호

                Log.d("", adapter + "");

                /**
                 * 입력 받은 전화번호 담아서 api 요청하기
                 */
                CallApi.getUsersByTelNum(strTelNum).enqueue(new Callback<ResUsersDto>() {
                    @Override
                    public void onResponse(Call<ResUsersDto> call, Response<ResUsersDto> response) {
                        int ageTest = 0;
                        if(response.isSuccessful() && response.body() != null) {    /** HTTP 호출 성공인 Code 200을 받았을 경우(실제 200~300이지만 200만 사용)*/
                            Log.d("aaa", response.body().toString() + "");

                            ResUsersDto usersDto = response.body();
//                            resultText.setText("Size : " + usersDto.list.size());

                            Calendar current = Calendar.getInstance();
                            int currentYear = current.get(Calendar.YEAR);

                            recyclerView_list.setAdapter(adapter);

                            /**검색 결과가 여러개가 쌓이지 않도록*/
                            adapter.clearItem();

                            /**데이터가 있는 만큼 결과 가져오기*/
                            for (int i = 0; i<usersDto.list.size(); i++){
                                TelSearchUserItem searchItem = new TelSearchUserItem();
                                searchItem.kuserName = usersDto.list.get(i).kuserName;
                                searchItem.kuserTel = usersDto.list.get(i).kuserTel;
                                searchItem.kuserSex = usersDto.list.get(i).kuserSex;

                                /**
                                 * 생년월일 받아온 값 -> 나이로 변환해주기
                                * */
                                if (usersDto.success = true){
                                    searchItem.kuserBirthDate = Utils.calculateAge(usersDto.list.get(i).kuserBirthDate) + "세";
                                }
                                adapter.addItem(searchItem);
                            }

//                            for (KioskUser kioskUser : usersDto.list) {
//                                resultText.append(", " + kioskUser.kuserTel);
//                                Log.e(TAG, kioskUser.kuserIdx + "," + kioskUser.kuserSex + "," + kioskUser.kuserName + ","
//                                        + kioskUser.kuserTel + "," + kioskUser.kuserBirthDate);
//                            }
                        } else {  /** HTTP code: 200 이 아니거나 response body 가 null 인 경우*/
                            switch (response.code()) {
                                case 500: { /** 서버에서 Exception 발생 시 500으로 보냄 */
                                    CommonResponse errorResponse = CustomRestClient.getInstance().getErrorResponse(response);
                                    System.out.println("errorResponse ? >>> " + errorResponse.msg);
//                                    resultText.setText(errorResponse.msg);
                                    break;
                                }
                                default: {  /** 그 외의 HTTP 오류 */
                                    try {
                                        Log.e(TAG, "code: " + response.code() + "\nmsg: " + response.errorBody().string());
                                    } catch(Exception e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                    break;
                                }
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call call, Throwable t) {
                        /** 통신 실패 예외 발생. 주로 Connection time out */
//                        Toast.makeText(getApplicationContext(),"HTTP ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        }); // telSearchBtn.setOnClickListener 끝


        return view;
    }

    private void getViewObject()
    {
        editText = (EditText) view.findViewById(R.id.telEntry);
        textView = (TextView) view.findViewById(R.id.telEntry);
    }

    //키보다 외부 클릭 시 자판 내려가게
    private void hideKeyboard()
    {
        if (getActivity() != null && getActivity().getCurrentFocus() != null)
        {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}