package com.planuri.rootonixsmartmirror.dialog;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.ScalpAnalysisData;
import com.planuri.rootonixsmartmirror.util.ImageDownloadByUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogFullScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogFullScreenFragment extends DialogFragment {
    List<ImageView> imageViews = null;

    private ScalpAnalysisData mAnalysisData = LoginKioskUser.getInstance().getAnalysisData();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DialogFullScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogFullScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogFullScreenFragment newInstance(String param1, String param2) {
        DialogFullScreenFragment fragment = new DialogFullScreenFragment();
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
        View view = inflater.inflate(R.layout.fragment_dialog_full_screen, container, false);
        ImageView imageView = view.findViewById(R.id.dialog_image_view); //이미지뷰의 id

        Bundle args = getArguments();
        if(args != null) {
            Bitmap image = args.getParcelable("image");
            imageView.setImageBitmap(image);
        }

        //나가기 버튼
        ImageView imgView = view.findViewById(R.id.cancel_back);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ((MainActivity)getActivity()).fragmentView(5);
            }
        });

        return view;
    }
}