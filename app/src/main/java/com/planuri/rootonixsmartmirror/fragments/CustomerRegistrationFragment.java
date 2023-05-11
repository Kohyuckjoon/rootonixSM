package com.planuri.rootonixsmartmirror.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerRegistrationFragment extends BaseFragment {



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
//        CheckBox checkbox_cheese1 = (CheckBox) view.findViewById(R.id.checkbox_cheese1);
//        CheckBox checkbox_cheese2 = (CheckBox) view.findViewById(R.id.checkbox_cheese2);
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

}