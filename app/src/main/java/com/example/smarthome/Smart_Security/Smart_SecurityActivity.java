package com.example.smarthome.Smart_Security;

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

import com.example.smarthome.Fragment.living_roomFragment;
import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.MainActivity;
import com.example.smarthome.R;
import com.example.smarthome.personal_center.Activity_Smart_Guardian;
import com.example.smarthome.personal_center.personal_Activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;
import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;


public class Smart_SecurityActivity extends AppCompatActivity implements View.OnLongClickListener,View.OnClickListener{



    //获取数据
    public NetWorkBusiness netWorkBusiness;
    //            数据令牌
    public String Token;
    //数据库标识适配
    private static SqLite msqLite;
    private static SQLiteDatabase db;

    //            一氧化碳设备地址               数据
    public static String str_NBID_carbon_monoxide,str_carbon_monoxide,
    //              甲烷设备地址           数据
                   str_NBID_Methane,str_methane,
    //中心网关
    str_Central_gateway,
    //      人体       火焰      警示灯
          str_human,str_flame,str_Warning_Light;

    //           人体      火焰     一氧化碳     甲烷
    public static String human="false",flame="0.0",carbon_monoxide="00.00",methane="00.00";
    public static String human_data="false",flame_data="0.0",carbon_monoxide_data="00.00",methane_data="00.00";

    //弹窗标识
    private int Toast_human=0,Toast_flame=0,Toast_carbon_monoxide=0,Toast_methane=0,Toast_ip=0;
    //TextView显示      人体           火焰              一氧化碳               甲烷
    private TextView textView_human,textView_flame,textView_carbon_monoxide,textView_methane;
    //界面跳转button按钮控件
    private Button but_Wisdom_life2,but_Smart_Security2,but_personal_center2;
    //警示灯 摄像头控制按键                  上         下          左        右  打开
    private TextView but_Warning_Light,but_sup,but_below,but_Left,but_right,but_open;
    //监控IP
    private static String ip="";
    private static  CameraManager cameraManager;

    //警示灯单击双控方法标识
    private int ag0=0;


    //handler方法
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    textView_Data();
                    setData();
                    getListData();
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
        setContentView(R.layout.activity_smart_security);

        Activity_Smart_Guardian.addActivity(Smart_SecurityActivity.this);

