package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class MessageActivity extends SingleFragmentActivity{
    private static final String EXTRA_MESSAGE_ID="com.hrc.myapp.message_id";
    private static final String EXTRA_MESSAGE_IS_NEW="com.hrc.myapp.message_isnew";

    public static Intent newIntent(Context packageContext, UUID messageid, boolean isFirstNew){
        Intent intent=new Intent(packageContext,MessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE_ID,messageid);
        intent.putExtra(EXTRA_MESSAGE_IS_NEW,isFirstNew);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID messageId= (UUID) getIntent().getSerializableExtra(EXTRA_MESSAGE_ID);
        boolean isFirstNew= (boolean) getIntent().getSerializableExtra(EXTRA_MESSAGE_IS_NEW);
        return MessageFragment.newInstance(messageId,isFirstNew);
    }
}
