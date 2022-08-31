package com.example.smarthome.personal_center;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.R;
import com.example.smarthome.Smart_Security.Smart_SecurityActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_HistoricalData extends AppCompatActivity implements View.OnClickListener {
//历史数据
    private ImageView but_break_data;
    private ListView listView;
    private List all=new ArrayList<>();
    private SqLite msqLite;
    private SQLiteDatabase db;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data);

        Activity_Smart_Guardian.addActivity(Activity_HistoricalData.this);

        but_break_data=findViewById(R.id.but_break_data);
        but_break_data.setOnClickListener(this::onClick);
        listView=findViewById(R.id.date_listView_tv);
        //数据库
        msqLite=new SqLite(this,"smart_home",null,1);
        msqLite.getWritableDatabase();
        db = msqLite.getWritableDatabase();
        cursor=db.query("Historical_data",null,null,null,null,null,null);
        traverseData();
    }


    @SuppressLint("Range")
    public void traverseData(){
        if (cursor==null){
            return;
        }
        if(cursor.moveToFirst()){
            do{
//                              对数据库提出的文本对象进行转换此处已转为String的类型;
                String data=cursor.getString(cursor.getColumnIndex("data"));
                String tempData=cursor.getString(cursor.getColumnIndex("tempData"));
                String humpData=cursor.getString(cursor.getColumnIndex("humpData"));
                String lightData=cursor.getString(cursor.getColumnIndex("lightData"));
                String co2Data=cursor.getString(cursor.getColumnIndex("co2Data"));
                String noiseData=cursor.getString(cursor.getColumnIndex("noiseData"));

                try {
                    if (tempData!=null&&humpData!=null&&lightData!=null
                                                    &&co2Data!=null&&noiseData!=null){
                        all.add("保存时间:"+data+"\r\r温度:"+tempData+"C°\r\r\r\r湿度:"+humpData+"%RH\r\r光照:"+lightData+"lx\r\r二氧化碳:"+co2Data+"ppm\r\r噪音:"+noiseData+"db\r\r"
                                + Smart_SecurityActivity.getListData()
                        );

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();//关闭查询

        try {
            if (all!=null){
                Log.d("all--->", "添加数据");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                           (this,android.R.layout.simple_expandable_list_item_1,all);
                //
                listView.setAdapter(adapter);
    //            setListViewHeight(listView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置listview高度的方法
     * @param listView
     */
    public void setListViewHeight(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {//listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);   //获得每个子item的视图
            listItem.measure(0, 0);   //先判断写入的widthMeasureSpec和heightMeasureSpec是否和当前的值相等，如果不等，重新调用onMeasure()，计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();   //累加不解释，统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   //加上每个item之间的距离，listView.getDividerHeight()获取子项间分隔符占用的高度
        listView.setLayoutParams(params);//params.height最后得到整个ListView完整显示需要的高度
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_break_data:
                Intent intent_data=new Intent(Activity_HistoricalData.this, Activity_Smart_Guardian.class);
                startActivity(intent_data);
               break;
            default:
                break;
        }
    }
}