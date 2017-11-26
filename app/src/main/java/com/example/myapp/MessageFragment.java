package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;

/**
 * 子项详细
 * Created by 黄荣聪 on 2017/11/23.
 */

public class MessageFragment extends Fragment{
    private static final String TAG="MessageFragment";

    private static final String ARG_MESSAGE_ID="message_id";
    private static final String ARG_MESSAGE_IS_NEW="message_is_new";

    private UserMessage mUserMessage;
    private UUID messageId;
    private boolean IsFirstNew;     //是否是第一次创建

    private EditText mPingTai;
    private EditText mUserName;
    private EditText mUser;
    private EditText mPassWord;
    private EditText mEmail;
    private EditText mPhone;

    private ImageView mIVEmail;
    private ImageView mIVPhone;
    private ImageView mIVPassword;

    private Button okButton;

    public static MessageFragment newInstance(UUID messageId,boolean isFirstNew){
        Bundle args=new Bundle();
        args.putSerializable(ARG_MESSAGE_ID,messageId);
        args.putSerializable(ARG_MESSAGE_IS_NEW,isFirstNew);

        MessageFragment fragment=new MessageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageId=(UUID)getArguments().getSerializable(ARG_MESSAGE_ID);
        IsFirstNew= (boolean) getArguments().getSerializable(ARG_MESSAGE_IS_NEW);
        Log.d(TAG,"isFirstNew :"+IsFirstNew);
        mUserMessage=MessageLab.get(getActivity()).getMessage(messageId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (MessageLab.get(getActivity()).getMessage(messageId)!=null) {
            MessageLab.get(getActivity()).updateMessage(mUserMessage);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_message,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_message_delete:
                MessageLab.get(getActivity()).deleteMessage(mUserMessage);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_message,container,false);
        init(v);

        if (IsFirstNew){
            MessageLab.get(getActivity()).deleteMessage(mUserMessage);
        }

        return v;
    }

    //提示用户有些信息需要完善
    private void messageIsRight(){
        if ((!isEmail(mEmail.getText().toString()))&&(!isPhone(mPhone.getText().toString()))){
            Toast.makeText(getActivity(), "有多项填写内容不正确,建议重新填写", Toast.LENGTH_SHORT).show();
        }
        changMessage();
    }

    //相应的修改信息
    private void changMessage(){
        if (IsFirstNew){
            mUserMessage=new UserMessage(messageId);
            MessageLab.get(getActivity()).addMessage(mUserMessage);
        }
        mUserMessage.setPingtai(mPingTai.getText().toString());
        mUserMessage.setUserName(mUserName.getText().toString());
        mUserMessage.setUser(mUser.getText().toString());
        mUserMessage.setPassword(mPassWord.getText().toString());
        mUserMessage.setEmail(mEmail.getText().toString());
        mUserMessage.setPhone(mPhone.getText().toString());
    }

    /**
     * 用于判断字符串是否为邮箱格式
     * @param str 需要判断的字符串
     * @return true为邮箱格式。false不是邮箱格式
     */
    private boolean isEmail(String str){
        boolean isEmail=false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)){
            isEmail=true;
        }
        return isEmail;
    }

    /**
     * 判断字符串是否为手机号码格式是否正确
     * @param phone 需判断的字符串
     * @return true正确，false则不正确
     */
    private boolean isPhone(String phone){
        if (TextUtils.isEmpty(phone)){      //如果
            return false;
        }else if (phone.length()==11){
            String telRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";     // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
            if (phone.matches(telRegex)){
                return true;            //若匹配正则表达式成功，则返回true
            }else{
                return false;           //匹配不成功，则返回false
            }
        }else{
            return false;               //其他情况为false
        }
    }

    /**
     * 判断字符串是否含有中文
     * @param str 待判断的字符串
     * @return true表示含有中文，false表示没有中文
     */
    private boolean hasChinese(String str){
        boolean hasChinese=false;
        String chinese="[\\u4e00-\\u9fa5]+";
        if (!TextUtils.isEmpty(str)){
            for (int i=0;i<str.length();i++){
                //获取一个字符
                String temp=str.substring(i,i+1);
                //判断是否为中文字符
                if (temp.matches(chinese)){
                    return true;
                }
            }
        }else{
            return true;            //当为空时，显示格式错误
        }
        return hasChinese;
    }

    /**
     * 控件的初始化
     * @param v 视图view
     */
    private void init(View v){
        mPingTai=v.findViewById(R.id.message_pingtai_edit);
        mUserName=v.findViewById(R.id.message_username_edit);
        mUser=v.findViewById(R.id.message_user_edit);
        mPassWord=v.findViewById(R.id.message_password_edit);
        mEmail=v.findViewById(R.id.message_email_edit);
        mPhone=v.findViewById(R.id.message_phone_edit);
        okButton=v.findViewById(R.id.message_ok);

        mIVEmail=v.findViewById(R.id.iv_edit_email);
        mIVPhone=v.findViewById(R.id.iv_edit_phone);
        mIVPassword=v.findViewById(R.id.iv_edit_password);

        //将Message相关信息填入编辑框内
        mPingTai.setText(mUserMessage.getPingtai());
        mUserName.setText(mUserMessage.getUserName());
        mUser.setText(mUserMessage.getUser());
        mPassWord.setText(mUserMessage.getPassword());
        mEmail.setText(mUserMessage.getEmail());
        mPhone.setText(mUserMessage.getPhone());

        addTextChang();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageIsRight();
                getActivity().finish();
            }
        });
    }

    /**
     * 为邮箱，手机号码。两个编辑框，添加文本变化监听
     */
    private void addTextChang(){
        if (!IsFirstNew){
            setImageViewDrawable(mIVEmail,isEmail(mUserMessage.getEmail()));
            setImageViewDrawable(mIVPhone,isPhone(mUserMessage.getPhone()));
            setImageViewDrawable(mIVPassword,!hasChinese(mUserMessage.getPassword()));
        }

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setImageViewDrawable(mIVEmail,isEmail(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setImageViewDrawable(mIVPhone,isPhone(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setImageViewDrawable(mIVPassword,!hasChinese(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 输入格式是否正确提示
     * @param iv 需要改变图片的imageview
     * @param isRight 格式正确与否
     */
    private void setImageViewDrawable(ImageView iv,boolean isRight){
        if (isRight){
            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_ok,null));
        }else{
            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_no,null));
        }
    }
}
