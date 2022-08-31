package com.example.smarthome.personal_center;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthome.R;

public class Activity_help_feedback extends AppCompatActivity {
//数据反馈
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feedback);

        Activity_Smart_Guardian.addActivity(Activity_help_feedback.this);

    }
}