package com.planuri.rootonixsmartmirror.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planuri.rootonixsmartmirror.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegCheckInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegCheckInfoFragment extends DialogFragment {

    public RegCheckInfoFragment() {
    }

    public static RegCheckInfoFragment newInstance(String param1, String param2) {
        RegCheckInfoFragment fragment = new RegCheckInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_check_info, container, false);

        TextView reg_check_info_text = view.findViewById(R.id.reg_check_info_text);
        ImageView info_close = view.findViewById(R.id.info_close_btn);
        ImageView check3 = view.findViewById(R.id.check3_btn);

        reg_check_info_text.setText("해당 이름과 전화번호로 이미 등록된 사용자입니다.");

        info_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}