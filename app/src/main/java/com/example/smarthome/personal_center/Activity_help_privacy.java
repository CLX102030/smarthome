package com.example.smarthome.personal_center;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthome.R;

public class Activity_help_privacy extends AppCompatActivity {
//privacy 隐私政策
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_privacy);

        Activity_Smart_Guardian.addActivity(Activity_help_privacy.this);
    }
}