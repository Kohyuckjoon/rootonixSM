package com.planuri.rootonixsmartmirror.fragments;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogCompletFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogFullScreenFragment;
import com.planuri.rootonixsmartmirror.dto.ResLoginDto;
import com.planuri.rootonixsmartmirror.dto.ResScalpAnalysisDto;
import com.planuri.rootonixsmartmirror.fragments.adapter.MeasurementUserAdapter;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistory;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistoryDetail;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.RecentAnalysis;
import com.planuri.rootonixsmartmirror.model.kioskuser.ScalpAnalysisData;
import com.planuri.rootonixsmartmirror.model.kioskuser.TelSearchUserItem;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.ImageDownloadByUrl;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;
import com.planuri.rootonixsmartmirror.util.retrofit.ScalpAnalysisRetrofit;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemEvaluationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemEvaluationFragment extends BaseFragment implements View.OnClickListener {
    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface
    MeasurementUserAdapter adapter; //Adapter 사용


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ScalpAnalysisData mAnalysisData = LoginKioskUser.getInstance().getAnalysisData();
    private RecentAnalysis mRecentAnalysis;

    private static String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String SAVE_FOLDER = "/TempScalpImage/";
    private static String SAVE_PATH = EXTERNAL_STORAGE + SAVE_FOLDER;

    private List<ImageView> imageViews = null;
    private List<Bitmap> analyzedImages = null;
    private final int analyzedImgSize = 6;
    private ProgressBar mBarDeadSkinBefore;
    private ProgressBar mBarSensitivityBefore;
    private ProgressBar mBarHairThickBefore;
    private ProgressBar mBarSebumBefore;
    private ProgressBar mBarNumHairsBefore;

    private TextView tv_eval_date;

    public ItemEvaluationFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemEvaluationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemEvaluationFragment newInstance(String param1, String param2) {
        ItemEvaluationFragment fragment = new ItemEvaluationFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_evaluation, container, false);

        TelSearchUserItem items = new TelSearchUserItem();

        /**로그인 상태 변경*/
        LoginKioskUser.getInstance().setLogin(true);

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

        /** 서비스 선택 화면
         * 왼쪽 상단 로그인한 사용자 정보 뿌려주기(성별)
         * */
        char kuserSexStatus = LoginKioskUser.getInstance().getKioskUser().kuserSex;
        if (kuserSexStatus=='M'){
            kuserSex.setText("남성");
        } else if (kuserSexStatus=='F'){
            kuserSex.setText("여성");
        }

        long kuserIdx = LoginKioskUser.getInstance().getKioskUser().kuserIdx;

        String strTemp = String.valueOf(kuserIdx);
        System.out.println("strTemp >>> " + strTemp);

//        kanalyzedIdx = LoginKioskUser.getInstance().getKioskAnalysisHistory().kanalyzedIdx;

        ResScalpAnalysisDto resData;
        /**
         * 서버에 있는 데이터 불려오기
         **/
//        int kanalyzedIdx = LoginKioskUser.getInstance().getKioskAnalysisHistory().kanalyzedIdx;
//        System.out.println("LoginKioskUser.kanalyzedIdx >>> " + kanalyzedIdx);

        ImageView measurementUserserviceSelect = view.findViewById(R.id.measurement_userservice_select);
        ImageView changingInfoBtn = view.findViewById(R.id.changing_info_btn);
        ImageView serviceIconMs = view.findViewById(R.id.service_icon_ms);
        ImageView btnTotalEval = view.findViewById(R.id.btnTotalEval);

        /** 항목별 평가 > 종합평가 이동 */
        btnTotalEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(6); //종합평가
            }
        });

        measurementUserserviceSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(4);
            }
        });

        changingInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(16);
            }
        });

        serviceIconMs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
            }
        });

        /**
         * 로그인 Call API
         * */
        CallApi.RequestLogin("" + items.kuserName, "" + items.kuserTel).enqueue(new Callback<ResLoginDto>() {
            @Override
            public void onResponse(Call<ResLoginDto> call, Response<ResLoginDto> response) {
                if (response.isSuccessful() && response.body() != null){/** HTTP 호출 성공인 Code 200을 받았을 경우(실제 200~300이지만 200만 사용)*/
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
                }
            }

            @Override
            public void onFailure(Call<ResLoginDto> call, Throwable t) {

            }
        });

        //두피 분석 비트맵 이미지 원본
        analyzedImages = new ArrayList<>();

        /** 두피 분석 이미지뷰(작은 사이즈) */
        imageViews = new ArrayList<>();
        imageViews.add(view.findViewById(R.id.evaluation_blank1));  //앞머리1
        imageViews.add(view.findViewById(R.id.evaluation_blank2));  //앞머리2
        imageViews.add(view.findViewById(R.id.evaluation_blank9));  //앞머리3
        imageViews.add(view.findViewById(R.id.evaluation_blank10)); //정수리1
        imageViews.add(view.findViewById(R.id.evaluation_blank3));  //정수리2
        imageViews.add(view.findViewById(R.id.evaluation_blank4));  //정수리3

        //클릭 리스너 등록
        for(ImageView imgView : imageViews) {
            imgView.setOnClickListener(this);
        }

        //분석데이터가 없으면 빈 화면을 보여줌
        if(mAnalysisData == null) return view;

        /** 로그아웃 Btn*/
        ImageView service_icon = view.findViewById(R.id.service_icon_ms);
        service_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
