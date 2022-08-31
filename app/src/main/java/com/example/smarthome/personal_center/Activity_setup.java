package com.example.smarthome.personal_center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthome.MainActivity;
import com.example.smarthome.MainActivity_login;
import com.example.smarthome.R;
import com.example.smarthome.Smart_Security.Smart_SecurityActivity;

//设置
public class Activity_setup extends AppCompatActivity implements View.OnClickListener {
    private ImageView but_break_tv2;
    private TextView switch_account,sign_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();//隐藏标题栏方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Activity_Smart_Guardian.addActivity(Activity_setup.this);

        but_break_tv2=findViewById(R.id.but_break_tv2);
        but_break_tv2.setOnClickListener(this);
        switch_account=findViewById(R.id.Switch_account);
        sign_out=findViewById(R.id.sign_out);
        switch_account.setOnClickListener(this);
        sign_out.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_break_tv2:
                Intent intent=new Intent(Activity_setup.this,personal_Activity.class);
                startActivity(intent);
                break;
            case R.id.Switch_account:
                Intent intent1=new Intent(Activity_setup.this, MainActivity_login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                System.gc();
                break;
            case R.id.sign_out:
                try {
                    MainActivity.loop=false;
                    Smart_SecurityActivity.dataLoop=false;
                    Activity_Smart_Guardian.loop=false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (Activity  activity:Activity_Smart_Guardian.activities){
                                activity.finish();

                }
                break;
            default:
                break;
        }
    }
}