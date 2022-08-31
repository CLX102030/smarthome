package com.example.smarthome;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.personal_center.Activity_Smart_Guardian;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_data_processing extends AppCompatActivity implements View.OnClickListener {
    private TextView but_return,but_confirm;
    private SqLite msqLite;
    public String home_intent ="0";
    public int int_middleware_logoRevise=-1;//判断跳转界面
    //以下为获取设备标识注释
    //                物联网中心网关设备ID
    //                温度           湿度          噪音            光照
    //                Co2           风速          人体            火焰
    //                照明灯          风扇        烟雾           警示灯
    //                窗帘          rgb         监控
    //           NBIOT设备ID    甲烷
    //           NBIOT设备Id    一氧化碳
    private EditText editText_Central_gateway,
                     editText_temp,editText_hump,editText_noise,editText_light,
                     editText_CO2,editText_wind,editText_human,editText_flame,
                     editText_flashlight,editText_fan,editText_smoke,editText_Warning_Light,
                     editText_curtains_open,editText_rgb,editText_monitor,
                     editText_NBID_Methane,editText_Methane,
                     editText_NBID_carbon_monoxide,editText_carbon_monoxide;
    private String   str_Central_gateway,
                     str_temp,str_hump,str_noise,str_light,
                     str_co2,str_wind,str_human,str_flame,
                     str_flashlight,str_fan,str_smoke,str_Warning_Light,
                     str_curtains_open,str_rgb,str_monitor,
                     str_NBID_Methane,str_Methane,
                     str_NBID_carbon_monoxide,str_carbon_monoxide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity_Smart_Guardian.addActivity(MainActivity_data_processing.this);

        setContentView(R.layout.activity_main_data_processing);
        Int_View();
        msqLite=new SqLite(this,"smart_home",null,1);
        msqLite.getWritableDatabase();
        read_Sqlite();//先查询数据库是否有数据
    }
    private void Int_View(){
        but_return=findViewById(R.id.butt_return);
        but_confirm=findViewById(R.id.butt_confirm);
        but_return.setOnClickListener(this);
        but_confirm.setOnClickListener(this);
//绑定
        editText_Central_gateway=findViewById(R.id.editText_Central_gateway);
        editText_temp=findViewById(R.id.editText_temp);
        editText_hump=findViewById(R.id.editText_hump);
        editText_noise=findViewById(R.id.editText_noise);
        editText_light=findViewById(R.id.editText_light);
        editText_CO2=findViewById(R.id.editText_CO2);
        editText_wind=findViewById(R.id.editText_wind);
        editText_human=findViewById(R.id.editText_human);
        editText_flame=findViewById(R.id.editText_flame);
        editText_flashlight=findViewById(R.id.editText_flashlight);
        editText_fan=findViewById(R.id.editText_fan);
        editText_smoke=findViewById(R.id.editText_smoke);//烟雾
        editText_Warning_Light=findViewById(R.id.editText_Warning_Light);
        editText_curtains_open=findViewById(R.id.editText_curtains_open);
        editText_rgb=findViewById(R.id.editText_Rgb);
        editText_monitor=findViewById(R.id.editText_monitor);//监控

        editText_NBID_Methane=findViewById(R.id.editText_NBID_Methane);

        editText_Methane=findViewById(R.id.editText_Methane);

        editText_NBID_carbon_monoxide=findViewById(R.id.editText_NBID_carbon_monoxide);

        editText_carbon_monoxide=findViewById(R.id.editText_carbon_monoxide);
    }
    private void Intent_view(){
        Intent intent=new Intent(this,MainActivity_login.class);
        startActivity(intent);
    }
    private void but_Bundle(){
        str_Central_gateway=editText_Central_gateway.getText().toString().trim();


        str_temp=editText_temp.getText().toString();
        str_hump=editText_hump.getText().toString();
        str_noise=editText_noise.getText().toString();
        str_light=editText_light.getText().toString();
        str_co2=editText_CO2.getText().toString();
        str_wind=editText_wind.getText().toString();
        str_human=editText_human.getText().toString();
        str_flame=editText_flame.getText().toString();
        str_flashlight=editText_flashlight.getText().toString();
        str_fan=editText_fan.getText().toString();
               str_smoke=editText_smoke.getText().toString();
        str_Warning_Light=editText_Warning_Light.getText().toString();
        str_curtains_open=editText_curtains_open.getText().toString();
        str_rgb=editText_rgb.getText().toString();
               str_monitor=editText_monitor.getText().toString();
        str_NBID_Methane=editText_NBID_Methane.getText().toString();
        str_Methane=editText_Methane.getText().toString();
        str_NBID_carbon_monoxide=editText_NBID_carbon_monoxide.getText().toString();
        str_carbon_monoxide=editText_carbon_monoxide.getText().toString();



        if(!str_Central_gateway.equals("")
          && !str_temp.equals("") && !str_hump.equals("") && !str_noise.equals("") && !str_light.equals("")
          && !str_co2.equals("") && !str_wind.equals("") && !str_human.equals("") && !str_flame.equals("")
          && !str_flashlight.equals("") && !str_fan.equals("") && !str_smoke.equals("") && !str_Warning_Light.equals("")
                && !str_curtains_open.equals("") && !str_rgb.equals("") && !str_monitor.equals("")
          && !str_NBID_Methane.equals("") && !str_Methane.equals("")
          && !str_NBID_carbon_monoxide.equals("") && !str_carbon_monoxide.equals(""))
        {

            SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLliteDatabase 的类去承接新建表的对象；
            ContentValues values=new ContentValues();//定义一个并实例化contentValus 的类；

            String implement="1";
            String history_login="2";
            values.put("home_intent", implement);
            values.put("history_login",history_login);

            values.put("str_Central_gateway",str_Central_gateway);

            values.put("str_temp",str_temp);
            values.put("str_hump",str_hump);
            values.put("str_noise",str_noise);
            values.put("str_light",str_light);

            values.put("str_co2",str_co2);
            values.put("str_wind",str_wind);
            values.put("str_human",str_human);
            values.put("str_flame",str_flame);

            values.put("str_flashlight",str_flashlight);
            values.put("str_fan",str_fan);
            values.put("str_smoke",str_smoke);
            values.put("str_Warning_Light",str_Warning_Light);

            values.put("str_curtains_open",str_curtains_open);
            values.put("str_rgb",str_rgb);
            values.put("str_monitor",str_monitor);

            values.put("str_NBID_Methane",str_NBID_Methane);
            values.put("str_Methane",str_Methane);


            values.put("str_NBID_carbon_monoxide",str_NBID_carbon_monoxide);
            values.put("str_carbon_monoxide",str_carbon_monoxide);


            db.insert("DataLogo",null,values);//把数据插入表中
            values.clear();//如果要重复插入需要关闭一下
            get_middleware_logoRevise();//跳转方法


        }else{
            Toast.makeText(MainActivity_data_processing.this,"设备标识不能为空",Toast.LENGTH_LONG).show();
        }
    }