        //数据库
        msqLite=new SqLite(this,"smart_home",null,1);
        msqLite.getWritableDatabase();
        db = msqLite.getWritableDatabase();
        //Token令牌
        Int_Token();
        Log.d("token----->", "onCreate: 智慧安防"+Token);
        Int_data();//数据库拿参
       //单击事件初始化
        But_intent();
        //获取数据
        getData();
        //监控

    }
    public void But_intent(){
        //底部
        but_Wisdom_life2=findViewById(R.id.but_Wisdom_life2);
        but_Smart_Security2=findViewById(R.id.but_Smart_Security2);
        but_personal_center2=findViewById(R.id.but_personal_center2);
        but_Wisdom_life2.setOnClickListener(this);
        but_Smart_Security2.setOnClickListener(this);
        but_personal_center2.setOnClickListener(this);

        //警示灯 上下左右
        but_Warning_Light=findViewById(R.id.but_Warning_Light);
        but_sup=findViewById(R.id.but_sup);
        but_below=findViewById(R.id.but_below);
        but_Left=findViewById(R.id.but_Left);
        but_right=findViewById(R.id.but_right);
        but_Warning_Light.setOnClickListener(this);
        but_sup.setOnLongClickListener(this);
        but_below.setOnLongClickListener(this);
        but_Left.setOnLongClickListener(this);
        but_right.setOnLongClickListener(this);

        //打开
        but_open=findViewById(R.id.but_open);
        but_open.setOnClickListener(this);


        //显示
        textView_human=findViewById(R.id.textView_human);
        textView_flame=findViewById(R.id.textView_flame);
        textView_carbon_monoxide=findViewById(R.id.textView_carbon_monoxide);
        textView_methane=findViewById(R.id.textView_methane);

    }
    @SuppressLint("Range")
    private void Int_Token(){
        //设备标识符
        Cursor cursor=db.query("Initialize",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String str=cursor.getString(cursor.getColumnIndex("Token"));
                if (str!=null){
                    Token=str;
                }
                if (str==null){
                    Log.d("---Token>", "Int_Token: 数据库故障 "+str);
                }

            }while(cursor.moveToNext());
        }
        cursor.close();//关闭查询

    }
    @SuppressLint("Range")
    private void Int_data(){
        //设备标识符
        Cursor cursor=db.query("DataLogo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
//                              对数据库提出的文本对象进行转换此处已转为String的类型;
                String   home_intent=cursor.getString(cursor.getColumnIndex("home_intent"));//标识符判断
                if (home_intent!=null){
                    int home=Integer.parseInt(home_intent);
                    if (home==1){
                        str_Central_gateway=cursor.getString(cursor.getColumnIndex("str_Central_gateway"));
                        str_NBID_carbon_monoxide=cursor.getString(cursor.getColumnIndex("str_NBID_carbon_monoxide"));
                        Log.d("----->", "data: 1"+str_NBID_carbon_monoxide);
                        str_carbon_monoxide=cursor.getString(cursor.getColumnIndex("str_carbon_monoxide"));

                        str_NBID_Methane=cursor.getString(cursor.getColumnIndex("str_NBID_Methane"));
                        str_methane=cursor.getString(cursor.getColumnIndex("str_Methane"));
                        ip=cursor.getString(cursor.getColumnIndex("str_monitor"));
                        str_human=cursor.getString(cursor.getColumnIndex("str_human"));
                        str_flame=cursor.getString(cursor.getColumnIndex("str_flame"));
                        Log.d("Data00----->", "data: 火焰标识"+str_flame);
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
    public static boolean dataLoop=true;
    //数据获取
    private void getData(){
        if (Token!=null){
            netWorkBusiness=new NetWorkBusiness(Token,"http://www.nlecloud.com");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (dataLoop){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Data();//数据获取
                            Message msg=new Message();
                            msg.what=1;
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();

            if (netWorkBusiness==null){
                Log.d("Token--->", "getData: 网络故障 ");
            }

        }
        if (Token==null){
            Log.d("Token--->", "getData: Token为null");
        }
    }
    private void Data(){

        if (netWorkBusiness!=null){

            netWorkBusiness.getSensor(str_Central_gateway,str_human,new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            human = sensorInfoBaseResponseEntity.getResultObj().getValue();
                            Log.d("Data01----->", "人体: "+human);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_human==0){
                            Toast.makeText(Smart_SecurityActivity.this,"请检查人体标识",Toast.LENGTH_LONG).show();
                            Toast_human++;
                        }
                        Log.d("Data01----->", "人体标识错误");
                    }
                }
            });
        }
        if (netWorkBusiness!=null){
            Log.d("Data00----->", "data------------->: 火焰标识"+str_flame);
            netWorkBusiness.getSensor(str_Central_gateway,str_flame , new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            flame = sensorInfoBaseResponseEntity.getResultObj().getValue();
                            try {
                                flame_data=flame;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            Log.d("Data01----->", "火焰: "+flame);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_flame==0){
                            Toast.makeText(Smart_SecurityActivity.this,"请检查火焰标识",Toast.LENGTH_LONG).show();
                            Toast_flame++;
                        }
                        Log.d("Dat01----->", "火焰标识错误");
                    }
                }
            });
        }
        if (netWorkBusiness!=null){
            netWorkBusiness.getSensor(str_NBID_carbon_monoxide,str_carbon_monoxide,new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    Log.d("Data0000001----->", "一氧化碳："+str_NBID_carbon_monoxide);
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            carbon_monoxide = sensorInfoBaseResponseEntity.getResultObj().getValue();

                            Log.d("Data01----->", "一氧化碳: "+carbon_monoxide);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_carbon_monoxide==0){
                            Toast.makeText(Smart_SecurityActivity.this,"请检查一氧化碳标识",Toast.LENGTH_LONG).show();
                            Toast_carbon_monoxide++;
                        }
                        Log.d("Data01----->", "一氧化碳标识错误");
                    }
                }
            });
        }
        if (netWorkBusiness!=null){
            netWorkBusiness.getSensor(str_NBID_Methane,str_methane, new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                    Log.d("Data0000001----->", "甲烷："+str_NBID_Methane);
                    if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                        if (sensorInfoBaseResponseEntity.getResultObj().getValue()!=null){
                            methane = sensorInfoBaseResponseEntity.getResultObj().getValue();

                            Log.d("Data01----->", "甲烷: "+methane);
                        }
                    }
                    if (sensorInfoBaseResponseEntity.getResultObj()==null){
                        if (Toast_methane==0){
                            Toast.makeText(Smart_SecurityActivity.this,"请检查甲烷标识",Toast.LENGTH_LONG).show();
                            Toast_methane++;
                        }
                        Log.d("Data01----->", "甲烷标识错误");
                    }
                }
            });

        }

    }
    //数据显示方法
    Boolean ag1=Boolean.getBoolean(human);
    private void  textView_Data(){
        Log.d("tag001--->", "textView_Data: "+ag1);
        if (ag1==false){
            textView_human.setText("无");
                //人体
        }
        if (ag1==true){
            textView_human.setText("有");
        }
        if (flame=="0.0"){
            textView_flame.setText("无");
        }
        if (flame=="1.0"){
            textView_flame.setText("有");
        }
        textView_carbon_monoxide.setText(carbon_monoxide+"ppm");
        textView_methane.setText(methane+"ppm");
    }


    private void setData(){
        ContentValues values=new ContentValues();

            Log.d("TAG===>", "setData: 进来了"+"人体："+human+"火焰："+flame+"yiyang:"+carbon_monoxide+"甲烷："+methane);
            if (human.equals("true")){
                values.put("humanBodyData","无");
            }else {
                values.put("humanBodyData","有");
            }
            if (flame.equals("1.0")){
                values.put("flameData","有");
            }else{
                values.put("flameData","无");
            }

            if (carbon_monoxide.equals("00.00")&&methane.equals("00.00")){
                values.put("carbonMonoxideData","0.0");
                values.put("methaneData","0.0");
                db.insert("Historical_data",null,values);//把数据插入表中
                values.clear();//如果要重复插入需要关闭一下
                human_data=human;
                flame_data=flame;
                Log.d("TAG===>", "setData: 进来了1");
                return;
            }
            values.put("carbonMonoxideData",carbon_monoxide);
            values.put("methaneData",methane_data);
            human_data=human;
            flame_data=flame;
            carbon_monoxide_data=carbon_monoxide;
            methane_data=methane;
            db.insert("Historical_data",null,values);//把数据插入表中
            values.clear();//如果要重复插入需要关闭一下
            Log.d("TAG===>", "setData: 进来了2");

    }

      @SuppressLint("Range")

    public static String getListData(){
      //定义一个SQLiteDatabase 的类去承接新建表的对象；
        String data="";

        Cursor cursor=db.query("Historical_data",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
//                              对数据库提出的文本对象进行转换此处已转为String的类型;
                String humanBodyData=cursor.getString(cursor.getColumnIndex("humanBodyData"));
                String flameData=cursor.getString(cursor.getColumnIndex("flameData"));
                String carbonMonoxideData=cursor.getString(cursor.getColumnIndex("carbonMonoxideData"));
                String methaneData=cursor.getString(cursor.getColumnIndex("methaneData"));
                if (humanBodyData!=null&&flameData!=null&&carbonMonoxideData!=null&&methaneData!=null){
                    data="人体:"+humanBodyData+"\r\r火焰:"+flameData+"\r\r一氧化碳:"+carbonMonoxideData+"ppm\r\r甲烷:"+methaneData+"ppm";
                }

            }while(cursor.moveToNext());
        }
        cursor.close();//关闭查询

          Log.d("csgo--->", "getListData: "+data);

          return data;
    }



        public void camera(){
        //摄像头
            String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(ip);
            if (m.matches()) {
                System.out.println("IP格式正确：" + ip);
                try {
                    cameraManager= CameraManager.getInstance();
                    cameraManager.setupInfo(findViewById(R.id.textureView),"admin","admin",ip,"1");
                    cameraManager.openCamera();
                    cameraManager.wait();
                } catch (Exception e) {
                    Log.d("e--------->", "camera: ip错误"+ip);
                    Toast.makeText(Smart_SecurityActivity.this,"ip错误或网络异常",Toast.LENGTH_LONG).show();
                }

            } else {
                System.out.println("IP格式为云平台标识");
                netWorkBusiness.getSensor(str_Central_gateway, ip, new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorInfo> sensorInfoBaseResponseEntity) {
                        if (sensorInfoBaseResponseEntity.getResultObj()!=null){
                            if (sensorInfoBaseResponseEntity.getResultObj().getHttpIp()!=null){
                                ip = sensorInfoBaseResponseEntity.getResultObj().getHttpIp();
                                Log.d("Dataip----->", "ip: "+ip);
                                try {
                                    cameraManager= CameraManager.getInstance();
                                    cameraManager.setupInfo(findViewById(R.id.textureView),"admin","admin",ip,"1");
                                    cameraManager.openCamera();
                                } catch (Exception e) {
                                    Log.d("e--------->", "camera: ip错误"+ip);
                                    Toast.makeText(Smart_SecurityActivity.this,"ip错误或网络异常！",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        if (sensorInfoBaseResponseEntity.getResultObj()==null){
                            if (Toast_ip==0){
                                Toast.makeText(Smart_SecurityActivity.this,"请检查ip标识",Toast.LENGTH_LONG).show();
                                Toast_ip++;
                            }
                        }
                    }
                });

            }



        }


        private boolean loop=false;
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.but_Wisdom_life2:
                Intent intent=new Intent(Smart_SecurityActivity.this, MainActivity.class);
                living_roomFragment.isStop=false;
                Log.d("stop----->", "发送数据");
                startActivity(intent);
                break;
            case R.id.but_personal_center2:
                Intent personal_intent=new Intent(Smart_SecurityActivity.this, personal_Activity.class);
                living_roomFragment.isStop=false;
                Log.d("stop----->", "发送数据");
                startActivity(personal_intent);
                break;
            case R.id.but_Warning_Light:
                set_Data();
                break;
            case R.id.but_open:
                if (loop==false){
                    camera();
                    but_open.setText("关闭");
                    loop=true;
                }else {
                    cameraManager.releaseCamera();
                    but_open.setText("打开");
                    loop=false;
                }
                break;
            default:
                break;
        }

    }
    //控制警报灯
    private void set_Data(){
        if (netWorkBusiness!=null){

            if (ag0==0) {
                netWorkBusiness.control(str_Central_gateway, str_Warning_Light, 1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity baseResponseEntity) {
                    }
                });
                Log.d("T----->", "set_Data: 11111              "+ag0);
                but_Warning_Light.setText("关闭");
                ag0++;
            } else {
                netWorkBusiness.control(str_Central_gateway, str_Warning_Light, 0, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity baseResponseEntity) {
                    }
                });
                Log.d("T----->", "set_Data: 22222              "+ag0);
                but_Warning_Light.setText("打开");
                ag0--;
            }
        }
    }
    @Override
    public boolean onLongClick(View v) {
        PTZ ptz=null;
        switch (v.getId()){
            case R.id.but_sup:
                ptz=PTZ.Up;
                break;
            case R.id.but_below:
                ptz=PTZ.Down;
                break;
            case R.id.but_Left:
                ptz=PTZ.Left;
                break;
            case R.id.but_right:
                ptz=PTZ.Right;
                break;
            default:
                break;
        }
        if (ptz!=null){
            cameraManager.controlDir(ptz);
        }
        return false;
    }

}