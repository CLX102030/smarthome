package com.example.smarthome.Help_class;

import java.io.Serializable;

public class Logo_data implements Serializable {
    public  Logo_data(){

    }
   public String Token;
    public void setToken(String str){
        Token=str;
    }
    public String getToken(){
        return this.Token;
    }

}