//                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 서비스 선택 화면으로 이동
         */
        ImageView measurement_userservice_select = view.findViewById(R.id.measurement_userservice_select);
        measurement_userservice_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "서비스 선택 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).fragmentView(4);
            }
        });

        /**
         * 항목별 평가 > 정보 수정 화면
         */
        ImageView changing_info_btn = view.findViewById(R.id.changing_info_btn);
        changing_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(16);
            }
        });

        /** 분석 날짜 */
        tv_eval_date = view.findViewById(R.id.tv_eval_date);
        if(mAnalysisData.total.analyzedDate != null && mAnalysisData.total.analyzedDate.length() > 3) {
            tv_eval_date.setText(mAnalysisData.total.analyzedDate.substring(0, mAnalysisData.total.analyzedDate.length() - 3));
        }

        /** 이미지 저장된 URL 로 분석 이미지 가져오기 */
        // 이미지 다운로드 클래스 생성 및 리스너 작성
        ImageDownloadByUrl imageUrlDown = new ImageDownloadByUrl(new ImageDownloadByUrl.OnPostDownLoadListener() {
            @Override
            public void onPost(List<Bitmap> bitmaps) {

                for(int i = 0; i < bitmaps.size(); i++) {

                    analyzedImages.add(bitmaps.get(i)); //원본 이미지
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmaps.get(i), 160, 120, true);
                    imageViews.get(i).setImageBitmap(resizedBitmap);

                    /** 파일 저장은 필요 또는 요청이 있을 시 */
                    /*try {
                        File file = new File(SAVE_PATH + "result_" + i + ".jpg");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch(FileNotFoundException e) {
                        e.printStackTrace();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }*/

                }
            }
        });

        // url 전달
        String strUrlList[] = new String[mAnalysisData.total.images_result.size()];
        for(int i = 0; i < mAnalysisData.total.images_result.size(); i++) {
            strUrlList[i] = mAnalysisData.total.images_result.get(i);
        }

        //프로그래스바
        ProgressBar barDeadSkin = view.findViewById(R.id.barDeadSkin);
        ProgressBar barSensitivity = view.findViewById(R.id.barSensitivity);
        ProgressBar barHairThick = view.findViewById(R.id.barHairThick);
        ProgressBar barSebum = view.findViewById(R.id.barSebum);
        ProgressBar barNumHairs = view.findViewById(R.id.barNumHairs);

        //이전 평가 프로그래스바
        mBarDeadSkinBefore = view.findViewById(R.id.barDeadSkinBefore);
        mBarSensitivityBefore = view.findViewById(R.id.barSensitivityBefore);
        mBarHairThickBefore = view.findViewById(R.id.barHairThickBefore);
        mBarSebumBefore = view.findViewById(R.id.barSebumBefore);
        mBarNumHairsBefore = view.findViewById(R.id.barNumHairsBefore);

        //항목 점수
        TextView text1 = (TextView)view.findViewById(R.id.dead_skin_sc_pt);
        text1.setText(String.valueOf(mAnalysisData.total.deadskin));
        TextView text2 = (TextView)view.findViewById(R.id.sensitivity_sc_pt);
        text2.setText(String.valueOf(mAnalysisData.total.sensitive));
        TextView text3 = (TextView)view.findViewById(R.id.hair_thick_sc_pt);
        text3.setText(String.valueOf(mAnalysisData.total.thickness));
        TextView text4 = (TextView)view.findViewById(R.id.sebum_sc_pt);
        text4.setText(String.valueOf(mAnalysisData.total.sebum));
        TextView text5 = (TextView)view.findViewById(R.id.hair_num_sc_pt);
        text5.setText(String.valueOf(mAnalysisData.total.num_hairs));

        //프로그래스바 실제 수치 및 그래프 표현 #각질
        if (mAnalysisData.total.deadskin >= 1 && mAnalysisData.total.deadskin <= 7){ // 1~7(붉은색)
            barDeadSkin.setProgress(mAnalysisData.total.deadskin);
            barDeadSkin.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#DC143C"))); // ProgressBar의 색상을 빨강으로 변경
        } else if(mAnalysisData.total.deadskin >= 8 && mAnalysisData.total.deadskin <= 12){// 8~12(주황색)
            barDeadSkin.setProgress(mAnalysisData.total.deadskin);
            barDeadSkin.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA07A"))); // ProgressBar의 색상을 주황으로 변경
        } else if(mAnalysisData.total.deadskin >= 13 && mAnalysisData.total.deadskin <= 17){ // 13~17(녹색)
            barDeadSkin.setProgress(mAnalysisData.total.deadskin);
            barDeadSkin.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F"))); // ProgressBar의 색상을 녹색으로 변경
        } else if(mAnalysisData.total.deadskin >= 18 && mAnalysisData.total.deadskin <= 20){ // 18~20(파란색
            barDeadSkin.setProgress(mAnalysisData.total.deadskin);
            barDeadSkin.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#87CEEB"))); // ProgressBar의 색상을 파랑으로 변경
        }

        //프로그래스바 실제 수치 및 그래프 표현 #민감도
        if (mAnalysisData.total.sensitive >= 1 && mAnalysisData.total.sensitive <= 7){ // 1~7(붉은색)
            barSensitivity.setProgress(mAnalysisData.total.sensitive);
            barSensitivity.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#DC143C"))); // ProgressBar의 색상을 빨강으로 변경
        } else if(mAnalysisData.total.sensitive >= 8 && mAnalysisData.total.sensitive <= 12){// 8~12(주황색)
            barSensitivity.setProgress(mAnalysisData.total.sensitive);
            barSensitivity.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA07A"))); // ProgressBar의 색상을 주황으로 변경
        } else if(mAnalysisData.total.sensitive >= 13 && mAnalysisData.total.sensitive <= 17){ // 13~17(녹색)
            barSensitivity.setProgress(mAnalysisData.total.sensitive);
            barSensitivity.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F"))); // ProgressBar의 색상을 녹색으로 변경
        } else if(mAnalysisData.total.sensitive >= 18 && mAnalysisData.total.sensitive <= 20){ // 18~20(파란색
            barSensitivity.setProgress(mAnalysisData.total.sensitive);
            barSensitivity.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#87CEEB"))); // ProgressBar의 색상을 파랑으로 변경
        }

        //프로그래스바 실제 수치 및 그래프 표현 #모발 두께
        if (mAnalysisData.total.thickness >= 1 && mAnalysisData.total.thickness <= 7){ // 1~7(붉은색)
            barHairThick.setProgress(mAnalysisData.total.thickness);
            barHairThick.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#DC143C"))); // ProgressBar의 색상을 빨강으로 변경
        } else if(mAnalysisData.total.thickness >= 8 && mAnalysisData.total.thickness <= 12){ // 8~12(주황색)
            barHairThick.setProgress(mAnalysisData.total.thickness);
            barHairThick.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA07A"))); // ProgressBar의 색상을 주황으로 변경
        } else if(mAnalysisData.total.thickness >= 13 && mAnalysisData.total.thickness <= 17){ // 13~17(녹색)
            barHairThick.setProgress(mAnalysisData.total.thickness);
            barHairThick.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F"))); // ProgressBar의 색상을 녹색으로 변경
        } else if(mAnalysisData.total.thickness >= 18 && mAnalysisData.total.thickness <= 20){// 18~20(파란색
            barHairThick.setProgress(mAnalysisData.total.thickness);
            barHairThick.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#87CEEB"))); // ProgressBar의 색상을 파랑으로 변경
        }

        //프로그래스바 실제 수치 및 그래프 표현 #유분
        if (mAnalysisData.total.sebum >= 1 && mAnalysisData.total.sebum <= 7){ // 1~7(붉은색)
            barSebum.setProgress(mAnalysisData.total.sebum);
            barSebum.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#DC143C"))); // ProgressBar의 색상을 빨강으로 변경
        } else if(mAnalysisData.total.sebum >= 8 && mAnalysisData.total.sebum <= 12){ // 8~12(주황색)
            barSebum.setProgress(mAnalysisData.total.sebum);
            barSebum.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA07A"))); // ProgressBar의 색상을 주황으로 변경
        } else if(mAnalysisData.total.sebum >= 13 && mAnalysisData.total.sebum <= 17){ // 13~17(녹색)
            barSebum.setProgress(mAnalysisData.total.sebum);
            barSebum.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F"))); // ProgressBar의 색상을 녹색으로 변경
        } else if(mAnalysisData.total.sebum >= 18 && mAnalysisData.total.sebum <= 20){// 18~20(파란색
            barSebum.setProgress(mAnalysisData.total.sebum);
            barSebum.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#87CEEB"))); // ProgressBar의 색상을 파랑으로 변경
        }

        //프로그래스바 실제 수치 및 그래프 표현 #모낭 당 모발 수
        if (mAnalysisData.total.num_hairs >= 1 && mAnalysisData.total.num_hairs <= 7){ // 1~7(붉은색)
            barNumHairs.setProgress(mAnalysisData.total.num_hairs);
            barNumHairs.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#DC143C"))); // ProgressBar의 색상을 빨강으로 변경
        } else if(mAnalysisData.total.num_hairs >= 8 && mAnalysisData.total.num_hairs <= 12){ // 8~12(주황색)
            barNumHairs.setProgress(mAnalysisData.total.num_hairs);
            barNumHairs.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFA07A"))); // ProgressBar의 색상을 주황으로 변경
        } else if(mAnalysisData.total.num_hairs >= 13 && mAnalysisData.total.num_hairs <= 17){ // 13~17(녹색)
            barNumHairs.setProgress(mAnalysisData.total.num_hairs);
            barNumHairs.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F"))); // ProgressBar의 색상을 녹색으로 변경
        } else if(mAnalysisData.total.num_hairs >= 18 && mAnalysisData.total.num_hairs <= 20){ // 18~20(파란색
            barNumHairs.setProgress(mAnalysisData.total.num_hairs);
            barNumHairs.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#87CEEB"))); // ProgressBar의 색상을 파랑으로 변경
        }

        //이전 평가 요청
        if(!LoginKioskUser.getInstance().isHistoryCall) {
            CustomRestClient.getInstance().getApiInterface().getRecentAnalysis(LoginKioskUser.getInstance().getKioskUser().kuserIdx)
                    .enqueue(new Callback<RecentAnalysis>() {
                        @Override
                        public void onResponse(Call<RecentAnalysis> call, Response<RecentAnalysis> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                mRecentAnalysis = response.body();
                                setBeforeProgressBar(true);
                            } else {  //HTTP 오류(500)
                                Toast.makeText(getActivity(), "이전 평가 데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<RecentAnalysis> call, Throwable t) {
                            Toast.makeText(getActivity(), "이전 평가 데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        //이전평가 비교 스위치 리스너
        Switch evSwitch = view.findViewById(R.id.previous_evaluation);
        evSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setBeforeProgressBar(b);
            }
        });

        imageUrlDown.execute(strUrlList);

        return view;
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if (analyzedImages == null || analyzedImages.size() == 0 || analyzedImages.size() < analyzedImgSize) {
            Toast.makeText(getActivity(), "이미지 불러오기 오류.", Toast.LENGTH_SHORT).show();
            return;
        }

        DialogFullScreenFragment bDialogFragment = new DialogFullScreenFragment();
        Bundle args = new Bundle();

        switch (viewId) {
            case R.id.evaluation_blank1:    // 앞머리 1
                args.putParcelable("image", (analyzedImages.get(0)));
                break;
            case R.id.evaluation_blank2:    // 앞머리 2
                args.putParcelable("image", (analyzedImages.get(1)));
                break;
            case R.id.evaluation_blank9:    // 앞머리 3
                args.putParcelable("image", (analyzedImages.get(2)));
                break;
            case R.id.evaluation_blank10:   // 정수리 1
                args.putParcelable("image", (analyzedImages.get(3)));
                break;
            case R.id.evaluation_blank3:    //정수리 2
                args.putParcelable("image", (analyzedImages.get(4)));
                break;
            case R.id.evaluation_blank4:    //정수리 3
                args.putParcelable("image", (analyzedImages.get(5)));
                break;
            default:    //앞머리1~3, 정수리1~3을 클릭한게 아니라면 #1A1A1A 색으로 비트맵 생성
                int width = 1280;
                int height = 720;
                int color = 0xFF1A1A1A; // RGB 값 1A1A1A를 16진수로 나타낸 값

                Bitmap defaultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                defaultBitmap.eraseColor(color);
                args.putParcelable("image", (defaultBitmap));
                break;
        }

        bDialogFragment.setArguments(args);
        bDialogFragment.show(getFragmentManager(), "dialog");
    }

    private void setBeforeProgressBar(boolean visible) {
        if(visible && (LoginKioskUser.getInstance().isHistoryCall == false)) {
            mBarDeadSkinBefore.setProgress(mRecentAnalysis.data.deadSkinPt);
            mBarSensitivityBefore.setProgress(mRecentAnalysis.data.sensitivityPt);
            mBarHairThickBefore.setProgress(mRecentAnalysis.data.hairThickPt);
            mBarSebumBefore.setProgress(mRecentAnalysis.data.sebumPt);
            mBarNumHairsBefore.setProgress(mRecentAnalysis.data.hairNumPt);
        }
        else {
            mBarDeadSkinBefore.setProgress(0);
            mBarSensitivityBefore.setProgress(0);
            mBarHairThickBefore.setProgress(0);
            mBarSebumBefore.setProgress(0);
            mBarNumHairsBefore.setProgress(0);
        }
    }
}