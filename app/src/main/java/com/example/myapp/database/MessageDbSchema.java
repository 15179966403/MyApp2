package com.example.myapp.database;

/**
 * 数据库操作相关代码的组织和归类
 * Created by 黄荣聪 on 2017/11/24.
 */

public class MessageDbSchema {
    public static final class MessageTable{
        public static final String NAME="usermessages";     //表名

        //列表字段名
        public static final class Cols{
            public static final String UUID="uuid";         //唯一标识id
            public static final String PINGTAI="pingtai";   //平台
            public static final String DATE="date";         //创建时间
            public static final String USERNAME="username"; //用户名
            public static final String USER="user";         //账号
            public static final String PASSWORD="password"; //密码
            public static final String EMAIL="email";       //邮箱
            public static final String PHONE="phone";       //手机
        }
    }
}
