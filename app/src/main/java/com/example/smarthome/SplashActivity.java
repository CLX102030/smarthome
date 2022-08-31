package com.example.smarthome;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.personal_center.Activity_Smart_Guardian;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private int count_int=5;//倒计时
    private boolean skipping=false;//跳过
    Handler handler = new Handler();
    public SqLite sqLite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getSupportActionBar().hide();//隐藏标题栏方法

        Activity_Smart_Guardian.addActivity(SplashActivity.this);

        Sqlite();
        if (savedInstanceState!=null){
            count_int=savedInstanceState.getInt("count_int");
        }
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        }).start();

    }
    private void Sqlite(){
        sqLite=new SqLite(this,"smart_home",null,1);
        sqLite.getWritableDatabase();
        SQLiteDatabase db=sqLite.getWritableDatabase();
        ContentValues Values=new ContentValues();
        int switch_int=0,int_middleware_logoRevise=0;//一个为开关    一个为标识判断跳转
//        Values.put("switch_int", switch_int);
        ContentValues values=new ContentValues();
        Values.put("middleware_logoRevise", int_middleware_logoRevise);
        String home_intent="0";
        String history_login="1";
        Values.put("home_intent",home_intent);
        values.put("history_login",history_login);
        db.insert("DataLogo",null,Values);
        values.clear();
        Log.d("TAG--->", "Sqlite: 第一步"+int_middleware_logoRevise);
    }
    private void goToMain() {
        TextView count_tv=findViewById(R.id.count_tv);//绑定倒计时控件
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count_int==0||skipping==true){
                Intent intent = new Intent(SplashActivity.this, MainActivity_login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                }else {
                    count_int--;
                    handler.postDelayed(this,1000);
                    count_tv.setText(count_int+"s");
                }
            }
        }, 6000);
        
    }
    public void oncLick_jump(View view){
            skipping=true;
    }
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putInt("count_int", count_int);
    }

}