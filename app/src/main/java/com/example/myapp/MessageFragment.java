package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

/**
 * 子项详细
 * Created by 黄荣聪 on 2017/11/23.
 */

public class MessageFragment extends Fragment{
    private static final String ARG_MESSAGE_ID="message_id";

    private UserMessage mUserMessage;

    private EditText mPingTai;
    private EditText mUserName;
    private EditText mUser;
    private EditText mPassWord;
    private EditText mEmail;
    private EditText mPhone;

    private Button okButton;

    public static MessageFragment newInstance(UUID messageId){
        Bundle args=new Bundle();
        args.putSerializable(ARG_MESSAGE_ID,messageId);

        MessageFragment fragment=new MessageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID messageId=(UUID)getArguments().getSerializable(ARG_MESSAGE_ID);
        mUserMessage=MessageLab.get(getActivity()).getMessage(messageId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        MessageLab.get(getActivity()).updateMessage(mUserMessage);
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

        mPingTai=v.findViewById(R.id.message_pingtai_edit);
        mUserName=v.findViewById(R.id.message_username_edit);
        mUser=v.findViewById(R.id.message_user_edit);
        mPassWord=v.findViewById(R.id.message_password_edit);
        mEmail=v.findViewById(R.id.message_email_edit);
        mPhone=v.findViewById(R.id.message_phone_edit);
        okButton=v.findViewById(R.id.message_ok);

        //将Message相关信息填入编辑框内
        mPingTai.setText(mUserMessage.getPingtai());
        mUserName.setText(mUserMessage.getUserName());
        mUser.setText(mUserMessage.getUser());
        mPassWord.setText(mUserMessage.getPassword());
        mEmail.setText(mUserMessage.getEmail());
        mPhone.setText(mUserMessage.getPhone());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changMessage();
                getActivity().finish();
            }
        });

        return v;
    }

    //相应的修改信息
    private void changMessage(){
        mUserMessage.setPingtai(mPingTai.getText().toString());
        mUserMessage.setUserName(mUserName.getText().toString());
        mUserMessage.setUser(mUser.getText().toString());
        mUserMessage.setPassword(mPassWord.getText().toString());
        mUserMessage.setEmail(mEmail.getText().toString());
        mUserMessage.setPhone(mPhone.getText().toString());
    }
}
