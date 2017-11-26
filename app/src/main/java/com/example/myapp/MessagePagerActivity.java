package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * 可左右切换的子项视图
 * Created by 黄荣聪 on 2017/11/23.
 */

public class MessagePagerActivity extends AppCompatActivity {
    private static final String EXTRA_MESSAGE_ID="com.hrc.myapp.message_id";
    private static final String EXTRA_MESSAGE_IS_NEW="com.hrc.myapp.message_isnew";

    private ViewPager mViewPager;
    private List<UserMessage> mMessages;

    public static Intent newIntent(Context packageContext, UUID messageid,boolean isFirstNew){
        Intent intent=new Intent(packageContext,MessagePagerActivity.class);
        intent.putExtra(EXTRA_MESSAGE_ID,messageid);
        intent.putExtra(EXTRA_MESSAGE_IS_NEW,isFirstNew);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_pager);

        UUID messageId= (UUID) getIntent().getSerializableExtra(EXTRA_MESSAGE_ID);
        final boolean isFirstNew= (boolean) getIntent().getSerializableExtra(EXTRA_MESSAGE_IS_NEW);

        mViewPager=findViewById(R.id.activity_messager_pager_view_pager);

        mMessages=MessageLab.get(this).getUserMessages();
        FragmentManager fragmentManager=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                UserMessage message=mMessages.get(position);
                return MessageFragment.newInstance(message.getId(),isFirstNew);
            }

            @Override
            public int getCount() {
                return mMessages.size();
            }
        });

        for (int i=0;i<mMessages.size();i++){
            if (mMessages.get(i).getId().equals(messageId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
