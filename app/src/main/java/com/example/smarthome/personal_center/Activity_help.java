package com.example.smarthome.personal_center;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smarthome.Help_class.MyBaseExpandableListAdapter;
import com.example.smarthome.Help_class.list_help.Group;
import com.example.smarthome.Help_class.list_help.Item;
import com.example.smarthome.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_help extends AppCompatActivity implements View.OnClickListener {
    private ImageView but_break_tv1;
    private ExpandableListView expandableListView;
    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private Context mContext;
    private MyBaseExpandableListAdapter myAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();//隐藏标题栏方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Activity_Smart_Guardian.addActivity(Activity_help.this);

        but_break_tv1=findViewById(R.id.but_break_tv1);
        but_break_tv1.setOnClickListener(this);
        expandableListView =findViewById(R.id.list_view_tv);
        mContext = Activity_help.this;
        Data_List();
    }
    public void Data_List(){
        //空格是为了解决text控件无法全屏的问题
        gData=new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        gData.add(new Group("1.常见问题\t\t\t                                                     "));
        gData.add(new Group("2.售后服务                                                           "));
        gData.add(new Group("3.关于我们                                                           "));
        lData=new ArrayList<Item>();
        //组
        lData.add(new Item(" 1.设备不上线怎么处理\n" +
                "        检查网络配置，若网络配置无问题后，请检查账号，云平台项目和项目标识等是否一致;"));
        lData.add(new Item("  2.应用bug反馈                                                           "));
        iData.add(lData);
        //组
        lData = new ArrayList<Item>();
        lData.add(new Item("1.电话:17538264963                                                           "));
        lData.add(new Item("2.企点客服                                                           "));
        iData.add(lData);
        //组
        lData = new ArrayList<Item>();
        lData.add(new Item("用户隐私政策                                                           "));
//        lData.add(new Item(""));
        iData.add(lData);

        myAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        expandableListView.setAdapter(myAdapter);
        //为列表设置点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition==0){
                    if (childPosition==0){
                        Log.d("----->", "onChildClick: 常见问题解决办法");
                    }
                    if (childPosition==1){
                        Intent feedback_intent=new Intent(Activity_help.this,Activity_help_feedback.class);
                        startActivity(feedback_intent);
                        Log.d("----->", "onChildClick: bug反馈");
                    }
                }
             if (groupPosition==1){
                 if(childPosition==0){
                     Uri uri=Uri.parse("tel:"+"17538264963");
                     Intent intent=new Intent(Intent.ACTION_DIAL,uri);
                     startActivity(intent);
                     Log.d("----->", "onChildClick: 电话");
                 }
                 if(childPosition==1){
                     try {
                         String url="mqqwpa://im/chat?chat_type=wpa&uin=2781206474";
                         startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                         Log.d("----->", "onChildClick: QQ客服");
                     } catch (Exception e) {
                         e.printStackTrace();
                         Toast.makeText(Activity_help.this,"手机无qq正在跳转下载",Toast.LENGTH_SHORT).show();
                         String url="https://im.qq.com/download";
                         startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                     }
                 }
             }
                if (groupPosition==2){
                    if (childPosition==0){
                        Intent privacy_intent=new Intent(Activity_help.this,Activity_help_privacy.class);
                        startActivity(privacy_intent);
                        Log.d("----->", "onChildClick: 用户政策");
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.but_break_tv1:
            Intent intent=new Intent(Activity_help.this,personal_Activity.class);
            startActivity(intent);
            break;
    }
    }


}