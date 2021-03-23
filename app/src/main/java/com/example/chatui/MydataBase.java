package com.example.chatui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MydataBase extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "messageCache";
    private static final String COLUMN_CHAT_CONTENT = "chat_content";
    private static final String COLUMN_CHAT_TYPE = "chat_type";
    private static final String CREATE_TABLE_MESSAGE_CACHE = "CREATE TABLE "+TABLE_NAME+"("
            + "id integer  primary key autoincrement,"
            + "chat_content text,"
            + "chat_type integer)";

    public MydataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CREATE_TABLE_MESSAGE_CACHE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnChatContent() {
        return COLUMN_CHAT_CONTENT;
    }

    public static String getColumnChatType() {
        return COLUMN_CHAT_TYPE;
    }
}
