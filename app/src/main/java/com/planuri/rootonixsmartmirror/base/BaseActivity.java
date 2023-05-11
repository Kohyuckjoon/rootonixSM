package com.planuri.rootonixsmartmirror.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.planuri.rootonixsmartmirror.util.Const;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Const.TAG, "onCreate " + this.getClass().getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Const.TAG, "onResume " + this.getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Const.TAG, "onPause " + this.getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Const.TAG, "onDestroy " + this.getClass().getName());
    }
}
