package com.planuri.rootonixsmartmirror.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.ScalpAnalysisData;

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

        TextView totalDesc = view.findViewById(R.id.total_desc);
        totalDesc.setText(mAnalysisData.total.title + "\n\n" + mAnalysisData.total.short_explain + "\n\n" + mAnalysisData.total.total_explain);

        TextView expertSuggest = view.findViewById(R.id.expert_suggest);
//        expertSuggest.setText(mAnalysisData.total.expert_suggest + "\n\n전문가 제안1: " + mAnalysisData.total.expert_list.get(0) + "\n\n" +
//                "전문가 제안2: " + mAnalysisData.total.expert_list.get(1));
        expertSuggest.setText(mAnalysisData.total.expert_suggest);

        TextView expert_suggest1 = view.findViewById(R.id.expert_suggest1);
        expert_suggest1.setText(mAnalysisData.total.expert_list.get(0) + "\n");
        TextView expert_suggest2 = view.findViewById(R.id.expert_suggest2);
        expert_suggest2.setText(mAnalysisData.total.expert_list.get(1) + "\n");
        TextView troubleScalpFont = view.findViewById(R.id.trouble_scalp_font);
        troubleScalpFont.setText("" + mAnalysisData.total.title);


        TextView totalScore = view.findViewById(R.id.score);
        totalScore.setText(String.valueOf(LoginKioskUser.getInstance().getAnalysisTotal()));

        ImageView pore_total = view.findViewById(R.id.pore_total);
        int analysisTotal = LoginKioskUser.getInstance().getAnalysisTotal(); // 5개의 값을 더한 결과 값



        /**
         * analysisTotal >>> 5개의 검사 결과 값을 더한 값
         * analysisTotal 수치에 따라 보여지는 이미지 결과가 다름.
         * */
        if (analysisTotal <= 10){
            pore_total.setImageResource(R.drawable.pore_10);
        } else if (analysisTotal <= 20 && analysisTotal >= 10){
            pore_total.setImageResource(R.drawable.pore_20);
        } else if (analysisTotal <= 30 && analysisTotal >= 20){
            pore_total.setImageResource(R.drawable.pore_30);
        } else if (analysisTotal <= 40 && analysisTotal >= 30){
            pore_total.setImageResource(R.drawable.pore_40);
        } else if (analysisTotal <= 50 && analysisTotal >= 40){
            pore_total.setImageResource(R.drawable.pore_50);
        } else if (analysisTotal <= 60 && analysisTotal >= 50){
            pore_total.setImageResource(R.drawable.pore_60);
        } else if (analysisTotal <= 70 && analysisTotal >= 60){
            pore_total.setImageResource(R.drawable.pore_70);
        } else if (analysisTotal <= 80 && analysisTotal >= 70){
            pore_total.setImageResource(R.drawable.pore_80);
        } else if (analysisTotal <= 90 && analysisTotal >= 80){
            pore_total.setImageResource(R.drawable.pore_90);
        } else if (analysisTotal <= 100 && analysisTotal >= 90){
            pore_total.setImageResource(R.drawable.pore_100);
        }

        //평가 항목들 중 가장 안 좋은 순서로 3개
        Map<String, Integer> sortItem = new HashMap<String, Integer>();

        sortItem.put("deadskin", mAnalysisData.total.deadskin);
        sortItem.put("sensitive", mAnalysisData.total.sensitive);
        sortItem.put("thickness", mAnalysisData.total.thickness);
        sortItem.put("sebum", mAnalysisData.total.sebum);
        sortItem.put("num_hairs", mAnalysisData.total.num_hairs);

        List<Map.Entry<String, Integer>> list_entries = new ArrayList<Map.Entry<String, Integer>>(sortItem.entrySet());

        // 비교함수 Comparator를 사용하여 오름차순으로 정렬
        Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
            // compare로 값을 비교
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                // 오름 차순 정렬
                return obj1.getValue().compareTo(obj2.getValue());
            }
        });

        ImageView iconImgView[] = new ImageView[3];
        iconImgView[0] = view.findViewById(R.id.oil_icon);
        iconImgView[1] = view.findViewById(R.id.corneous_icon);
        iconImgView[2] = view.findViewById(R.id.responsiveness_icon);

        ImageView statusImgView[] = new ImageView[3];
        statusImgView[0] = view.findViewById(R.id.first_val);
        statusImgView[1] = view.findViewById(R.id.second_val);
        statusImgView[2] = view.findViewById(R.id.third_val);

        TextView textItem[] = new TextView[3];
        textItem[0] = view.findViewById(R.id.oil);
        textItem[1] = view.findViewById(R.id.corneous);
        textItem[2] = view.findViewById(R.id.responsiveness);


        for(int i = 0; i < 3; i ++) {

            if(list_entries.get(i).getKey().equals("deadskin")) {
                iconImgView[i].setImageResource(R.drawable.corneous_icon);  //각질

                int resourceId = getStatusIconByValue(list_entries.get(i).getValue());
                statusImgView[i].setImageResource(resourceId);
                textItem[i].setText("각질");
            }
            else if(list_entries.get(i).getKey().equals("sensitive")) {
                iconImgView[i].setImageResource(R.drawable.responsiveness_icon);    //민감도

                int resourceId = getStatusIconByValue(list_entries.get(i).getValue());
                statusImgView[i].setImageResource(resourceId);
                textItem[i].setText("민감도");
            }
            else if(list_entries.get(i).getKey().equals("thickness")) {
                iconImgView[i].setImageResource(R.drawable.hair_num);    //모발 두께

                int resourceId = getStatusIconByValue(list_entries.get(i).getValue());
                statusImgView[i].setImageResource(resourceId);
                textItem[i].setText("모발 두께");
            }
            else if(list_entries.get(i).getKey().equals("sebum")) {
                iconImgView[i].setImageResource(R.drawable.oil_icon);    //유분

                int resourceId = getStatusIconByValue(list_entries.get(i).getValue());
                statusImgView[i].setImageResource(resourceId);
                textItem[i].setText("유분");
            }
            else if(list_entries.get(i).getKey().equals("num_hairs")) {
                iconImgView[i].setImageResource(R.drawable.hair_thick);    //모낭당 모발 수

                int resourceId = getStatusIconByValue(list_entries.get(i).getValue());
                statusImgView[i].setImageResource(resourceId);
                textItem[i].setText("모발 수");
            }
        }

        itemEvaluationFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(5);
            }
        });

        return view;
    }

    private int getStatusIconByValue(int value) {
        if(value >= 15 ) { return R.drawable.good; }
        else if(value >= 10 && value < 15) { return R.drawable.usually; }
        else if(value >= 5 && value < 10) { return R.drawable.caution; }
        else return R.drawable.bad;
    }
}