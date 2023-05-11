package com.planuri.rootonixsmartmirror.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.dialog.RegCheckInfoFragment;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.RegKioskUserDto;
import com.planuri.rootonixsmartmirror.dto.ResUsersDto;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCustRegistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCustRegistFragment extends Fragment {
    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface
    private static final String TAG = CareModeFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewCustRegistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewCustRegistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewCustRegistFragment newInstance(String param1, String param2) {
        NewCustRegistFragment fragment = new NewCustRegistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_cust_regist, container, false);

        ImageView customer_cancel_btn = (ImageView) view.findViewById(R.id.cancel);



        //취소 버튼 클릭
        customer_cancel_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
            }
        });

        /**
         * 고객 신규 등록 2번째 화면
         */
        EditText strName = view.findViewById(R.id.name_input_box_text);
        EditText strTelNum = view.findViewById(R.id.phone_number);
//        EditText strYearBirthText = view.findViewById(R.id.year_birth_text);
//        EditText monthBirthText = view.findViewById(R.id.month_birth_text);
//        EditText dateBirthText = view.findViewById(R.id.date_birth_text);
//        CheckBox kuserCheckM = view.findViewById(R.id.checkbox_m);
//        CheckBox kuserCheckW = view.findViewById(R.id.checkbox_w);
        RadioButton kuserCheckM = view.findViewById(R.id.new_regist_man_rd_check);
        RadioButton kuserCheckW = view.findViewById(R.id.new_regist_woman_rd_check);

        ImageView next = (ImageView) view.findViewById(R.id.next);

        /**현재 날짜 구하기*/
        Date date = new Date();
        /**포맷형식 정해주기*/
        SimpleDateFormat sdfStatus = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c1 = Calendar.getInstance();
        /**현재 년,월 int형으로 변환*/
        int thisYear = Integer.parseInt(sdfStatus.format(c1.getTime()).substring(0,4)); // int형 현재 년도
        int thisMonth = Integer.parseInt(sdfStatus.format(c1.getTime()).substring(5,7)); // int형 현재 년도
        System.out.println("thisYear >>> " + thisYear);
        System.out.println("thisYear >>> " + thisMonth);

        /**
         * Spinner 사용
         * 1. 고객 신규 등록 / 연도별
         **/
        List<String> listYears = new ArrayList<String>();

        for(int i = 1930; i <= thisYear; i++) {
            listYears.add(String.valueOf(i)+"년"); //생년월일 >> 문자로 1930~현재연도까지 담김.
        }
        int listLenYears = listYears.size(); //담긴 리스트가 몇개인지
        System.out.println("listLen >>> " + listLenYears);

        Spinner listYearStatus = (Spinner)view.findViewById(R.id.spinner_year_regist);
        ArrayAdapter<String> yearAdapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listYears);
        listYearStatus.setAdapter(yearAdapters);
        listYearStatus.setSelection(thisYear-1952);

        /**
         * Spinner 사용
         * 2. 고객 신규 등록 / 월별
         */
        String[] arrayMonthList = {
                "01월", "02월", "03월", "04월", "05월", "06월", "07월", "08월", "09월", "10월", "11월", "12월"
        };

        List<String> listMonth = new ArrayList<String>();
        int listMonthLen = listMonth.size();
        System.out.println("listMonthLen >>> " + listMonthLen);

//        for(int i = 2020; i <= thisYear; i++) {
//            listMonth.add(String.valueOf(i)+"년"); //문자로 1930~2012년까지 담김.
//        }
//        int listLenMonth = listMonth.size(); //담긴 리스트가 몇개인지
//        System.out.println("listLen >>> " + listLenMonth);

        Spinner listMonthStatus = (Spinner)view.findViewById(R.id.spinner_month_regist);
        ArrayAdapter<String> monthAdapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayMonthList);
        listMonthStatus.setAdapter(monthAdapters);
        listMonthStatus.setSelection(thisMonth-1);

        /**
         * Spinner 사용
         * 3. 고객 신규 등록 / 일별
         */
        String[] arrayDateList = {
                "01일", "02일", "03일", "04일", "05일", "06일", "07일", "08일", "09일", "10일",
                "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일",
                "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일", "29일", "30일", "31일"
        };

        int datethisYear = Integer.parseInt(sdfStatus.format(c1.getTime()).substring(8,10)); // int형 현재 날짜(일)
        System.out.println("datethisYear >>> " + datethisYear);

//        List<String> listDate = new ArrayList<String>();
//        for(int i = 1; i <= datethisYear; i++) {
//            listDate.add("0" + String.valueOf(i) + "일"); //문자로 1일~30, 31일까지 담김
//        }

        Spinner spDateStatus = (Spinner)view.findViewById(R.id.spinner_day_regist);
        ArrayAdapter<String> dateAdapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayDateList);
        spDateStatus.setAdapter(dateAdapters);
        spDateStatus.setSelection(0);



        /** Spinner 사용*/
