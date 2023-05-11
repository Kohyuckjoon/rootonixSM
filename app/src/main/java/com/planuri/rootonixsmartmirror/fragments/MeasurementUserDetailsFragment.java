package com.planuri.rootonixsmartmirror.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.ResAnalysisHistoryDto;
import com.planuri.rootonixsmartmirror.dto.ResLoginDto;
import com.planuri.rootonixsmartmirror.dto.ResUsersDto;
import com.planuri.rootonixsmartmirror.fragments.adapter.MeasurementDetailsAdapter;
import com.planuri.rootonixsmartmirror.fragments.adapter.MeasurementUserAdapter;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistory;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistoryDetail;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.ScalpAnalysisData;
import com.planuri.rootonixsmartmirror.model.kioskuser.TelSearchUserItem;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeasurementUserDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeasurementUserDetailsFragment extends Fragment {
    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface
    private static final String TAG = CareModeFragment.class.getSimpleName();


    RecyclerView recyclerView_list; //RecyclerView를 사용
    MeasurementUserAdapter adapter; //Adapter 사용


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MeasurementUserDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeasurementUserDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeasurementUserDetailsFragment newInstance(String param1, String param2) {
        MeasurementUserDetailsFragment fragment = new MeasurementUserDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_measurement_user_details, container, false);

//        ViewGroup fragmentLayout = (ViewGroup) view.findViewById(R.id.)

        /** 로그인한 상태 유지*/
        LoginKioskUser.getInstance().setLogin(true);

        TelSearchUserItem items = new TelSearchUserItem();

        adapter = new MeasurementUserAdapter(getActivity().getApplicationContext());

        /** 정보수정 진입*/
        ImageView iv_changing_information_icon = view.findViewById(R.id.changing_information_icon);

        iv_changing_information_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(16);
            }
        });

        /**현재 날짜 구하기*/
        Date date = new Date();
        /**포맷형식 정해주기*/
        SimpleDateFormat sdfStatus = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c1 = Calendar.getInstance();
        /**현재 년,월 int형으로 변환*/
        int thisYear = Integer.parseInt(sdfStatus.format(c1.getTime()).substring(0,4)); // int형 현재 년도
        int thisMonth = Integer.parseInt(sdfStatus.format(c1.getTime()).substring(5,7)); // int형 현재 년도

        /**
         * Spinner 사용
         * 1. 측정 내역 보기 / 연도 <추후 삭제>
         **/
        List<String> listYears = new ArrayList<String>();

        for(int i = 2020; i <= thisYear; i++) {
            listYears.add(String.valueOf(i)+"년"); //문자로 2020~현재연도까지 담김
        }
        int listLenYears = listYears.size(); //담긴 리스트가 몇개인지
        System.out.println("listLen >>> " + listLenYears);

        Spinner listYearStatus = (Spinner)view.findViewById(R.id.masurement_spinner_year);
        ArrayAdapter<String> yearAdapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listYears);
        listYearStatus.setAdapter(yearAdapters);
        listYearStatus.setSelection(thisYear-2020);

        /**
         * Spinner 사용
         * 2. 측정 내역보기 / 월별
         */
        String[] arrayMonthList = {
                "01월", "02월", "03월", "04월", "05월", "06월", "07월", "08월", "09월", "10월", "11월", "12월"
        };

        List<String> listMonth = new ArrayList<String>();

//        for(int i = 2020; i <= thisYear; i++) {
//            listMonth.add(String.valueOf(i)+"년"); //문자로 1930~2012년까지 담김.
//        }
//        int listLenMonth = listMonth.size(); //담긴 리스트가 몇개인지
//        System.out.println("listLen >>> " + listLenMonth);

        Spinner listMonthStatus = (Spinner)view.findViewById(R.id.masurement_spinner_month);
        ArrayAdapter<String> monthAdapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayMonthList);
        listMonthStatus.setAdapter(monthAdapters);
        listMonthStatus.setSelection(thisMonth-1);

        /**
         * Spinner 사용
         * 1. 측정 내역 보기 / 연도 <추후 삭제>
         **/
