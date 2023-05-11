package com.planuri.rootonixsmartmirror.fragments;

import android.icu.util.LocaleData;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.dialog.DialogwithdrawalFragment;
import com.planuri.rootonixsmartmirror.dialog.RegCheckInfoFragment;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.CorrectionDto;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CorrectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CorrectionFragment extends DialogFragment {
    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface
    private static final String TAG = CareModeFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CorrectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CorrectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CorrectionFragment newInstance(String param1, String param2) {
        CorrectionFragment fragment = new CorrectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_correction, container, false);

        /** 로그인한 상태 유지*/
        LoginKioskUser.getInstance().setLogin(true);

        long temp = LoginKioskUser.getInstance().getKioskUser().kuserIdx;
        String strTemp = String.valueOf(temp);

        /** 입력받은 이름, 전화번호 id값 불러오기 */
        EditText name_input = view.findViewById(R.id.name_input);
        EditText phone_input = view.findViewById(R.id.phone_input);

        /** 입력받은이름, 전화번호 문자로 받아오기 */
        String nameInput = name_input.getText().toString();
        String phoneInput = phone_input.getText().toString();

        /** 남녀 체크박스 선택하기 */
//        CheckBox checkboxCheeseM = (CheckBox) view.findViewById(R.id.checkbox_m_correct);
//        CheckBox checkboxCheeseF = (CheckBox) view.findViewById(R.id.checkbox_w_correct);
        RadioButton rdBtnkuserM = (RadioButton) view.findViewById(R.id.man_rd_Check);
        RadioButton rdBtnkuserF = (RadioButton) view.findViewById(R.id.wonman_rd_Check);

        /**
         * 기본적으로 로그인 된 정보 화면에 뿌려주기
         * */
        name_input.setText("" + LoginKioskUser.getInstance().getKioskUser().kuserName);
        phone_input.setText("" + LoginKioskUser.getInstance().getKioskUser().kuserTel);

        /**
         * 입력받은 체크박스
         * m(남자) / w(여자
         **/
//        CheckBox checkbox_m_correct = view.findViewById(R.id.checkbox_m_correct);
//        CheckBox checkbox_w_correct = view.findViewById(R.id.checkbox_w_correct);
        char kuserSex = 'M';

        /**
         * 성별 구분
         */
//        if (rdBtnkuserM.isChecked()){
//            kuserSex = 'M';
//            System.out.println("kuserSex >>> " + kuserSex);
//        }else if(rdBtnkuserF.isChecked()){
//            kuserSex = 'F';
//            System.out.println("kuserSex " + kuserSex);
//        }else if(!rdBtnkuserM.isChecked() || !rdBtnkuserF.isChecked()){
////            Toast.makeText(getActivity().getApplicationContext(), "성별을 체크해주세요.", Toast.LENGTH_SHORT).show();
//            System.out.println("성별을 체크해주세요.");
//        }


//        if (checkbox_m_correct.isChecked()){
//            kuserSex = 'M';
//            System.out.println("kuserSex >>> " + kuserSex);
//        }else if(checkbox_w_correct.isChecked()){
//            kuserSex = 'F';
//            System.out.println("kuserSex " + kuserSex);
//        }else if(!checkbox_m_correct.isChecked() || !checkbox_w_correct.isChecked()){
////            Toast.makeText(getActivity().getApplicationContext(), "성별을 체크해주세요.", Toast.LENGTH_SHORT).show();
//            System.out.println("성별을 체크해주세요.");
//        }

        /** Spinner 사용*/
        /** 기본값(연도) 뿌려주기*/
//        String[] facilityList = {
//                "1930년","1931년","1932년","1933년","1934년","1935년","1936년","1937년","1938년","1939년","1940년","1941년","1942년","1943년","1944년","1945년",
//                "1946년","1947년","1948년","1949년","1950년","1951년","1952년","1953년","1954년","1955년","1956년","1957년","1958년","1959년","1960년","1961년",
//                "1962년","1963년","1964년","1965년","1966년","1967년","1968년","1969년","1970년","1971년","1972년","1973년","1974년","1975년","1976년","1977년",
//                "1978년","1979년","1980년","1981년","1982년","1983년","1984년","1985년","1986년","1987년","1988년","1989년","1990년","1991년","1992년","1993년",
//                "1994년","1995년","1996년","1997년","1998년","1999년","2000년","2001년","2002년","2003년","2004년","2005년","2006년","2007년","2008년","2009년",
//                "2010년","2011년","2012년","2013년","2014년","2015년","2016년","2017년","2018년","2019년","2020년","2021년","2022년","2023년"
//        };
        /**현재 날짜 구하기*/
        Date date = new Date();
        /**포맷형식 정해주기*/
        SimpleDateFormat sdfStatus = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c1 = Calendar.getInstance();
        int thisYear = Integer.parseInt(sdfStatus.format(c1.getTime()).substring(0,4)); // int형 현재 년도
        System.out.println("thisYear >>> " + thisYear);

        List<String> listYears = new ArrayList<String>();

        for(int i = 1930; i < (thisYear - 10); i++) {
            listYears.add(String.valueOf(i)+"년"); //문자로 1930~2012년까지 담김.
        }

        int listLen = listYears.size(); //담긴 리스트가 몇개인지

        String strYearSpiner = LoginKioskUser.getInstance().getKioskUser().kuserBirthDate;  //서버에 저장되어있는 생년월일 저장
        int intYearSpiners = Integer.parseInt(strYearSpiner.substring(0,4)); // 받아온 생년월일중 연도만 int형으로 파싱 (int = 1995)

        Spinner listYearStatus = (Spinner)view.findViewById(R.id.spinner_user_years);
        ArrayAdapter<String> yearAdapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listYears);
        listYearStatus.setAdapter(yearAdapters);