//        Spinner yearSpinner = (Spinner) view.findViewById(R.id.spinner_year_regist);
//        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date_year_regist, android.R.layout.simple_spinner_item);
//        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        yearSpinner.setAdapter(yearAdapter);

        Spinner monthSpinner = (Spinner) view.findViewById(R.id.spinner_month_regist);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date_month_regist, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        Spinner daySpinner = (Spinner) view.findViewById(R.id.spinner_day_regist);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date_day_regist, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        /**
         * 회원가입(다음)버튼 클릭시,
         */
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    /**이름, 휴대전화번호 받아오기*/
                    String kuserName = strName.getText().toString();
                    String kuserTel = strTelNum.getText().toString();   //끝 4자리 전화번호

//                    String strYearBirth = yearSpinner.getSelectedItem().toString();
                    String strYearBirth = listYearStatus.getSelectedItem().toString();
                    String strMonthBirth = monthSpinner.getSelectedItem().toString();
                    String strDateBirth = daySpinner.getSelectedItem().toString();

                    System.out.println("strYearBirth >>> " + strYearBirth);
                    System.out.println("strMonthBirth >>> " + strMonthBirth);
                    System.out.println("strDateBirth >>> " + strDateBirth);
                    System.out.println("합치기 >>> " + strYearBirth.substring(0,4) + "-" + strMonthBirth.substring(0,2) + "-" + strDateBirth.substring(0,2));
                    int regKioskIdx = 1;
                    char kuserSex = 'M';

//                    Log.e("inserted data : ", kuserName+", "+kuserTel+", "+strYearBirth+", "+strMonthBirth+", "+strDateBirth);

                    /**
                     * 입력 받은 생년월일 합치기
                     * "1999-00-00" << 형식
                     * */
//                    String kuserBirthDate = strYearBirth + "-" + strMonthBirth + "-" + strDateBirth;
                    String kuserBirthDate = strYearBirth.substring(0,4) + "-" + strMonthBirth.substring(0,2) + "-" + strDateBirth.substring(0,2);
                    System.out.println("kuserBirthDate >>> " + kuserBirthDate);
//                String kuserBirthDate = strYearBirth + strMonthBirth + strDateBirth;

                    /**
                     * 성별 구분
                     */
                    if (kuserCheckM.isChecked()){
                        kuserSex = 'M';
                        System.out.println("kuserSex >>> " + kuserSex);
                    }else if(kuserCheckW.isChecked()){
                        kuserSex = 'F';
                        System.out.println("kuserSex >>> " + kuserSex);
                    }

                    /**
                     * userDto에 회원가입에 필요한 정보들을 담기
                     */
                    RegKioskUserDto userDto = new RegKioskUserDto(kuserBirthDate, kuserName, kuserSex, kuserTel, regKioskIdx);
                    Log.e("sended data : ", userDto.toString());
                    /**
                     * CallApi로 userDto를 담아서 보내기
                     */
                    if (kuserName == null || kuserName == ""){
                        Toast.makeText(getActivity(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else if(kuserTel == null || kuserTel == ""){
                        Toast.makeText(getActivity(), "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else if(userDto != null){
                        CallApi.RegisterUser(userDto).enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                CommonResponse commonResponse = response.body();
                                System.out.println("commonResponse >> " + commonResponse);

                                if(response.isSuccessful() && response.body() != null) {    /** HTTP 호출 성공인 Code 200을 받았을 경우(실제 200~300이지만 200만 사용)*/
                                    System.out.println("response.body() >>> " + response);
                                    System.out.println("성공여부 : >>> " + commonResponse.success);
                                    System.out.println("msg : >>> " + commonResponse.msg);
                                    System.out.println("코드 : >>> " + commonResponse.code);

                                    if(commonResponse.success == true) {
                                        ((MainActivity)getActivity()).fragmentView(10);
                                    }


                                } else {  /** HTTP code: 200 이 아니거나 response body 가 null 인 경우*/
                                    switch (response.code()) {
                                        case 500: { /** 서버에서 Exception 발생 시 500으로 보냄 */
                                            CommonResponse errorResponse = CustomRestClient.getInstance().getErrorResponse(response);

                                            System.out.println("errorResponse.msg >>> " + errorResponse.msg);
                                            System.out.println("errorResponse.code >>> " + errorResponse.code);
                                            System.out.println("errorResponse.success >>> " + errorResponse.success);

                                            RegCheckInfoFragment dlg = new RegCheckInfoFragment();
                                            dlg.show(getActivity().getSupportFragmentManager(), "RegCheckInfoFragment");
                                            dlg.setCancelable(true);

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
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                /** 통신 실패 예외 발생. 주로 Connection time out */
                                Toast.makeText(getActivity().getApplicationContext(),"HTTP ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        return view;
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