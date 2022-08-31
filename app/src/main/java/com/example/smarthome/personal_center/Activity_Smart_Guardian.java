package com.example.smarthome.personal_center;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.MainActivity;
import com.example.smarthome.MainActivity_data_processing;
import com.example.smarthome.R;
import com.example.smarthome.Smart_Security.Smart_SecurityActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class Activity_Smart_Guardian extends AppCompatActivity implements View.OnClickListener {
    private ImageView but_break_tv3;
    private TextView mSwitch;
    private  SqLite msqLite;
    private SQLiteDatabase db;
    private  int switch_int=-1;
    private TextView middleware_logoRevise,history_record_tv;
    public String str_middleware_logoRevise="1";
    private String Token;
    private static NetWorkBusiness netWorkBusiness= MainActivity.netWorkBusiness;
    public static boolean loop=true;
    public static ArrayList<Activity> activities=new ArrayList<>();
    public static void addActivity(Activity activity){
        if (activity==null){
            return;
        }
        for (Activity activity1:activities){  //做一个重复判断
            if (activity1==activity){
                return;
            }
        }
        activities.add(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();//隐藏标题栏方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_guardian);

        Activity_Smart_Guardian.addActivity(Activity_Smart_Guardian.this);


        but_break_tv3=findViewById(R.id.but_break_tv3);
        but_break_tv3.setOnClickListener(this);
        middleware_logoRevise=findViewById(R.id.middleware_logoRevise);
        middleware_logoRevise.setOnClickListener(this);

        history_record_tv=findViewById(R.id.history_record_tv);
        history_record_tv.setOnClickListener(this);
if (netWorkBusiness==null){
    netWorkBusiness=new NetWorkBusiness(Token,"http://www.nlecloud.com");
}
        mSwitch=findViewById(R.id.mSwitch);
        mSwitch.setOnClickListener(this);
        msqLite= new SqLite(this, "smart_home", null, 1);
        msqLite.getWritableDatabase();
        db= msqLite.getWritableDatabase();//定义一个SQLiteDatabase 的类去承接新建表的对象；
        Switch_getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_break_tv3:
                Intent intent=new Intent(Activity_Smart_Guardian.this,personal_Activity.class);
                startActivity(intent);
                break;
            case R.id.middleware_logoRevise:
                set_middleware_logoRevise();
                Intent intent_middleware_logoRevise=new Intent(Activity_Smart_Guardian.this, MainActivity_data_processing.class);
                startActivity(intent_middleware_logoRevise);
                break;
            case R.id.history_record_tv:
                Intent intent_data=new Intent(Activity_Smart_Guardian.this, Activity_HistoricalData.class);
                startActivity(intent_data);
                break;
            case R.id.mSwitch:

                int logo=-1;
                if (switch_int==0){
                    mSwitch.setText("自动控制                                 打开");

                    Log.d("switch--------->1", "onClick: 关闭");
                    loop=false;
                    //关闭线程
                    ContentValues values=new ContentValues();//定义一个并实例化contentValus 的类；
                    logo=0;
                    values.put("switch_logo",logo);
                    db.insert("Student",null,values);//把数据插入表中
                    values.clear();//如果要重复插入需要关闭一下
                    switch_int=1;

                }else if (switch_int==1){
                    mSwitch.setText("自动控制                                 关闭");
                    if (Token!=null){
                        loop=true;
                        plot(this,netWorkBusiness);
                    }
                    logo=0;
                    ContentValues values=new ContentValues();//定义一个并实例化contentValus 的类；
                    values.put("switch_logo",logo);
                    db.insert("Student",null,values);//把数据插入表中
                    values.clear();
                    switch_int=0;
                    Log.d("switch--------->1", "onClick: 打开");
                }
                break;
        }
    }

    private void set_middleware_logoRevise(){
        ContentValues values=new ContentValues();
        Log.d("TAG--->", "set_middleware_logoRevise: 第三步"+str_middleware_logoRevise);
        values.put("middleware_logoRevise",str_middleware_logoRevise);
        db.insert("DataLogo",null,values);
        values.clear();
    }

int logo=-1;
    @SuppressLint("Range")
    public void Switch_getData(){
       Cursor cursor=db.query("Initialize",null,null,null,null,null,null);
       if(cursor.moveToFirst()){
           do{
               try {
                   logo = cursor.getInt(cursor.getColumnIndex("switch_logo"));//标识符判断
                   Log.d("----logo", "Switch_getData: "+switch_int);
                   String str=cursor.getString(cursor.getColumnIndex("Token"));
                   if (str!=null){
                       Token=str;
                   }
                   if (str==null){
                       Log.d("---Token>", "Int_Token: 数据库故障 "+str);
                   }
               } catch (Exception e) {
                   logo=0;
               }

           }while(cursor.moveToNext());

                       if (logo==0){
                            mSwitch.setText("自动控制                                 打开");
                            switch_int=1;
                            loop=false;
                        }else if (logo==1){
                            mSwitch.setText("自动控制                                 关闭");
                            switch_int=0;
                        }

       }
       cursor.close();//关闭查询

        //自动控制按钮
        Log.d("Switch_getData", "onCheckedChanged: "+switch_int);


   }
   public static void plot(Context context,NetWorkBusiness netWorkBusiness){


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (loop){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //警示灯检测 烟雾自动检测

                    try {
                        if (Smart_SecurityActivity.human.equals("true")||Smart_SecurityActivity.flame.equals("1.0")){
                            MainActivity.netWorkBusiness.control(MainActivity.str_Central_gateway,MainActivity.str_Warning_Light, 1, new NCallBack<BaseResponseEntity>(context.getApplicationContext()) {
                                @Override
                                protected void onResponse(BaseResponseEntity baseResponseEntity) {

                                }
                            });
                     }else if (Smart_SecurityActivity.human.equals("false")||Smart_SecurityActivity.flame.equals("0.0")){
                            MainActivity.netWorkBusiness.control(MainActivity.str_Central_gateway,MainActivity.str_Warning_Light, 0, new NCallBack<BaseResponseEntity>(context.getApplicationContext()) {
                                @Override
                                protected void onResponse(BaseResponseEntity baseResponseEntity) {
                                }
                            });
                        }
                        Log.d("switch------>", "走进来了线程打开" +Smart_SecurityActivity.human+Smart_SecurityActivity.flame);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }
        }).start();
   }

}