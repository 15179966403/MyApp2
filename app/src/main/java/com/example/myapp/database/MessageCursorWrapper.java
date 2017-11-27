package com.example.myapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.myapp.UserMessage;
import com.example.myapp.database.MessageDbSchema.MessageTable;

import java.util.UUID;

/**
 * 封装数据表中的字段
 * Created by 黄荣聪 on 2017/11/24.
 */

public class MessageCursorWrapper extends CursorWrapper {
    public MessageCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public UserMessage getMessage(){
        String uuidString=getString(getColumnIndex(MessageTable.Cols.UUID));
        String pingtai=getString(getColumnIndex(MessageTable.Cols.PINGTAI));
        String username=getString(getColumnIndex(MessageTable.Cols.USERNAME));
        String user=getString(getColumnIndex(MessageTable.Cols.USER));
        String password=getString(getColumnIndex(MessageTable.Cols.PASSWORD));
        String email=getString(getColumnIndex(MessageTable.Cols.EMAIL));
        String phone=getString(getColumnIndex(MessageTable.Cols.PHONE));
        String date=getString(getColumnIndex(MessageTable.Cols.DATE));
        int type=getInt(getColumnIndex(MessageTable.Cols.TYPE));

        UserMessage message=new UserMessage(UUID.fromString(uuidString));
        message.setPingtai(pingtai);
        message.setUserName(username);
        message.setUser(user);
        message.setPassword(password);
        message.setEmail(email);
        message.setPhone(phone);
        message.setDate(date);
        message.setType(type);

        return message;
    }
}
