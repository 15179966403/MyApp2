package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapp.database.MessageBaseHelper;
import com.example.myapp.database.MessageCursorWrapper;
import com.example.myapp.database.MessageDbSchema;
import com.example.myapp.database.MessageDbSchema.MessageTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 储存信息
 * Created by 黄荣聪 on 2017/11/23.
 */

public class MessageLab {
    private static MessageLab sMessageLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    //单例模式
    public static MessageLab get(Context context){
        if (sMessageLab==null){
            sMessageLab=new MessageLab(context);
        }
        return sMessageLab;
    }

    private MessageLab(Context context){
        mContext=context.getApplicationContext();
        mDatabase=new MessageBaseHelper(mContext).getWritableDatabase();
    }

    public List<UserMessage> getUserMessages() {
        List<UserMessage> messages=new ArrayList<>();

        MessageCursorWrapper cursor=queryMessages(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                messages.add(cursor.getMessage());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return messages;
    }

    public UserMessage getMessage(UUID id){
        MessageCursorWrapper cursor=queryMessages(MessageTable.Cols.UUID+"=?",new String[]{id.toString()});

        try {
            if (cursor.getCount()==0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMessage();
        } finally {
            cursor.close();
        }
    }

    //更新信息
    public void updateMessage(UserMessage userMessage){
        String uuidString=userMessage.getId().toString();
        ContentValues values=getContentValues(userMessage);

        mDatabase.update(MessageTable.NAME,values,MessageTable.Cols.UUID+"=?",new String[]{uuidString});
    }

    //插入信息
    public void addMessage(UserMessage message){
        ContentValues values=getContentValues(message);

        mDatabase.insert(MessageTable.NAME,null,values);
    }

    //获得 操作辅助类ContentValues
    private static ContentValues getContentValues(UserMessage userMessage){
        ContentValues values=new ContentValues();
        values.put(MessageTable.Cols.UUID,userMessage.getId().toString());
        values.put(MessageTable.Cols.PINGTAI,userMessage.getPingtai());
        values.put(MessageTable.Cols.USERNAME,userMessage.getUserName());
        values.put(MessageTable.Cols.USER,userMessage.getUser());
        values.put(MessageTable.Cols.PASSWORD,userMessage.getPassword());
        values.put(MessageTable.Cols.EMAIL,userMessage.getEmail());
        values.put(MessageTable.Cols.PHONE,userMessage.getPhone());
        values.put(MessageTable.Cols.DATE,userMessage.getDate());

        return values;
    }

    /**
     * 查询信息
     * @param whereClause 查询的具体列
     * @param whereArgs     列的具体值
     * @return  自定义的封装操作类
     */
    private MessageCursorWrapper queryMessages(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(MessageTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new MessageCursorWrapper(cursor);
    }

    public void deleteMessage(UserMessage message){
        String uuidString=message.getId().toString();
        mDatabase.delete(MessageTable.NAME,MessageTable.Cols.UUID+"=?",new String[]{uuidString});
    }
}