//        Spinner yearSpinner = (Spinner) view.findViewById(R.id.spinner_year);
//        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date_year, android.R.layout.simple_spinner_item);
//        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        yearSpinner.setAdapter(yearAdapter);

        /**
         * Spinner 사용
         * 2. 측정 내역보기 / 월별 <추후 삭제>
         */
//        Spinner monthSpinner = (Spinner) view.findViewById(R.id.spinner_month);
//        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date_month, android.R.layout.simple_spinner_item);
//        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        monthSpinner.setAdapter(monthAdapter);

        /** 회원정보 불러오기 */
        TextView kuserName = view.findViewById(R.id.kuser_name);
        TextView kuserTel = view.findViewById(R.id.kuser_tel);
        TextView kuserBirthDate = view.findViewById(R.id.kuser_birth_date);
        TextView kuserSex = view.findViewById(R.id.kuser_sex);

        /** 회원 정보 뿌려주기
         * 이름이 외자일 경우 위쪽 if문 실행 /
         * 이름이 3글자 이상일 경우 아래쪽 else if문 실행
         * */
        if (LoginKioskUser.getInstance().getKioskUser().kuserName.length() >= 3) {
            kuserName.setText(LoginKioskUser.getInstance().getKioskUser().kuserName.substring(0, 1)
                    + "*" + LoginKioskUser.getInstance().getKioskUser().kuserName.substring(2, 3) + " 님");
        } else if (LoginKioskUser.getInstance().getKioskUser().kuserName.length() < 3) {
            kuserName.setText(LoginKioskUser.getInstance().getKioskUser().kuserName.substring(0, 1) + "*" + " 님");
        }

        /** 전화번호 뿌려주기*/
        kuserTel.setText(LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(0,3) + "-" + "****" + "-" +
                LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(7,11));

        /**
         * 생년월일 받아온 값 -> 나이로 변환해주기
         * */
        if (LoginKioskUser.getInstance().getKioskUser().kuserBirthDate != null){

            Calendar current = Calendar.getInstance();
            int currentYear = current.get(Calendar.YEAR);

            int kuserBirth = Integer.parseInt(LoginKioskUser.getInstance().getKioskUser().kuserBirthDate.substring(0,4));
            int result = currentYear - kuserBirth;

            kuserBirthDate.setText(Integer.toString(result) + "세");
        }

        /**
         * 측정 내역 보기
         * 왼쪽 상단 로그인한 사용자 정보 뿌려주기(성별)
         * */
        char kuserSexStatus = LoginKioskUser.getInstance().getKioskUser().kuserSex;
        if (kuserSexStatus=='M'){
            kuserSex.setText("남성");
        } else if (kuserSexStatus=='F'){
            kuserSex.setText("여성");
        }


        /** recyclerView Setting*/
        recyclerView_list = (RecyclerView) view.findViewById(R.id.kuser_measurement_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView_list.setLayoutManager(layoutManager);

        String strTelNum = LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(7,11);
        System.out.println("strTelNum >>> " + strTelNum);

        /** 회원번호*/
        long regKioskIdx = 4;

        long temp = LoginKioskUser.getInstance().getKioskUser().kuserIdx;

        String strTemp = String.valueOf(temp);
        System.out.println("strTemp >>> " + strTemp);

        /**
         * 로그아웃 버튼
         */
        ImageView measurement_logout = view.findViewById(R.id.measurement_logout);
        measurement_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 서비스 선택
         */
        ImageView measurement_user_service = view.findViewById(R.id.measurement_user_service);
        measurement_user_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "서비스 선택 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).fragmentView(4);
            }
        });

//        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        /**
         * 검색 결과 보여주기
         */
        listMonthStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                /**
                 * Spinner를 통해서 가져온 값 불러오기
                 */
