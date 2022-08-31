package com.example.smarthome;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.Help_class.SqLite;
import com.example.smarthome.personal_center.Activity_Smart_Guardian;

import androidx.appcompat.app.AppCompatActivity;
import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity_login extends AppCompatActivity implements View.OnClickListener {
    private NetWorkBusiness netWorkBusiness;
    private EditText editText_account,editText_Password;
    private TextView butt_logo,butt_log_in,butt_Forgot_password;
    public String account="",password="";
    public String Token;
    public int history_login=-1;
    public SqLite msqLite;
    public String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity_Smart_Guardian.addActivity(MainActivity_login.this);

        setContentView(R.layout.activity_main_login);
        Int_view();
        msqLite=new SqLite(this,"smart_home",null,1);
        msqLite.getWritableDatabase();

    }
    private void Int_view(){
        editText_account=findViewById(R.id.editText_account);
        editText_Password=findViewById(R.id.editText_Password);
        butt_logo=findViewById(R.id.butt_logo);
        butt_log_in=findViewById(R.id.butt_log_in);
        butt_Forgot_password=findViewById(R.id.butt_Forgot_password);
        butt_logo.setOnClickListener(this);
        butt_log_in.setOnClickListener(this);
        butt_Forgot_password.setOnClickListener(this);
    }
    private void INt_NetWorkBusiness(){
        account=editText_account.getText().toString();
        password=editText_Password.getText().toString();
        if (!account.equals("") && !password.equals("")){
            new NetWorkBusiness("","http://www.nlecloud.com/").signIn(new SignIn(account, password), new NCallBack<BaseResponseEntity<User>>(getApplicationContext()) {
                @Override
                protected void onResponse(BaseResponseEntity<User> userBaseResponseEntity) {
                    if (userBaseResponseEntity!=null){
                        if(userBaseResponseEntity.getStatus()==0){
                        Token=userBaseResponseEntity.getResultObj().getAccessToken();
                            if (Token!=null){
                                account_sql();//插入账号密码密钥方法
                                Log.d("token----->", "登陆成功，账号密码标识已存入 ");
                                Intent intent=new Intent(MainActivity_login.this,MainActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            if (Token==null){
                                Toast.makeText(MainActivity_login.this,"请检查云平台账户或云平台api",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(MainActivity_login.this,"账号或密码错误",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(MainActivity_login.this,"请检查账号密码或网络",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            Toast.makeText(this,"账号密码不能为空",Toast.LENGTH_LONG).show();
        }
    }
    private void Intent_logo(){
        Intent intent=new Intent(this,MainActivity_data_processing.class);
        startActivity(intent);
    }
    private void account_sql(){
        //账号account    password
        SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLliteDatabase 的类去承接新建表的对象；
        ContentValues values=new ContentValues();//定义一个并实例化contentValus 的类；
values.put("account",account);
values.put("password",password);
values.put("Token",Token);
db.insert("Initialize",null,values);
values.clear();

        }


    @SuppressLint("Range")
    public void Sqlite(){
        SQLiteDatabase db = msqLite.getWritableDatabase();//定义一个SQLliteDatabase 的类去承接新建表的对象；
        Cursor cursor=db.query("DataLogo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String log=cursor.getString(cursor.getColumnIndex("history_login"));
                if (log!=null) {

                    history_login=Integer.parseInt(log);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();//关闭查询
        Log.d("a---->", "Sqlite: 登录1111           " + history_login);
        if (history_login == 2) {
            //证明设备标识无误后，然后对令牌账号密码进行判断
            INt_NetWorkBusiness();
        }
        else {
            Toast.makeText(MainActivity_login.this, "请先填写设备标识", Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.butt_logo:
                Intent_logo();
                break;
            case R.id.butt_log_in:
                Sqlite();
                break;
            case R.id.butt_Forgot_password:
               Uri uri = Uri.parse("http://www.nlecloud.com/my/forget");
               Intent uri_intent=new Intent(ACTION_VIEW,uri);
               startActivity(uri_intent);
               break;
        }
    }
}