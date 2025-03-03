package com.planuri.rootonixsmartmirror.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.dialog.RegCheckInfoFragment;
import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.CorrectionDto;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEditFragment extends DialogFragment {

    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface
    private static final String TAG = CareModeFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserEditFragment newInstance(String param1, String param2) {
        UserEditFragment fragment = new UserEditFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_edit, container, false);

        ImageView user_edit_check_btn = view.findViewById(R.id.user_edit_check_btn);

        user_edit_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ((MainActivity)getActivity()).fragmentView(3);
            }
        });
        return view;
    }
}