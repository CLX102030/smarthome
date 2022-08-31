package com.example.smarthome.Fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.MainActivity;
import com.example.smarthome.R;

import androidx.fragment.app.Fragment;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class living_roomFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static boolean isStop;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public View view;

    //灯泡风扇
    public TextView button_fan_open,button_fan_close,button_lamp_open,button_lamp_close;
    //窗帘
    private TextView but_curtains_open_tv,but_curtains_pause_tv,but_curtains_close_tv;
    //rgb灯带
    private SeekBar seekBar_R,seekBar_G,seekBar_B;
    int int_R,int_G,int_B;
    private String str_Rgb="";
    //图片
    public ImageView imageView_lamp,imageView_fan;
    public int[] image ={R.mipmap.lamp_on,R.mipmap.lamp_off};
    public AnimationDrawable animationDrawable;

    //云控制
    private String Token;
    private SqLite mSqlite;
    private NetWorkBusiness netWorkBusiness;


    //标识                网关id              风扇        灯泡            窗帘      rgb灯带
    private  String str_Central_gateway, str_fan,str_flashlight,str_curtains,str_rgb;
    public living_roomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment living_roomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static living_roomFragment newInstance(String param1, String param2) {
        living_roomFragment fragment = new living_roomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            Log.d("Bundle----->", "数据恢复"+int_R);
            int_R=savedInstanceState.getInt("int_R");
            int_G=savedInstanceState.getInt("int_G");
            int_B=savedInstanceState.getInt("int_B");
            seekBar_R.setProgress(int_R);
        }
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_living_room, container, false);
        //数据库
        mSqlite=new SqLite(getActivity(),"smart_home",null,1);
        mSqlite.getWritableDatabase();
        intiView();
        int_image();
        Int_Token();

        if (Token!=null){
            netWorkBusiness=new NetWorkBusiness(Token,"http://www.nlecloud.com");
            Log.d("control--->", "netWorkBusiness网址附值成功！");
        }
        if (Token==null){
            Log.d("control--->", "Token:"+Token);
        }
        Int_Sqlite();
        get_RGB();
        return view;
    }

    public void intiView(){
        //图片特效控件绑定
        button_fan_open=view.findViewById(R.id.butt_fan_open);
        button_fan_close=view.findViewById(R.id.button_fan_close);
        button_lamp_open=view.findViewById(R.id.button_lamp_open);
        button_lamp_close=view.findViewById(R.id.button_lamp_close);
        imageView_lamp=view.findViewById(R.id.image_lamp);
        imageView_fan=view.findViewById(R.id.image_fan);
        button_fan_open.setOnClickListener(this);
        button_fan_close.setOnClickListener(this);
        button_lamp_open.setOnClickListener(this);
        button_lamp_close.setOnClickListener(this);

//窗帘控制初始化
        but_curtains_open_tv=view.findViewById(R.id.curtains_open_tv);
        but_curtains_pause_tv=view.findViewById(R.id.curtains_pause_tv);
        but_curtains_close_tv=view.findViewById(R.id.curtains_close_tv);
        but_curtains_open_tv.setOnClickListener(this::onClick);
        but_curtains_pause_tv.setOnClickListener(this::onClick);
        but_curtains_close_tv.setOnClickListener(this::onClick);

//RGB灯带
        seekBar_R=view.findViewById(R.id.seekBar_R);
        seekBar_G=view.findViewById(R.id.seekBar_G);
        seekBar_B=view.findViewById(R.id.seekBar_B);


    }



    @SuppressLint("Range")
    private void Int_Token(){
        //设备标识符
        SQLiteDatabase db = mSqlite.getWritableDatabase();//定义一个SQLiteDatabase 的类去承接新建表的对象；
        Cursor cursor=db.query("Initialize",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String str=cursor.getString(cursor.getColumnIndex("Token"));
                if (str!=null){
                    Token=str;
                }
            }while(cursor.moveToNext());
            if (Token!=null){
                Log.d("token----->", "token正常控制模块");
            }
            if(Token==null){
                Log.d("token----->", "Int_Token: 数据库故障 ");
            }
        }
        cursor.close();//关闭查询

    }

    @SuppressLint("Range")
    private void Int_Sqlite(){
        //设备标识符
        SQLiteDatabase db = mSqlite.getWritableDatabase();//定义一个SQLiteDatabase 的类去承接新建表的对象；
        Cursor cursor=db.query("DataLogo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String logo0=cursor.getString(cursor.getColumnIndex("str_Central_gateway"));
                String logo=cursor.getString(cursor.getColumnIndex("str_fan"));
                String logo1=cursor.getString(cursor.getColumnIndex("str_flashlight"));
                String logo2=cursor.getString(cursor.getColumnIndex("str_curtains_open"));
                String logo3=cursor.getString(cursor.getColumnIndex("str_rgb"));
                if (logo0!=null){
                    str_Central_gateway=logo0;
                }
                if (logo!=null){
                    str_fan=logo;
                }
                if (logo1!=null){
                    str_flashlight=logo1;
                }
                if (logo2!=null){
                    str_curtains=logo2;
                }
                if (logo3!=null){
                    str_rgb=logo3;
                    Log.d("RGB--->", "logo3    数据库      :"+logo3);
                }

            }while(cursor.moveToNext());
            if (Token!=null){
                Log.d("token----->", "token正常控制模块");
            }
            if(Token==null){
                Log.d("token----->", "Int_Token: 数据库故障 ");
            }
        }
        cursor.close();//关闭查询

    }



    public void int_image(){
        imageView_fan.setImageResource(R.drawable.animation);
        animationDrawable= (AnimationDrawable) imageView_fan.getDrawable();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.butt_fan_open:
                animationDrawable.start();
                        set_control(str_Central_gateway,str_fan,1);
                Log.d("control--->", "onClick: id:"+str_Central_gateway+"fan:"+str_fan);
                break;
            case R.id.button_fan_close:
                animationDrawable.stop();
                         set_control(str_Central_gateway,str_fan,0);
                break;
            case R.id.button_lamp_open:
                Log.d("control--->", "onClick: id:"+str_Central_gateway+"zmd:"+str_flashlight);
                  set_control(str_Central_gateway,str_flashlight,1);
                imageView_lamp.setImageResource(image[0]);
                break;
            case R.id.button_lamp_close:
                set_control(str_Central_gateway,str_flashlight,0);
                imageView_lamp.setImageResource(image[1]);
                break;
            case R.id.curtains_open_tv:
                set_control(str_Central_gateway,str_curtains,1);
                break;
            case R.id.curtains_pause_tv:
                set_control(str_Central_gateway,str_curtains,2);
                break;
            case R.id.curtains_close_tv:
                set_control(str_Central_gateway,str_curtains,0);
                break;
            default:
                break;
        }
    }

    private void set_control(String id,String tag,int data) {

        if (netWorkBusiness != null) {

            netWorkBusiness.control(id, tag, data, new NCallBack<BaseResponseEntity>(getActivity().getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity baseResponseEntity) {

                }
            });
        }
        if (netWorkBusiness==null){
            Log.d("control--->", "netWorkBusiness为null");
        }
    }



    public void get_RGB(){

        new Thread(new Runnable() {
               @Override
               public void run() {

                   while (!isStop){
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }

                       if (MainActivity.loop==false){
                           isStop=true;
                           break;
                       }

                       seekBar_R.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                           @Override
                           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                                   Log.d("seekBar--->", "onProgressChanged: RRRRR        " + i);
                                   int_R = i;
                                   //seekBar_R.setProgress(int_R);


                           }

                           @Override
                           public void onStartTrackingTouch(SeekBar seekBar) {

                           }

                           @Override
                           public void onStopTrackingTouch(SeekBar seekBar) {

                           }
                       });
                       seekBar_G.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                           @Override
                           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                                   Log.d("seekBar--->", "onProgressChanged: GGGGGGGGGG       " + i);
                                   int_G = i;
                                   //seekBar_G.setProgress(int_G);

                           }

                           @Override
                           public void onStartTrackingTouch(SeekBar seekBar) {

                           }

                           @Override
                           public void onStopTrackingTouch(SeekBar seekBar) {

                           }
                       });
                       seekBar_B.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                           @Override
                           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                                   Log.d("seekBar--->", "onProgressChanged: BBBBBBBBBB      " + i);
                                   int_B = i;
                                   //seekBar_B.setProgress(int_B);

                           }

                           @Override
                           public void onStartTrackingTouch(SeekBar seekBar) {

                           }

                           @Override
                           public void onStopTrackingTouch(SeekBar seekBar) {

                           }
                       });

                       String set_strRGB=int_R+","+int_G+","+int_B;

                       if (netWorkBusiness!=null){
                           Log.d("RGB--->", "run: 网关:  "+str_Central_gateway+"设备id:  "+str_rgb+"控制值："+int_R+int_G+int_B);
                           netWorkBusiness.control(str_Central_gateway,str_rgb,set_strRGB, new NCallBack<BaseResponseEntity>(getActivity()) {
                               @Override
                               protected void onResponse(BaseResponseEntity baseResponseEntity) {

                               }
                           });
                       }
                   }
               }
           }).start();

    }



}