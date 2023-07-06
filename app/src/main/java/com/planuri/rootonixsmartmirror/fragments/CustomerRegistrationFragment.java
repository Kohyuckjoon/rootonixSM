package com.planuri.rootonixsmartmirror.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.util.file.TextFileLoadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerRegistrationFragment extends BaseFragment {

    private TextFileLoadTask textFileLoadTask;

    public CustomerRegistrationFragment() {}

    public static CustomerRegistrationFragment newInstance(String param1, String param2) {
        CustomerRegistrationFragment fragment = new CustomerRegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_registration, container, false);

        TextView tv_terms_of_use = view.findViewById(R.id.tv_terms_of_use);  //이용약관
        TextView tv_privacy = view.findViewById(R.id.tv_privacy);       //개인정보이용

        // 읽어들인 파일 리소스와 TextView와 짝을 맞춰서 호출
        new TextFileLoadTask(getContext()).execute(
                new Pair<>(tv_terms_of_use, R.raw.termsofuse),
                new Pair<>(tv_privacy, R.raw.privacy)
        );

        CheckBox checkboxCheese1 = (CheckBox) view.findViewById(R.id.terms_checkbox_cheese1);
        CheckBox checkboxCheese2 = (CheckBox) view.findViewById(R.id.terms_checkbox_cheese2);
        ImageView customer_regist_btn = (ImageView) view.findViewById(R.id.customer_regist_btn);
        ImageView customer_cancel_btn = (ImageView) view.findViewById(R.id.cancel);

        checkboxCheese1.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!checkboxCheese1.isChecked()){
                    Toast.makeText(getActivity(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkboxCheese2.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!((CheckBox)view).isChecked()){
                    Toast.makeText(getActivity(), "개인정보취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //취소 버튼 클릭
        customer_cancel_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
            }
        });

        //다음 버튼 클릭
        customer_regist_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!checkboxCheese1.isChecked()){ //체크 안했으면 실행
                    Toast.makeText(getActivity(), "고객이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    checkboxCheese1.requestFocus();
                } else if (!checkboxCheese2.isChecked()){ //체크 안했으면 실행
                    Toast.makeText(getActivity(), "개인정보취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
                } else if (checkboxCheese1.isChecked() && checkboxCheese2.isChecked()){
                    ((MainActivity)getActivity()).fragmentView(9);
                }

//                if(checkbox_cheese1.isChecked() && checkbox_cheese2.isChecked()){
//
//                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_terms_of_use = view.findViewById(R.id.tv_terms_of_use);  //이용약관
        TextView tv_privacy = view.findViewById(R.id.tv_privacy);       //개인정보이용

        // 읽을 파일 리소스 ID와 TextView 매핑
        List<Pair<TextView, Integer>> textViewResourcePairs = new ArrayList<>();
        textViewResourcePairs.add(new Pair<>(tv_terms_of_use, R.raw.termsofuse));
        textViewResourcePairs.add(new Pair<>(tv_privacy, R.raw.privacy));

        // TextFileLoadTask 를 생성하고 실행
        textFileLoadTask = new TextFileLoadTask(requireContext());
        textFileLoadTask.execute(
                new Pair<>(tv_terms_of_use, R.raw.termsofuse),
                new Pair<>(tv_privacy, R.raw.privacy));

        CheckBox checkboxCheese1 = (CheckBox) view.findViewById(R.id.terms_checkbox_cheese1);
        CheckBox checkboxCheese2 = (CheckBox) view.findViewById(R.id.terms_checkbox_cheese2);
        ImageView customer_regist_btn = (ImageView) view.findViewById(R.id.customer_regist_btn);
        ImageView customer_cancel_btn = (ImageView) view.findViewById(R.id.cancel);

        checkboxCheese1.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!checkboxCheese1.isChecked()){
                    Toast.makeText(getActivity(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkboxCheese2.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!((CheckBox)view).isChecked()){
                    Toast.makeText(getActivity(), "개인정보취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //취소 버튼 클릭
        customer_cancel_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
            }
        });

        //다음 버튼 클릭
        customer_regist_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!checkboxCheese1.isChecked()){ //체크 안했으면 실행
                    Toast.makeText(getActivity(), "고객이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    checkboxCheese1.requestFocus();
                } else if (!checkboxCheese2.isChecked()){ //체크 안했으면 실행
                    Toast.makeText(getActivity(), "개인정보취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
                } else if (checkboxCheese1.isChecked() && checkboxCheese2.isChecked()){
                    ((MainActivity)getActivity()).fragmentView(9);
                }

//                if(checkbox_cheese1.isChecked() && checkbox_cheese2.isChecked()){
//
//                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (textFileLoadTask != null) {
            textFileLoadTask.cancel(true);
        }
    }
}