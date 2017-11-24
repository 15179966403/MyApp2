package com.example.myapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapp.database.MessageDbSchema.MessageTable;

/**
 * 数据库的创建和更新
 * Created by 黄荣聪 on 2017/11/24.
 */

public class MessageBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;         //数据库版本
    private static final String DATABASE_NAME="messageBase.db";     //数据库名称

    public MessageBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ MessageTable.NAME+"(" +
                " _id integer primary key autoincrement," +
                MessageTable.Cols.UUID+"," +
                MessageTable.Cols.PINGTAI+"," +
                MessageTable.Cols.USERNAME+"," +
                MessageTable.Cols.USER+"," +
                MessageTable.Cols.PASSWORD+"," +
                MessageTable.Cols.EMAIL+"," +
                MessageTable.Cols.PHONE+"," +
                MessageTable.Cols.DATE+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
