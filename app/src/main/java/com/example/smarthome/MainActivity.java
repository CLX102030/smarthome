package com.example.smarthome;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.Fragment.bathroom_Fragment;
import com.example.smarthome.Fragment.bedroom_Fragment;
import com.example.smarthome.Fragment.kitchen_Fragment;
import com.example.smarthome.Fragment.living_roomFragment;
import com.example.smarthome.Fragment.second_bedroomFragment;
import com.example.smarthome.Fragment.study_Fragment;
import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.Smart_Security.Smart_SecurityActivity;
import com.example.smarthome.personal_center.Activity_Smart_Guardian;
import com.example.smarthome.personal_center.personal_Activity;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //界面跳转交互
    public int login=0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public List<Fragment> list;
    public String[] titles3={"客厅","主卧","次卧","书房","厨房","卫生间"};//户型选择方案
    public String[] titles2={"客厅","主卧","次卧","书房","厨房","卫生间"};
    public String[] titles1={"客厅","卧室","厨房","卫生间"};
    public MyAdapter myAdapter;
    public static boolean loop=true;
    public static String str_Central_gateway, str_temp,str_hump,str_noise,str_light,str_co2;
    //            一氧化碳设备地址               数据
    public static String str_NBID_carbon_monoxide,str_carbon_monoxide,
    //              甲烷设备地址           数据
    str_NBID_Methane,str_methane,
    //      人体       火焰      警示灯
    str_human,str_flame,str_Warning_Light;

    //获取数据
    public static NetWorkBusiness netWorkBusiness;
    public String Token;
    //界面显示
    private TextView text_temp,text_hump,text_noise,text_light,text_co2;
    //界面跳转button按钮控件
    private Button but_Wisdom_life,but_Smart_Security,but_personal_center;
    //数据库标识适配
    public  static   SqLite msqLite;
    public static    SQLiteDatabase db;
    //数据
    public static String temp,hump,light,noise,co2;
    //数据Data
    public static double d_temp,d_hump,d_light,d_noise,d_co2;
    //标识错误弹窗
    private int Toast_temp=0,Toast_hump=0,Toast_light=0,Toast_noise=0,Toast_co2=0;
    //Handler方法
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    text_temp.setText(temp+"°C");
                    text_hump.setText(hump+"%RH");
                    text_light.setText(light+"lx");
                    text_noise.setText(noise+"db");
                    text_co2.setText(co2+"ppm");

                    database_storage();
                  //  csgo();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // getSupportActionBar().hide();//隐藏标题栏方法
        super.onCreate(savedInstanceState);
        Activity_Smart_Guardian.addActivity(MainActivity.this);
        setContentView(R.layout.home_activity);
        //数据库
        msqLite=new SqLite(this,"smart_home",null,1);
        db = msqLite.getWritableDatabase();
        Int_data();//数据库拿参
        fragment1();//fragment配置生成
        But_intent();//界面跳转初始化控件
        //Token令牌
        Int_Token();
        if (str_Central_gateway!=null&&str_temp!=null){
            getData();
        }
        netWorkBusiness=new NetWorkBusiness(Token,"http://www.nlecloud.com");
        if (netWorkBusiness!=null){
        Activity_Smart_Guardian.plot(this,netWorkBusiness);}
    }
    /*
    获取数据并实时显示在文本上
     */
    private void getData(){
        Log.d("Data--->", "获取数据方法");
        if (Token!=null) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (loop){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("Data----->", "Thread线程故障");
                        }
                        Data();//获取数据的方法
                        Message msg=new Message();
                        msg.what=1;
                        mHandler.sendMessage(msg);

                    }
                }
            }).start();

        }
        if (Token==null){
            Log.d("token----->", "getData:智慧生活 token==null");
        }

    }
    //定义变量 用与存储比较 为了数据的存储量不是那么大
    Date date=new Date();
    double temp_double;
    double hump_double;
    double light_double;
    double noise_double;
    double co2_double;
    ContentValues values=new ContentValues();
    SimpleDateFormat df=new SimpleDateFormat("y/M/d h:m:s");
    private void database_storage(){
                                           //该处做一个数据校验      优化一下list
        if (d_temp != temp_double||d_hump!=hump_double||d_light!=light_double
                ||d_noise!=noise_double||d_co2!=co2_double){
            Log.d("csgo--->", "改变===================");
            //定义一个并实例化contentValues的类；
            values.put("data",df.format(date));
            values.put("tempData",d_temp);
            values.put("humpData",d_hump);
            values.put("lightData",d_light);
            values.put("co2Data",d_co2);
            values.put("noiseData",d_co2);
            db.insert("Historical_data",null,values);//把数据插入表中
            values.clear();//如果要重复插入需要关闭一下
            temp_double=d_temp;
            hump_double=d_hump;
            light_double=d_light;
            noise_double=d_noise;
            co2_double  =d_co2;
        }else {
            Log.d("csgo--->", "未改变");
        }
    }

    private void Data(){

        if (netWorkBusiness!=null){

            netWorkBusiness.getSensor(str_Central_gateway,str_temp , new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            temp = sensorInfoBaseResponseEntity.getResultObj().getValue();
                            d_temp=Double.parseDouble(temp);
                            Log.d("Data----->", "温度: "+temp);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_temp==0){
                            Toast.makeText(MainActivity.this,"请检查温度标识",Toast.LENGTH_LONG).show();
                            Toast_temp++;
                        }
                        Log.d("Data----->", "温度标识错误");
                    }
                }
            });
        }
        if (netWorkBusiness!=null){
            netWorkBusiness.getSensor(str_Central_gateway,str_hump , new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            hump = sensorInfoBaseResponseEntity.getResultObj().getValue().trim();
                            d_hump=Double.parseDouble(hump);
                            Log.d("Data----->", "湿度: "+hump);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_hump==0){
                            Toast.makeText(MainActivity.this,"请检查湿度标识",Toast.LENGTH_LONG).show();
                            Toast_hump++;
                        }
                        Log.d("Data----->", "湿度标识错误");
                    }
                }
            });
        }
        if (netWorkBusiness!=null){
            netWorkBusiness.getSensor(str_Central_gateway,str_light , new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            light = sensorInfoBaseResponseEntity.getResultObj().getValue();
                            d_light=Double.parseDouble(light);
                            Log.d("Data----->", "光照: "+light);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_light==0){
                            Toast.makeText(MainActivity.this,"请检查光照标识",Toast.LENGTH_LONG).show();
                            Toast_light++;
                        }
                        Log.d("Data----->", "光照标识错误");
                    }
                }
            });
        }
        if (netWorkBusiness!=null){
            netWorkBusiness.getSensor(str_Central_gateway,str_noise, new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {

                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                           if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                                noise = sensorInfoBaseResponseEntity.getResultObj().getValue();
                                d_noise=Double.parseDouble(noise);
                                Log.d("Data----->", "噪音: "+noise);
                           }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_noise==0){
                        Toast.makeText(MainActivity.this,"请检查噪音标识",Toast.LENGTH_LONG).show();
                            Toast_noise++;
                        }
                        Log.d("Data----->", "噪音标识错误");
                    }
                }
            });

        }
        if (netWorkBusiness!=null){
            netWorkBusiness.getSensor(str_Central_gateway,str_co2, new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            co2 = sensorInfoBaseResponseEntity.getResultObj().getValue();
                            d_co2=Double.parseDouble(co2);
                            Log.d("Data----->", "二氧化碳: "+co2);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_co2==0){
                            Toast.makeText(MainActivity.this,"请检查二氧化碳标识",Toast.LENGTH_LONG).show();
                            Toast_co2++;
                        }
                        Log.d("Data----->", "噪音标识错误");
                    }
                }
            });
        }
    }
    @SuppressLint("Range")
    private void Int_Token(){
        //设备标识符
        SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLiteDatabase 的类去承接新建表的对象；
        Cursor cursor=db.query("Initialize",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
             String str=cursor.getString(cursor.getColumnIndex("Token"));
                if (str!=null){
                    Token=str;
                }
            }while(cursor.moveToNext());
            if (Token!=null){
                Log.d("token----->", "token正常智慧生活");
            }
            if(Token==null){
                Log.d("token----->", "Int_Token: 数据库故障 ");
            }
        }
        cursor.close();//关闭查询

    }
    //fragment控件的使用
    public void fragment1(){
        viewPager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tab_layout);
        list =new ArrayList<>();
        list.add(new living_roomFragment());
        list.add(new bedroom_Fragment());//
        list.add(new second_bedroomFragment());
        list.add(new study_Fragment());
        list.add(new kitchen_Fragment());
        list.add(new bathroom_Fragment());
        myAdapter=new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @SuppressLint("Range")
    private void Int_data(){
        //设备标识符
        SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLiteDatabase 的类去承接新建表的对象；
        Cursor cursor=db.query("DataLogo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
//                              对数据库提出的文本对象进行转换此处已转为String的类型;
          String   home_intent=cursor.getString(cursor.getColumnIndex("home_intent"));//标识符判断
          if (home_intent!=null){
              int home=Integer.parseInt(home_intent);
              if (home==1){
                  str_Central_gateway=cursor.getString(cursor.getColumnIndex("str_Central_gateway"));
                  Log.d("TAG--->", "网关id"+str_Central_gateway);
                  Log.d("----->", "data: "+str_Central_gateway);
                  str_temp=cursor.getString(cursor.getColumnIndex("str_temp"));
                  str_hump=cursor.getString(cursor.getColumnIndex("str_hump"));
                  str_light=cursor.getString(cursor.getColumnIndex("str_light"));
                  str_co2=cursor.getString(cursor.getColumnIndex("str_co2"));
                  str_noise=cursor.getString(cursor.getColumnIndex("str_noise"));

                  str_Central_gateway=cursor.getString(cursor.getColumnIndex("str_Central_gateway"));
                  str_NBID_carbon_monoxide=cursor.getString(cursor.getColumnIndex("str_NBID_carbon_monoxide"));
                  str_carbon_monoxide=cursor.getString(cursor.getColumnIndex("str_carbon_monoxide"));

                  str_NBID_Methane=cursor.getString(cursor.getColumnIndex("str_NBID_Methane"));
                  str_methane=cursor.getString(cursor.getColumnIndex("str_Methane"));
                  str_human=cursor.getString(cursor.getColumnIndex("str_human"));
                  str_flame=cursor.getString(cursor.getColumnIndex("str_flame"));
                  str_Warning_Light=cursor.getString(cursor.getColumnIndex("str_Warning_Light"));

              }
          }
          if (home_intent==null){
              Log.d("----->", "Int_data: 数据库异常");
          }

            }while(cursor.moveToNext());
        }
        cursor.close();//关闭查询

    }

    public void But_intent(){
        but_Wisdom_life=findViewById(R.id.but_Wisdom_life1);
        but_Smart_Security=findViewById(R.id.but_Smart_Security1);
        but_personal_center=findViewById(R.id.but_personal_center1);
        but_Wisdom_life.setOnClickListener(this);
        but_Smart_Security.setOnClickListener(this);
        but_personal_center.setOnClickListener(this);

       text_temp= findViewById(R.id.text_temp_tv);
       text_hump= findViewById(R.id.text_hump_tv);
       text_light=(TextView) findViewById(R.id.text_light_tv);
       text_noise=(TextView) findViewById(R.id.text_noise_tv);
       text_co2=(TextView) findViewById(R.id.text_co2_tv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_Wisdom_life1:
                break;
            case R.id.but_Smart_Security1:
                Intent intent=new Intent(MainActivity.this, Smart_SecurityActivity.class);
                living_roomFragment.isStop=true;
                Log.d("stop----->", "发送数据");
                startActivity(intent);
                break;
            case R.id.but_personal_center1:
                Intent personal_intent=new Intent(MainActivity.this, personal_Activity.class);
                living_roomFragment.isStop=true;
                Log.d("stop----->", "发送数据");
                startActivity(personal_intent);
                break;
        }
    }




class MyAdapter extends FragmentPagerAdapter{

    public MyAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public CharSequence getPageTitle(int position){
        return titles3[position];
    }
}
}