//        listYearStatus.setSelection(yearSpiners - 1930);

        listYearStatus.setSelection(intYearSpiners-1930);
        /**기본값(연도) 끝*/

        /**기본값(월별) 뿌려주기*/
        String[] arrayMonthList = {
                "01월", "02월", "03월", "04월", "05월", "06월", "07월", "08월", "09월", "10월", "11월", "12월"
        };

        Calendar c2 = Calendar.getInstance();
        int monthList = Integer.parseInt(sdfStatus.format(c2.getTime()).substring(5,7)); // int형 현재 월별
        System.out.println("thisYear >>> " + monthList);

        String strMonthSpinner = LoginKioskUser.getInstance().getKioskUser().kuserBirthDate;
        int intMonthSpiners = Integer.parseInt(strMonthSpinner.substring(5,7));

        Spinner spMonthStatus = (Spinner)view.findViewById(R.id.spinner_user_month);
        ArrayAdapter<String> monthadapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayMonthList);
        spMonthStatus.setAdapter(monthadapters);
        spMonthStatus.setSelection(intMonthSpiners-1);
        /** 기본값(월별) 끝*/

        /** 기본값(일별) 뿌려주기*/
        String[] facilityList3 = {
                "01일", "02일", "03일", "04일", "05일", "06일", "07일", "08일", "09일", "10일",
                "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일",
                "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일", "29일", "30일", "31일"
        };

        int datethisYear = Integer.parseInt(sdfStatus.format(c1.getTime()).substring(8,10)); // int형 현재 날짜(일)
        System.out.println("datethisYear >>> " + datethisYear);

        List<String> listDate = new ArrayList<String>();

        for(int i = 1; i <= datethisYear; i++) {
            listDate.add("0" + String.valueOf(i) + "일"); //문자로 1일~30, 31일까지 담김
        }

        String strDateSpiner = LoginKioskUser.getInstance().getKioskUser().kuserBirthDate; // 서버에 저장되어있는 값 불러오기
        int intDateSpiners = Integer.parseInt(strDateSpiner.substring(8,10)); // 생년월일중 몇 일인지 + int형으로 파싱 (int = 15)

        Spinner spDateStatus = (Spinner)view.findViewById(R.id.spinner_user_date);
        ArrayAdapter<String> dateAdapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, facilityList3);
        spDateStatus.setAdapter(dateAdapters);
        spDateStatus.setSelection(intDateSpiners-1);
        /** 기본값(일별) 끝*/

        Spinner yearSpinner = (Spinner) view.findViewById(R.id.spinner_user_years);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getContext(), R.array.cor_date_year_regist, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        yearSpinner.setAdapter(yearAdapter);

        Spinner monthSpinner = (Spinner) view.findViewById(R.id.spinner_user_month);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.cor_date_month_regist, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        monthSpinner.setAdapter(monthAdapter);

        Spinner daySpinner = (Spinner) view.findViewById(R.id.spinner_user_date);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.cor_date_day_regist, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        daySpinner.setAdapter(dayAdapter);

//        int kuserIdx = 1;

        char rdCheckSex = LoginKioskUser.getInstance().getKioskUser().kuserSex;

        RadioButton man_rd_Check = view.findViewById(R.id.man_rd_Check);
        RadioButton wonman_rd_Check = view.findViewById(R.id.wonman_rd_Check);

        System.out.println("rdCheckSex >>> " + rdCheckSex);

        if (rdCheckSex == 'M'){
            man_rd_Check.setChecked(true);
        } else if (rdCheckSex == 'F'){
            wonman_rd_Check.setChecked(true);
        }

        /** 회원 탈퇴 버튼*/
        ImageView withdrawal = view.findViewById(R.id.withdrawal);
        /** 초기화 버튼*/
        ImageView reset_btn = view.findViewById(R.id.reset_btn);
        /** 수정 버튼*/
        ImageView edit_btn = view.findViewById(R.id.edit_btn);

        /** 회원 탈퇴 버튼 클릭시*/
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 다이얼로그 띄워야함
                 * 회원 탈퇴를 진행하시겠습니까? 예
                 * */
                DialogwithdrawalFragment dialogwithdrawalFragment = new DialogwithdrawalFragment();
                dialogwithdrawalFragment.show(getActivity().getSupportFragmentManager(), "dialogwithdrawalFragment");
                dialogwithdrawalFragment.setCancelable(true);
            }
        });

        /** 초기화 버튼 클릭시*/
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_input.setText("" + LoginKioskUser.getInstance().getKioskUser().kuserName);
                phone_input.setText("" + LoginKioskUser.getInstance().getKioskUser().kuserTel);

