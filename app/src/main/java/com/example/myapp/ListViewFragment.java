package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
                UserMessage message=new UserMessage();
                MessageLab.get(getActivity()).addMessage(message);
                Intent intent=MessageActivity.newIntent(getActivity(),message.getId(),true);
                startActivity(intent);
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

        public MessageHolder(View itemView) {
            super(itemView);
            mPingTaiTextView=itemView.findViewById(R.id.message_pingtai);
            mUserNameTextView=itemView.findViewById(R.id.message_user_name);

            itemView.setOnClickListener(this);
        }

        public void bindMessage(UserMessage message){
            mUserMessage=message;
            mPingTaiTextView.setText(mUserMessage.getPingtai());
            mUserNameTextView.setText(mUserMessage.getUserName());
        }

        @Override
        public void onClick(View view) {
            Intent intent=MessageActivity.newIntent(getActivity(),mUserMessage.getId(),false);
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
            UserMessage message=new UserMessage();
            MessageLab.get(getActivity()).addMessage(message);
            Intent intent=MessageActivity.newIntent(getActivity(),message.getId(),true);
            startActivity(intent);
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
}
