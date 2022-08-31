package com.example.smarthome.Help_class;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class SqLite extends SQLiteOpenHelper {
    //以下为获取设备标识注释
    //                物联网中心网关设备ID
    //                温度           湿度          噪音            光照
    //                Co2           风速          人体            火焰
    //                照明灯          风扇        烟雾           警示灯
    //                窗帘          rgb         监控
    //           NBIOT设备ID    甲烷
    //           NBIOT设备Id    一氧化碳
    public static final String CREAT_Initialize="create table Initialize("
            +"account,"//登录账号
            +"Token,"//token密钥存储
            +"switch_int,"//默认更新数据 0为开 1为关swich
            +"switch_logo,"//switch组件标识
            +"password)";//登录密码
    public static final String CREAT_Data="create table DataLogo("
            //主页跳转判断用    判断标识数据   中间件标识修改判断返回界面（云平台标识修改）    登陆历史
            +"home_intent,data_logo,middleware_logoRevise,history_login,"
            +"str_Central_gateway," +"str_temp," +"str_hump," +"str_noise," +"str_light,"
            +"str_co2," +"str_wind," +"str_human," +"str_flame,"
            +"str_flashlight," +"str_fan," +"str_smoke," +"str_Warning_Light,"
            +"str_curtains_open," +"str_rgb," +"str_monitor,"
            +"str_NBID_Methane," +"str_Methane," +"str_NBID_carbon_monoxide," +"str_carbon_monoxide)";
    public static final String HISTORICAL_DATA ="create table Historical_data("
            +"id integer primary key autoincrement,"+ "tempData," + "humpData," + "lightData," +
            "co2Data," + "noiseData," + "carbonMonoxideData,"+ //一氧化痰
            "methaneData," + "flameData," + "data,"+ "humanBodyData)";//人体

    public SqLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_Initialize);
        db.execSQL(CREAT_Data);
        db.execSQL(HISTORICAL_DATA);
        Log.d("SQL--->", "新建表成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}