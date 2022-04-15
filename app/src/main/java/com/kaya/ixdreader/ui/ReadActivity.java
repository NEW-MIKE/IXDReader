package com.kaya.ixdreader.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kaya.ixdreader.R;
import com.kaya.ixdreader.utils.downReader;

import java.util.List;

public class ReadActivity extends BaseActivity {

    private String input ;
    private String bookid;
    private Button addbtn,sumbtn;
    private TextView oneview,twoview,threeview;
    private downReader mdownReader;
    private List<String> readContentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Intent getIntent = getIntent();
        input = getIntent.getStringExtra("param1");
        bookid = getIntent.getStringExtra("bookid");
        addbtn = findViewById(R.id.add_btn);
        sumbtn = findViewById(R.id.sum_btn);
        oneview = findViewById(R.id.readone);
        twoview = findViewById(R.id.readtwo);
        threeview = findViewById(R.id.readthree);
        oneview.setMovementMethod(ScrollingMovementMethod.getInstance());
        twoview.setMovementMethod(ScrollingMovementMethod.getInstance());
        threeview.setMovementMethod(ScrollingMovementMethod.getInstance());
        mdownReader = new downReader(input);
        readContentList = mdownReader.getContentList();
        oneview.setText(readContentList.get(0));
        twoview.setText(readContentList.get(1));
        threeview.setText(readContentList.get(2));
        threeview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                readContentList = mdownReader.getContentList();
                oneview.setText(readContentList.get(0));
                twoview.setText(readContentList.get(1));
                threeview.setText(readContentList.get(2));
                oneview.moveCursorToVisibleOffset();
                twoview.moveCursorToVisibleOffset();
                threeview.moveCursorToVisibleOffset();
                try {
                    mdownReader.saveReadHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        oneview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readContentList = mdownReader.getContentList();
                oneview.setText(readContentList.get(0));
                twoview.setText(readContentList.get(1));
                threeview.setText(readContentList.get(2));
                oneview.moveCursorToVisibleOffset();
                twoview.moveCursorToVisibleOffset();
                threeview.moveCursorToVisibleOffset();
                try {
                    mdownReader.saveReadHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public static void actionStart(Context context, String data1, String bookid) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("bookid", bookid);
        context.startActivity(intent);
    }
}