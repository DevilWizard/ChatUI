package com.example.chatui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<Msg> msgList = new ArrayList<>();
    private final List<Msg> cacheMsgList = new ArrayList<>();
    private EditText inputText;
    private Button sendButton;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private MydataBase mydataBase;
    private SQLiteDatabase db;
    private final boolean chatChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initMsg();
        mydataBase = new MydataBase(this, "ChatUI.db", null, 1);
        this.db = mydataBase.getWritableDatabase();
        inputText = (EditText) findViewById(R.id.input_text);
        sendButton = (Button) findViewById(R.id.send_button);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgList = readCache();
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        netWorkChangeReciever = new NetWorkChangeReciever();
//        registerReceiver(netWorkChangeReciever, intentFilter);
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    inputText.setHint(null);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!Objects.equals("", content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    cacheMsgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");

                }
                for (Msg x : msgList) {
                    System.out.println(x.getContent());
                }

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cacheToStorage();
    }

    private void initMsg() {
        Msg msg1 = new Msg("渣男狗都不吃", Msg.TYPE_RECIEVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("@只信他公 @只信他爹 ", Msg.TYPE_RECIEVED);
        msgList.add(msg2);
        Msg msg3 = new Msg("狗才不吃", Msg.TYPE_RECIEVED);
        msgList.add(msg3);
        Msg msg4= new Msg("sbg ljg", Msg.TYPE_SENT);
        msgList.add(msg4);
    }



    private void cacheToStorage() {
        for(Msg x: this.cacheMsgList) {
            String content = x.getContent();
            int contentType = x.getType();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MydataBase.getColumnChatContent(), content);
            contentValues.put(MydataBase.getColumnChatType(), contentType);
            this.db.insert(MydataBase.getTableName(), null, contentValues);
            contentValues.clear();
        }
    }

    private List<Msg> readCache() {

        List<Msg> list = new ArrayList<>();
        Cursor cursor = this.db.query(MydataBase.getTableName(), null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex(MydataBase.getColumnChatContent()));
                int contentType = cursor.getInt(cursor.getColumnIndex(MydataBase.getColumnChatType()));
                Msg msg = new Msg(content, contentType);
                list.add(msg);
            }while(cursor.moveToNext());
        }

        return list;
    }

    class NetWorkChangeReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "检测到网络状态变化", Toast.LENGTH_SHORT).show();

        }
    }



}
