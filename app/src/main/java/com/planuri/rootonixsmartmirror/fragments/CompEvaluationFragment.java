package com.planuri.rootonixsmartmirror.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogFullScreenFragment;
import com.planuri.rootonixsmartmirror.dialog.DialogRecommendProduct;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.ScalpAnalysisData;
import com.planuri.rootonixsmartmirror.util.Const;
import com.planuri.rootonixsmartmirror.util.Util;
import com.planuri.rootonixsmartmirror.util.Utils;
import com.planuri.rootonixsmartmirror.util.graphic.RadarChartDrawer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompEvaluationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompEvaluationFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ScalpAnalysisData mAnalysisData = LoginKioskUser.getInstance().getAnalysisData();

    /** 종합평가의 타이핑 효과 관련 */
    private Handler typingDelayHandler = new Handler();
    private Runnable typingRunnable;
    private int totalPos = 0;    //타이핑 효과 시 totalText 현재 위치
    String totalText;
    TextView totalDesc; //종합평가 텍스트뷰
    ScrollView totalDescScroll;   //종합평가 텍스트뷰의 스크롤뷰

    /** 레이더 차트 관련 */
    private ImageView iv_radar_chart_big;

    //추천제품 버튼
    private ImageView iv_product_recommend;

    public CompEvaluationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompEvaluationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompEvaluationFragment newInstance(String param1, String param2) {
        CompEvaluationFragment fragment = new CompEvaluationFragment();
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
        View view = inflater.inflate(R.layout.fragment_comp_evaluation, container, false);

        ImageView itemEvaluationFont = (ImageView) view.findViewById(R.id.item_evaluation_font);
        totalDesc = view.findViewById(R.id.total_desc);
        totalDescScroll = view.findViewById(R.id.total_desc_scroll);

        /**
         * 서비스 선택 화면
         */
        ImageView measurement_service_out = view.findViewById(R.id.measurement_service_out);
        measurement_service_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "서비스 선택 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).fragmentView(4);
            }
        });

        /**
         * 종합평가 화면 > 정보 수정
         */
        ImageView changing_information_btn = view.findViewById(R.id.changing_information_btn);
        changing_information_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(16);
            }
        });

        /**
         * 로그아웃 버튼
         */
        ImageView service_logout_btn = view.findViewById(R.id.service_logout_btn);
        service_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /** 회원정보 불러오기 */
        TextView kuserName = view.findViewById(R.id.kuser_name);
        TextView tv_scalp_front_head = view.findViewById(R.id.tv_scalp_front_head);
        TextView kuserTel = view.findViewById(R.id.kuser_tel);
        TextView kuserBirthDate = view.findViewById(R.id.kuser_birth_date);
        TextView kuserSex = view.findViewById(R.id.kuser_sex);

        /** 회원 정보 뿌려주기
         * 이름이 외자일 경우 위쪽 if문 실행 /
         * 이름이 3글자 이상일 경우 아래쪽 else if문 실행
         * */
        String userNameBlinded = "";
        if (LoginKioskUser.getInstance().getKioskUser().kuserName.length() >= 3) {
            userNameBlinded = LoginKioskUser.getInstance().getKioskUser().kuserName.substring(0, 1)
                    + "*" + LoginKioskUser.getInstance().getKioskUser().kuserName.substring(2, 3) + " 님";
        } else if (LoginKioskUser.getInstance().getKioskUser().kuserName.length() < 3) {
            userNameBlinded = LoginKioskUser.getInstance().getKioskUser().kuserName.substring(0, 1) + "*" + " 님";
        }

        kuserName.setText(userNameBlinded);
        tv_scalp_front_head.setText(userNameBlinded + "은");

        /** 전화번호 및 나이 */
        kuserTel.setText(LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(0,3) + "-" + "****" + "-" +
                LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(7,11));
        kuserBirthDate.setText(Utils.calculateAge(LoginKioskUser.getInstance().getKioskUser().kuserBirthDate));

        /** 서비스 선택 화면
         * 왼쪽 상단 로그인한 사용자 정보 뿌려주기(성별)
         * */
        char kuserSexStatus = LoginKioskUser.getInstance().getKioskUser().kuserSex;
        if (kuserSexStatus=='M'){
            kuserSex.setText("남성");
        } else if (kuserSexStatus=='F'){
            kuserSex.setText("여성");
        }

        // 타이핑 효과에 사용할 Text(종합평가)
        totalText = mAnalysisData.total.title + "\n\n" + mAnalysisData.total.short_explain + "\n\n" + mAnalysisData.total.total_explain;

        TextView expertSuggest = view.findViewById(R.id.expert_suggest);
        expertSuggest.setText(mAnalysisData.total.expert_suggest);

        TextView expert_suggest1 = view.findViewById(R.id.expert_suggest1);
        expert_suggest1.setText(mAnalysisData.total.expert_list.get(0) + "\n");
        TextView expert_suggest2 = view.findViewById(R.id.expert_suggest2);
        expert_suggest2.setText(mAnalysisData.total.expert_list.get(1) + "\n");
        TextView troubleScalpFont = view.findViewById(R.id.trouble_scalp_font);
        troubleScalpFont.setText("" + mAnalysisData.total.title);

        /** 두피 타입 */
        TextView tv_scalp_type_code = view.findViewById(R.id.tv_scalp_type_code);
        tv_scalp_type_code.setText(Const.ScalpType.getTypeCode(mAnalysisData.total.title));

        iv_product_recommend = view.findViewById(R.id.iv_product_recommend);
        /** 추천 제품 버튼 클릭 */
        iv_product_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogRecommendProduct dialogRecommendProduct = new DialogRecommendProduct();
                dialogRecommendProduct.show(getActivity().getSupportFragmentManager(), "recommend");
            }
        });

        itemEvaluationFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(5);
            }
        });

        iv_radar_chart_big = view.findViewById(R.id.iv_radar_chart_big);

        iv_radar_chart_big.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_radar_chart_big.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int width = iv_radar_chart_big.getWidth();
                int height = iv_radar_chart_big.getHeight();

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);

                // 점수 정보
                ArrayList<Integer> scores = new ArrayList<>();
                scores.add(mAnalysisData.total.deadskin);   //각질
                scores.add(mAnalysisData.total.sebum);      //유분
                scores.add(mAnalysisData.total.thickness);  //모발두께
                scores.add(mAnalysisData.total.num_hairs);  //모낭당 모발 수
                scores.add(mAnalysisData.total.sensitive);  //민감도
                /*int s1=20, s2=20, s3=20, s4=20, s5=20;
                scores.add(s1);
                scores.add(s2);
                scores.add(s3);
                scores.add(s4);
                scores.add(s5);*/

                /** RadarChartDrawer.RadarAttribute 의 diameter 값을 조절하여 레이더 차트의 크기 설정, vertexRadius 로 꼭지점 반지름 크기 설정, vertexColor 로 꼭지점 색 설정
                 * RadarChartDrawer.OffsetPoint 의 x 를 이용해서 중심 x좌표 이동, y를 이용해서 중심 y좌표 이동
                 */
                RadarChartDrawer.drawRadarChart(canvas, scores, new RadarChartDrawer.RadarAttribute(232.0f, 0f, "#5E72E9"),
                        new RadarChartDrawer.OffsetPoint(6.5f, 9.5f));

                iv_radar_chart_big.setImageBitmap(bitmap);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 생성 2초 후 타이핑 효과 시작
        typingDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTyping();
            }
        }, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTyping();
    }

    private void startTyping() {
        typingDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (totalPos < totalText.length()) {
                    totalDesc.append(String.valueOf(totalText.charAt(totalPos)));
                    totalPos++;
                    //스크롤뷰 위치 업데이트
                    totalDescScroll.post(new Runnable() {
                        @Override
                        public void run() {
                            totalDescScroll.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                    typingDelayHandler.postDelayed(this, 50);
                }
            }
        }, 50);
    }

    private void stopTyping() {
        typingDelayHandler.removeCallbacksAndMessages(null);
    }
}