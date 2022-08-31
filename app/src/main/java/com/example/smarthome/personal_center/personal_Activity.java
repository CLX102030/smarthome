package com.example.smarthome.personal_center;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smarthome.Fragment.living_roomFragment;
import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.MainActivity;
import com.example.smarthome.R;
import com.example.smarthome.Smart_Security.Smart_SecurityActivity;

import androidx.appcompat.app.AppCompatActivity;

public class personal_Activity extends AppCompatActivity implements View.OnClickListener {
    //界面跳转button按钮控件
    private Button but_Wisdom_life3,but_Smart_Security3,but_personal_center3;
    //界面内交互button
    private TextView but_Smart_Guardian,but_setup,but_personal;
    //界面显示账号
    private TextView account_tv;
    //
    private SqLite msqLite;
    //账号
    private String   account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getSupportActionBar().hide();//隐藏标题栏方法
        super.onCreate(savedInstanceState);

        Activity_Smart_Guardian.addActivity(personal_Activity.this);

        setContentView(R.layout.activity_personal_center);
        But_intent();
        //数据库
        msqLite=new SqLite(this,"smart_home",null,1);
        msqLite.getWritableDatabase();
        account_sql();//更新账号显示位置
    }
    public void But_intent(){
        but_Wisdom_life3=findViewById(R.id.but_Wisdom_life3);
        but_Smart_Security3=findViewById(R.id.but_Smart_Security3);
        but_personal_center3=findViewById(R.id.but_personal_center3);
        but_Wisdom_life3.setOnClickListener(this);
        but_Smart_Security3.setOnClickListener(this);
        but_personal_center3.setOnClickListener(this);
        but_Smart_Guardian=findViewById(R.id.but_Smart_Guardian);
        but_setup=findViewById(R.id.but_setup);
        but_personal=findViewById(R.id.but_personal);
        but_Smart_Guardian.setOnClickListener(this);
        but_setup.setOnClickListener(this);
        but_personal.setOnClickListener(this);
    }
    @SuppressLint("Range")
    public void account_sql(){
    account_tv=findViewById(R.id.account_tv);
    //设备标识符
    SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLiteDatabase 的类去承接新建表的对象；
    Cursor cursor=db.query("Initialize",null,null,null,null,null,null);
    if(cursor.moveToFirst()){
        do{
//                              对数据库提出的文本对象进行转换此处已转为String的类型;
               account=cursor.getString(cursor.getColumnIndex("account"));//标识符判断

        }while(cursor.moveToNext());
    }
    cursor.close();//关闭查询
        if (account!=null){
            account_tv.setText(account);
        }
        if (account==null){
            Log.d("----->", "account_sql: 数据错误00");
        }
}
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_Wisdom_life3:
                Intent intent=new Intent(personal_Activity.this, MainActivity.class);
                living_roomFragment.isStop=false;
                Log.d("stop----->", "发送数据");
                startActivity(intent);
                break;
            case R.id.but_Smart_Security3:
                Intent intent_smart=new Intent(personal_Activity.this, Smart_SecurityActivity.class);
                living_roomFragment.isStop=false;
                Log.d("stop----->", "发送数据");
                startActivity(intent_smart);
                break;
//            case R.id.but_personal_center3:
//                Intent personal_intent=new Intent(personal_Activity.this, personal_Activity.class);
//                startActivity(personal_intent);
//                break;
            case R.id.but_Smart_Guardian:
                Intent Smart_Guardian=new Intent(personal_Activity.this, Activity_Smart_Guardian.class);
                startActivity(Smart_Guardian);
                break;
            case R.id.but_setup:
                Intent setup=new Intent(personal_Activity.this, Activity_setup.class);
                startActivity(setup);
                break;
            case R.id.but_personal:
                Intent help=new Intent(personal_Activity.this, Activity_help.class);
                startActivity(help);
                break;
        }
    }
}