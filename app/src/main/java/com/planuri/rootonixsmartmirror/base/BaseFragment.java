package com.planuri.rootonixsmartmirror.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.planuri.rootonixsmartmirror.util.Const;

public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Const.TAG, "onCreate " + this.getClass().getName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Const.TAG, "onViewCreated " + this.getClass().getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Const.TAG, "onResume " + this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Const.TAG, "onPause " + this.getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Const.TAG, "onDestroy " + this.getClass().getName());
    }
}