//                String dateOfBirth = LoginKioskUser.getInstance().getKioskUser().kuserBirthDate;
//                String yearSpinnerPrint = dateOfBirth.substring(0,4);
//                String monthSpinnerPrint = dateOfBirth.substring(5,7);
//                String daySpinnerPrint = dateOfBirth.substring(8,10);

                listYearStatus.setSelection(intYearSpiners-1930);
                spMonthStatus.setSelection(intMonthSpiners-1);
                spDateStatus.setSelection(intDateSpiners-1);
            }
        });

//        checkboxCheeseM.setOnClickListener(new CheckBox.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                if (!checkboxCheese1.isChecked()){
//                    Toast.makeText(getActivity(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        checkboxCheeseF.setOnClickListener(new CheckBox.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                if (!((CheckBox)view).isChecked()){
//                    Toast.makeText(getActivity(), "개인정보취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        /** 수정 버튼 클릭시*/
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Spinner를 통해 입력 받은 생년월일
                 * 1991-08-11 형태로 합쳐주기
                 * */
                String kuserName = name_input.getText().toString();
                String kuserTel = phone_input.getText().toString();   //끝 4자리 전화번호

                String strCorYearBirth = yearSpinner.getSelectedItem().toString();
                String strCorMonthBirth = monthSpinner.getSelectedItem().toString();
                String strCorDateBirth = daySpinner.getSelectedItem().toString();

                /**
                 * 성별 구분
                 */
                char kuserSex = 'M';
                if (rdBtnkuserM.isChecked()){
                    kuserSex = 'M';
                    System.out.println("kuserSex >>> " + kuserSex);
                }else if(rdBtnkuserF.isChecked()){
                    kuserSex = 'F';
                    System.out.println("kuserSex " + kuserSex);
                }else if(!rdBtnkuserM.isChecked()){
                    Toast.makeText(getActivity(), "성별을 체크해주세요", Toast.LENGTH_SHORT).show();
                }else if(!rdBtnkuserF.isChecked()){
                    Toast.makeText(getActivity(), "성별을 체크해주세요", Toast.LENGTH_SHORT).show();
                }

                String kuserBirthDate = strCorYearBirth.substring(0,4) + "-" + strCorMonthBirth.substring(0,2) + "-" + strCorDateBirth.substring(0,2);
                System.out.println("yearSpinner >>> " + strCorYearBirth);
                System.out.println("monthSpinner >>> " + strCorMonthBirth);
                System.out.println("daySpinner >>> " + strCorDateBirth);
                System.out.println("kuserBirthDate >>> " + kuserBirthDate);
                System.out.println("kuserName >>> " + kuserName);
                System.out.println("kuserSex >>> " + kuserSex);
                System.out.println("kuserTel >>> " + kuserTel);

                if (rdBtnkuserM != null || rdBtnkuserF != null || !rdBtnkuserM.isChecked() || !rdBtnkuserF.isChecked()){
                    CorrectionDto correctionDto = new CorrectionDto(kuserBirthDate, kuserName, kuserSex, kuserTel);
                    CallApi.getCorrection(temp, correctionDto).enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            CommonResponse commonResponse = response.body();
                            System.out.println("CommonResponse >>> " + commonResponse);

                            if (response.isSuccessful() && response.body() != null && rdBtnkuserM.isChecked() || rdBtnkuserF.isChecked()){
                                System.out.println("성공여부 success : >>> " + commonResponse.success);
                                System.out.println("성공여부 msg : >>> " + commonResponse.msg);
                                System.out.println("성공여부 code : >>> " + commonResponse.code);
                                System.out.println("성공여부 kuserBirthDate : >>> " + correctionDto.kuserBirthDate);
                                System.out.println("성공여부 nameInput : >>> " + correctionDto.kuserName);
                                System.out.println("성공여부 kuserSex : >>> " + correctionDto.kuserSex);
                                System.out.println("성공여부 phoneInput : >>> " + correctionDto.kuserTel);

                                /**성공 다이얼로그 띄워줘야함
                                 * 수정이 완료되었습니다!
                                 * */

                                /**
                                 * 다이얼로그 띄워주기
                                 */
                                UserEditFragment userEditFragment = new UserEditFragment();
                                userEditFragment.show(getActivity().getSupportFragmentManager(), "userEditFragment");
                                userEditFragment.setCancelable(true);

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
                /*else if(checkbox_m_correct != null && checkbox_w_correct != null){
                    Toast.makeText(getActivity(), "성별을 체크해주세요", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        return view;
    }
}