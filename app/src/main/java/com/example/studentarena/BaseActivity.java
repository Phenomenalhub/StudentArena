package com.example.studentarena;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "Base";

    @Override
    protected void onResume() {
        Log.i(TAG,"BaseActivity");
        super.onResume();
    }
}
