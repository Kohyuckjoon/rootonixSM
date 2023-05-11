package com.planuri.rootonixsmartmirror.dialog;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.fragments.CareModeFragment;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogwithdrawalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogwithdrawalFragment extends DialogFragment {
    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface
    private static final String TAG = CareModeFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DialogwithdrawalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogwithdrawalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogwithdrawalFragment newInstance(String param1, String param2) {
        DialogwithdrawalFragment fragment = new DialogwithdrawalFragment();
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
        View view = inflater.inflate(R.layout.fragment_dialogwithdrawal, container, false);

        ImageView secession_btn = view.findViewById(R.id.secession_btn);
        ImageView withdrawal_cancel_btn = view.findViewById(R.id.withdrawal_cancel_btn);

        long temp = LoginKioskUser.getInstance().getKioskUser().kuserIdx;

        /** 탈퇴 버튼 클릭시*/
        secession_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CallApi.getWithdrawal(temp).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        CommonResponse commonResponse = response.body();
                        if (response.isSuccessful() && response.body() != null){
                            System.out.println("성공여부 success : >>> " + commonResponse.success);
                            System.out.println("성공여부 msg : >>> " + commonResponse.msg);
                            System.out.println("성공여부 code : >>> " + commonResponse.code);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });

                /** 다이얼로그 띄워야함
                 * 회원 탈퇴가 완료되었습니다!
                 * */
                DialogwithdrawalCheckFragment dialogwithdrawalCheckFragment = new DialogwithdrawalCheckFragment();
                dialogwithdrawalCheckFragment.show(getActivity().getSupportFragmentManager(), "dialogwithdrawalCheckFragment");
                dialogwithdrawalCheckFragment.setCancelable(true);
                dismiss();
            }

        });

        /**
         * 취소버튼 눌렀을때
         */
        withdrawal_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return view;
    }
}