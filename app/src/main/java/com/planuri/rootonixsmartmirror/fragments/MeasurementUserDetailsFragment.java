package com.planuri.rootonixsmartmirror.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.ResAnalysisHistoryDto;
import com.planuri.rootonixsmartmirror.fragments.adapter.MeasurementUserAdapter;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistory;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistoryDetail;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.ScalpAnalysisData;
import com.planuri.rootonixsmartmirror.model.kioskuser.TelSearchUserItem;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.Utils;
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

        /**
         * 검색 년도 Dropdown Spinner
         **/
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> yearsList = new ArrayList<>();
        final int startYear = 2020;
        final String CONST_YEAR_STR = " 년";

        for(int i = startYear; i <= thisYear; i++) {
            yearsList.add(Integer.toString(i) + CONST_YEAR_STR);  //서비스 시작년도 ~ 현재까지만
        }

        Spinner yearsSpinner = (Spinner)view.findViewById(R.id.masurement_spinner_year);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, yearsList);
        yearsSpinner.setAdapter(yearsAdapter);
        yearsSpinner.setSelection(thisYear - startYear);    // 올해로 기본 선택

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

        /** 전화번호 및 나이 */
        kuserTel.setText(LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(0,3) + "-" + "****" + "-" +
                LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(7,11));
        kuserBirthDate.setText(Utils.calculateAge(LoginKioskUser.getInstance().getKioskUser().kuserBirthDate));


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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_list.setLayoutManager(layoutManager);

        /** 회원번호*/
        final long lUserNum = LoginKioskUser.getInstance().getKioskUser().kuserIdx;

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
         * 년도 선택 시 해당 년도의 측정 이력 요청 및 recyclerView update
         */
        yearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedYear = parent.getItemAtPosition(position).toString();
                selectedYear = selectedYear.replace(CONST_YEAR_STR, ""); // " 년"을 제거

                //recyclerView_list.clearOnChildAttachStateChangeListeners();
                /** 로그인한 사용자의 측정 내역 불러오기*/
                CallApi.getAnalysisHistory(lUserNum, ""+ selectedYear).enqueue(new Callback<ResAnalysisHistoryDto>() {
                    @Override
                    public void onResponse(Call<ResAnalysisHistoryDto> call, Response<ResAnalysisHistoryDto> response) {
                        if(response.isSuccessful() && response.body() != null) {
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
                                kioskAnalysisHistory.diagnosisName = resAnalysisHistoryDto.list.get(i).diagnosisName;
                                kioskAnalysisHistory.kanalyzedIdx = resAnalysisHistoryDto.list.get(i).kanalyzedIdx;
                                System.out.println("kanalyzedIdx >>> " + resAnalysisHistoryDto.list.get(i).kanalyzedIdx);

                                adapter.addItem(kioskAnalysisHistory);
                            }

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

                long kuserIdx = 0;
                if(LoginKioskUser.getInstance().isLogin()) {
                    kuserIdx = LoginKioskUser.getInstance().getKioskUser().kuserIdx;
                }
                else {
                    Toast.makeText(getActivity(), "사용자 로그인 상태가 아닙니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                        scalpAnalysisData.total.analyzedDate = kioskAnalysisHistoryDetail.data.kioskAnalysisDetail.analyzedDate;    //진단Datetime

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