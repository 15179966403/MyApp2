package com.example.myapp;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * 信息对象
 * Created by 黄荣聪 on 2017/11/23.
 */

public class UserMessage {
    public static final int TYPE_OF_USUALLY=0;
    public static final int TYPE_OF_PHONE=1;
    public static final int TYPE_OF_EMAIL=2;

    private UUID mId;       //每一个对象的唯一标识id
    private String mPingtai="";    //用户所注册的平台
    private String mUserName="";   //用户在该平台的名称
    private String mUser="";       //用户账号
    private String mPassword="";    //用户密码
    private String mEmail="";       //用户绑定的邮箱
    private String mPhone="";       //用户绑定的手机号码
    private String mDate="";        //用户创建的日期
    private int mType=TYPE_OF_USUALLY;            //账号的类别

    public UserMessage(){
        mId=UUID.randomUUID();
    }

    public UserMessage(UUID id){
        mId=id;
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        Date date=new Date(System.currentTimeMillis());
        mDate=format.format(date);
    }

    public String getPingtai() {
        return mPingtai;
    }

    public void setPingtai(String pingtai) {
        mPingtai = pingtai;
    }

    public UUID getId() {
        return mId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
