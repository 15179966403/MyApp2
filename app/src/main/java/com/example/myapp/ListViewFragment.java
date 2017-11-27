package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 主界面列表类
 * Created by 黄荣聪 on 2017/11/23.
 */

public class ListViewFragment extends Fragment{

    private RecyclerView mMessageRecyclerView;      //列表视图
    private MessageAdapter mAdapter;                //列表的适配器

    //使用单例模式创建Fragment
    public static ListViewFragment newInstance(){
        return new ListViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list_view,container,false);

        mMessageRecyclerView=v.findViewById(R.id.message_recycler_view);    //实例化列表
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));      //设置列表的布局方式为垂直方式

        updateUI();     //列表视图的刷新

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();         //从子项视图返回时，应当刷新数据
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_message_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_message:
                createAlertDialog().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSubtitle(){
        MessageLab messageLab=MessageLab.get(getActivity());
        int messageCount=messageLab.getUserMessages().size();
        String subtitle="总共："+messageCount+"个";

        AppCompatActivity activity= (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //用于在数据有变动时，刷新数据
    private void updateUI(){
        MessageLab messageLab=MessageLab.get(getActivity());
        List<UserMessage> messages=messageLab.getUserMessages();

        if (mAdapter==null){
            mAdapter=new MessageAdapter(messages);
            mMessageRecyclerView.setAdapter(mAdapter);      //为列表设置适配器
        }else{
            mAdapter.setMessages(messages);
            mAdapter.notifyDataSetChanged();
        }

        showSubtitle();
    }

    /**
     * 内部类，用于处理每个item的显示，以及子项的点击事件
     */
    private class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private UserMessage mUserMessage;

        private TextView mPingTaiTextView;
        private TextView mUserNameTextView;
        private ImageView mMessageType;

        public MessageHolder(View itemView) {
            super(itemView);
            mPingTaiTextView=itemView.findViewById(R.id.message_pingtai);
            mUserNameTextView=itemView.findViewById(R.id.message_user_name);
            mMessageType=itemView.findViewById(R.id.message_image_view);

            itemView.setOnClickListener(this);
        }

        public void bindMessage(UserMessage message){
            mUserMessage=message;
            mPingTaiTextView.setText(mUserMessage.getPingtai());
            mUserNameTextView.setText(mUserMessage.getUserName());
            if (message.getType()==UserMessage.TYPE_OF_EMAIL){      //根据类型不同，设置图片
                mMessageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_dialog_email,null));
                mMessageType.setBackground(getResources().getDrawable(R.drawable.ic_dialog_email_background,null));
            }else if (message.getType()==UserMessage.TYPE_OF_PHONE){
                mMessageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_dialog_phone,null));
                mMessageType.setBackground(getResources().getDrawable(R.drawable.ic_launcher_background,null));
            }else{
                mMessageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_dialog_usually,null));
                mMessageType.setBackground(getResources().getDrawable(R.drawable.ic_dialog_usually_background,null));
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent=MessageActivity.newIntent(getActivity(),mUserMessage.getId(),false,mUserMessage.getType());
            startActivity(intent);
        }
    }
    //当为空时，显示的图像
    private class MessageEmptyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public MessageEmptyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            createAlertDialog().show();
        }
    }

    /**
     * 内部类，用于子项视图
     */
    private class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<UserMessage> mMessages;

        private int VIEW_TYPE_NULL=-1;      //列表为空时，列表显示类型

        public MessageAdapter(List<UserMessage> messages){
            mMessages=messages;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            if (viewType==VIEW_TYPE_NULL){
                View view=inflater.inflate(R.layout.list_item_message_empty,parent,false);
                return new MessageEmptyHolder(view);
            }
            View view=inflater.inflate(R.layout.list_item_message,parent,false);
            return new MessageHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MessageHolder) {
                UserMessage message=mMessages.get(position);
                ((MessageHolder) holder).bindMessage(message);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mMessages.size()<=0){
                return VIEW_TYPE_NULL;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mMessages.size()>0 ? mMessages.size():1;
        }

        public void setMessages(List<UserMessage> messages){
            mMessages=messages;
        }
    }

    private AlertDialog createAlertDialog(){
        final UserMessage message=new UserMessage();
        DialogInterface.OnKeyListener keyListener=new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i==KeyEvent.KEYCODE_BACK&&keyEvent.getRepeatCount()==0){
                    return true;
                }
                return false;
            }
        };
        AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setTitle("选择三种存储方式")
                .setView(R.layout.dialog_view)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setOnKeyListener(keyListener).setCancelable(false)
                .setNegativeButton("手机及账号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setMessageType(message,UserMessage.TYPE_OF_PHONE);
                        Log.d("AlertDialog","type:"+message.getType());
                    }
                })
                .setNeutralButton("账号自定义", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setMessageType(message,UserMessage.TYPE_OF_USUALLY);
                    }
                })
                .setPositiveButton("邮箱及账号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setMessageType(message,UserMessage.TYPE_OF_EMAIL);
                    }
                })
                .create();

        return dialog;
    }
    //设置message的type
    private void setMessageType(UserMessage message,int type){
        message.setType(type);
        MessageLab.get(getActivity()).addMessage(message);
        Intent intent=MessageActivity.newIntent(getActivity(),message.getId(),true,type);
        startActivity(intent);
    }
}