@SuppressLint("Range")
private void get_middleware_logoRevise(){

    SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLliteDatabase 的类去承接新建表的对象；
    Cursor cursor=db.query("DataLogo",null,null,null,null,null,null);
    if(cursor.moveToFirst()){
        do{
//                              对数据库提出的文本对象进行转换此处已转为String的类型；
           String str=cursor.getString(cursor.getColumnIndex("middleware_logoRevise"));
if (str!=null){
    int_middleware_logoRevise=Integer.parseInt(str);
}
        }while(cursor.moveToNext());
    }
    cursor.close();//关闭查询
    Log.d("TAG--->", "get_middleware_logoRevise: 第二部 "+int_middleware_logoRevise);
    if (int_middleware_logoRevise==0){
        Intent set_intent=new Intent(MainActivity_data_processing.this,MainActivity_login.class);
        startActivity(set_intent);
    }
    if (int_middleware_logoRevise==1){
        ContentValues values1=new ContentValues();
         int  set_int_middleware_logoRevise=0;
        values1.put("middleware_logoRevise",set_int_middleware_logoRevise);
        db.insert("DataLogo",null,values1);
        Intent intent_middleware_logoRevise=new Intent(MainActivity_data_processing.this, Activity_Smart_Guardian.class);
              // .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//清楚activity的生命周期


        startActivity(intent_middleware_logoRevise);
    }

}
    @SuppressLint("Range")
    private void read_Sqlite(){
        SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLliteDatabase 的类去承接新建表的对象；
        Cursor cursor=db.query("DataLogo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
//                              对数据库提出的文本对象进行转换此处已转为String的类型；
                String implement_home_intent=cursor.getString(cursor.getColumnIndex("home_intent"));
                if (implement_home_intent!=null){
                    int home=Integer.parseInt(implement_home_intent);
                    if (home==1){
                        str_Central_gateway=cursor.getString(cursor.getColumnIndex("str_Central_gateway"));
                        set_text(editText_Central_gateway,str_Central_gateway);


//                editText_temp,editText_hump,editText_noise,editText_light,
//                str_temp,str_hump,str_noise,str_light,
                        str_temp=cursor.getString(cursor.getColumnIndex("str_temp"));
                        set_text(editText_temp,str_temp);
                        str_hump=cursor.getString(cursor.getColumnIndex("str_hump"));
                        set_text(editText_hump,str_hump);
                        str_noise=cursor.getString(cursor.getColumnIndex("str_noise"));
                        set_text(editText_noise,str_noise);
                        str_light=cursor.getString(cursor.getColumnIndex("str_light"));
                        set_text(editText_light,str_light);

//                editText_CO2,editText_wind,editText_human,editText_flame,
//                str_co2,str_wind,str_human,str_flame,
                        str_co2=cursor.getString(cursor.getColumnIndex("str_co2"));
                        set_text(editText_CO2,str_co2);
                        str_wind=cursor.getString(cursor.getColumnIndex("str_wind"));
                        set_text(editText_wind,str_wind);
                        str_human=cursor.getString(cursor.getColumnIndex("str_human"));
                        set_text(editText_human,str_human);
                        str_flame=cursor.getString(cursor.getColumnIndex("str_flame"));
                        set_text(editText_flame,str_flame);

//                editText_flashlight,editText_fan,editText_air_conditioner,editText_Warning_Light,
//                str_flashlight,str_fan,str_air_conditioner,str_Warning_Light,
                        str_flashlight=cursor.getString(cursor.getColumnIndex("str_flashlight"));
                        set_text(editText_flashlight,str_flashlight);
                        str_fan=cursor.getString(cursor.getColumnIndex("str_fan"));
                        set_text(editText_fan,str_fan);
                        str_smoke=cursor.getString(cursor.getColumnIndex("str_smoke"));
                        set_text(editText_smoke,str_smoke);
                        str_Warning_Light=cursor.getString(cursor.getColumnIndex("str_Warning_Light"));
                        set_text(editText_Warning_Light,str_Warning_Light);


//                str_curtains_open,str_rgb,str_curtain_close,
//                editText_curtains_open,editText_rgb,editText_curtain_close,
                        str_curtains_open=cursor.getString(cursor.getColumnIndex("str_curtains_open"));
                        set_text(editText_curtains_open,str_curtains_open);
                        str_rgb=cursor.getString(cursor.getColumnIndex("str_rgb"));
                        set_text(editText_rgb,str_rgb);
                        str_monitor=cursor.getString(cursor.getColumnIndex("str_monitor"));
                        set_text(editText_monitor,str_monitor);

//                editText_NBID_Methane,editText_Methane,     设备  数据
//                str_NBID_Methane,str_Methane,

                        str_NBID_Methane=cursor.getString(cursor.getColumnIndex("str_NBID_Methane"));
                        set_text(editText_NBID_Methane,str_NBID_Methane);
                        str_Methane=cursor.getString(cursor.getColumnIndex("str_Methane"));
                        set_text(editText_Methane,str_Methane);

//                editText_NBID_carbon_monoxide,editText_carbon_monoxide
//                str_NBID_carbon_monoxide,str_carbon_monoxide
                        str_NBID_carbon_monoxide=cursor.getString(cursor.getColumnIndex("str_NBID_carbon_monoxide"));
                        set_text(editText_NBID_carbon_monoxide,str_NBID_carbon_monoxide);
                        str_carbon_monoxide=cursor.getString(cursor.getColumnIndex("str_carbon_monoxide"));
                        set_text(editText_carbon_monoxide,str_carbon_monoxide);

                    }
                    if (home==0){
                        Log.d("----->", "read_Sqlite: 首次执行");
                    }
                }

            }while(cursor.moveToNext());
        }
        cursor.close();//关闭查询

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.butt_return:
//                Intent_view();
                get_middleware_logoRevise();
                break;
            case R.id.butt_confirm:
                but_Bundle();
               break;
        }
    }
    public void set_text(EditText text,String str){
        text.setText(str);
    }
}