//                String yearMonth1 = yearSpinner.getSelectedItem().toString().substring(0,4);
                String yearMonth1 = listYearStatus.getSelectedItem().toString().substring(0,4);
                System.out.println("yearMonth >>> " + yearMonth1);

                String yearMonth2 = listMonthStatus.getSelectedItem().toString().substring(0,2);
                System.out.println("yearMonth2 >>> " + yearMonth2);

                /**
                 * 입력받은 연도 + 월 합쳐주기
                 * 스마트미러 측정 내역 yearMonth = 2022-11 형식
                 */
                String yearMonthResult = yearMonth1 + "-" + yearMonth2;
                System.out.println("yearMonthResult >>> " + yearMonthResult);

                recyclerView_list.clearOnChildAttachStateChangeListeners();
                /** 로그인한 사용자의 측정 내역 불러오기*/
                CallApi.getAnalysisHistory("" + strTemp, ""+ yearMonthResult).enqueue(new Callback<ResAnalysisHistoryDto>() {
                    @Override
                    public void onResponse(Call<ResAnalysisHistoryDto> call, Response<ResAnalysisHistoryDto> response) {
                        if(response.isSuccessful() && response.body() != null) {
//                    ResAnalysisHistoryDto resAnalysisHistoryDto = response.body();

                            ResAnalysisHistoryDto resAnalysisHistoryDto = response.body();
                            System.out.println("resAnalysisHistoryDto >>> " + resAnalysisHistoryDto);

                            Log.e("log test >>> ", response.body() + "");

                            /**검색 결과가 여러개가 쌓이지 않도록*/
                            adapter.clearItem();
                            recyclerView_list.setAdapter(adapter);

                            for (int i = 0; i<resAnalysisHistoryDto.list.size(); i++){
                                KioskAnalysisHistory kioskAnalysisHistory = new KioskAnalysisHistory();
                                kioskAnalysisHistory.analyzedDate = resAnalysisHistoryDto.list.get(i).analyzedDate; //일시
                                kioskAnalysisHistory.deadSkinPt = resAnalysisHistoryDto.list.get(i).deadSkinPt;
                                kioskAnalysisHistory.hairNumPt = resAnalysisHistoryDto.list.get(i).hairNumPt;
                                kioskAnalysisHistory.hairThickPt = resAnalysisHistoryDto.list.get(i).hairThickPt;
                                kioskAnalysisHistory.sebumPt = resAnalysisHistoryDto.list.get(i).sebumPt;
                                kioskAnalysisHistory.sensitivityPt = resAnalysisHistoryDto.list.get(i).sensitivityPt;
                                kioskAnalysisHistory.totalPt = resAnalysisHistoryDto.list.get(i).totalPt;
                                kioskAnalysisHistory.kanalyzedIdx = resAnalysisHistoryDto.list.get(i).kanalyzedIdx;
                                System.out.println("kanalyzedIdx >>> " + resAnalysisHistoryDto.list.get(i).kanalyzedIdx);

                                adapter.addItem(kioskAnalysisHistory);
                            }


                            /**
                             *  analyzedDate.setText(item.analyzedDate);        //일시
                             *  analyzedDayWeek.setText(item.analyzedDate);     //요일
                             *  analyzedTime.setText(item.analyzedDate);        //시간
                             */

                        }else {  /** HTTP code: 200 이 아니거나 response body 가 null 인 경우*/
                            switch (response.code()) {
                                case 500: { /** 서버에서 Exception 발생 시 500으로 보냄 */
                                    CommonResponse errorResponse = CustomRestClient.getInstance().getErrorResponse(response);
                                    System.out.println("errorResponse ? >>> " + errorResponse.msg);
                                    Toast.makeText(getActivity().getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
//                                    resultText.setText(errorResponse.msg);
                                    break;
                                }
                                default: {  /** 그 외의 HTTP 오류 */
                                    try {
                                        Log.e(TAG, "code: " + response.code() + "\nmsg: " + response.errorBody().string());
//                                        Toast.makeText(getActivity().getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                    } catch(Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(getActivity().getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        CallApi.RequestLogin("" + items.kuserName, "" + items.kuserTel).enqueue(new Callback<ResLoginDto>() {
//            @Override
//            public void onResponse(Call<ResLoginDto> call, Response<ResLoginDto> response) {
//                if (response.isSuccessful() && response.body() != null){/** HTTP 호출 성공인 Code 200을 받았을 경우(실제 200~300이지만 200만 사용)*/
//                    Log.d("aaa", response.body().toString() + "");
//
//                    ResLoginDto loginDto = response.body();
//
//                    TelSearchUserItem searchItem = new TelSearchUserItem();
//
//                    /**데이터 내용 확인*/
//                    System.out.println("response.body() " + response.body());
//                    System.out.println("loginDto.msg >>>>>" + loginDto.msg);
//                    System.out.println("loginDto.success >>>>>" + loginDto.success);
//                    System.out.println("loginDto.code >>>>>" + loginDto.code);
//
//                    /**
//                     * 넘어온 데이터 담기
//                     * loginDto.data.createDate, kuserName, kuserTel, kuserBirthDate, kuserSex
//                     */
//                    LoginKioskUser.getInstance().setKioskUser(loginDto.data);
//
//                    /**로그인 상태 변경*/
//                    LoginKioskUser.getInstance().setLogin(true);
//
//                    ((MainActivity)getActivity()).fragmentView(4);
//                    Toast.makeText(getActivity(), items.kuserName + "님, 환영합니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResLoginDto> call, Throwable t) {
//
//            }
//        });

        /** RecyclerView Grid 아이템 클릭 이벤트 */
        adapter.setOnGridItemClickListener(new MeasurementUserAdapter.OnGridItemClickListener() {
            @Override
            public void onGridItemClicked(View v, int position, KioskAnalysisHistory items) {
                System.out.println("RecyclerView Click ???_>? >>> ");

                /**로그인 상태 변경*/
                LoginKioskUser.getInstance().setLogin(true);

                System.out.println("items.kanalyzedIdx ><>>" + items.kanalyzedIdx);
                System.out.println("items.kanalyzedIdx ><>>" + items.deadSkinPt);
                System.out.println("items.kanalyzedIdx ><>>" + items.deadSkinPt);
                System.out.println("items.kanalyzedIdx ><>>" + items.deadSkinPt);

                LoginKioskUser.getInstance().setAnalysisHistory(items);
                System.out.println("items >>> " + items);
                long kuserIdx = LoginKioskUser.getInstance().getKioskUser().kuserIdx;

                CallApi.getAnalysisDetail(kuserIdx, items.kanalyzedIdx).enqueue(new Callback<KioskAnalysisHistoryDetail>() {
                    @Override
                    public void onResponse(Call<KioskAnalysisHistoryDetail> call, Response<KioskAnalysisHistoryDetail> response) {
                        KioskAnalysisHistoryDetail kioskAnalysisHistoryDetail = response.body();
                        /** 예 */
                        ScalpAnalysisData scalpAnalysisData = new ScalpAnalysisData();

                        scalpAnalysisData.total.deadskin = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.deadSkinPt;
                        scalpAnalysisData.total.sebum = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.sebumPt;
                        scalpAnalysisData.total.sensitive = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.sensitivityPt;
                        scalpAnalysisData.total.num_hairs = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.hairNumPt;
                        scalpAnalysisData.total.thickness = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.hairThickPt;
                        scalpAnalysisData.total.short_explain = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.diagnosisDesc;
                        scalpAnalysisData.total.total_explain = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.totalDesc;
                        scalpAnalysisData.total.expert_suggest = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.expertSuggest;
                        scalpAnalysisData.total.expert_list.add(kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.expertSuggestItem1);
                        scalpAnalysisData.total.expert_list.add(kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.expertSuggestItem2);
                        scalpAnalysisData.total.title = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.diagnosisName; //진단명

                        scalpAnalysisData.total.images_result = kioskAnalysisHistoryDetail.data.analyzedImagesList;

                        LoginKioskUser.getInstance().isHistoryCall = true;

                        LoginKioskUser.getInstance().setAnalysisData(scalpAnalysisData);

                        ((MainActivity)getActivity()).fragmentView(5);

                    }

                    @Override
                    public void onFailure(Call<KioskAnalysisHistoryDetail> call, Throwable t) {
                        Toast.makeText(getActivity(), "상세 내역을 불러오는 중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });


//                ((MainActivity)getActivity()).fragmentView(6);
            }
        });

        return view;
    }
}