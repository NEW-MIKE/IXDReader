package com.kaya.ixdreader.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import com.kaya.ixdreader.databinding.ActivityReadBinding;
import com.kaya.ixdreader.utils.ContentWriteUtil;
import com.kaya.ixdreader.utils.DownReader;

import java.util.List;

public class ReadActivity extends BaseActivity {

    private String input ;
    private String bookid;
    private String bookName;
    private DownReader mdownReader;
    private List<String> readContentList;
    private ActivityReadBinding binding;

    public static void actionStart(Context context, String data1, String bookid,String bookName) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("bookid", bookid);
        intent.putExtra("bookname", bookName);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent getIntent = getIntent();
        input = getIntent.getStringExtra("param1");
        bookid = getIntent.getStringExtra("bookid");
        bookName = getIntent.getStringExtra("bookname");
        binding.readone.setMovementMethod(ScrollingMovementMethod.getInstance());
        binding.readtwo.setMovementMethod(ScrollingMovementMethod.getInstance());
        binding.readthree.setMovementMethod(ScrollingMovementMethod.getInstance());
        mdownReader = new DownReader(input);
        readContentList = mdownReader.getContentList();
        binding.readone.setText(readContentList.get(0));
        binding.readtwo.setText(readContentList.get(1));
        binding.readthree.setText(readContentList.get(2));
        binding.sumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.readtwo.getText().toString();
                ContentWriteUtil.writeContentToFile(content,bookName+"Reader.txt");
                readContentList = mdownReader.getContentList();
                binding.readone.setText(readContentList.get(0));
                binding.readtwo.setText(readContentList.get(1));
                binding.readthree.setText(readContentList.get(2));
                binding.readone.moveCursorToVisibleOffset();
                binding.readtwo.moveCursorToVisibleOffset();
                binding.readthree.moveCursorToVisibleOffset();
                try {
                    mdownReader.saveReadHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.readone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readContentList = mdownReader.getContentList();
                binding.readone.setText(readContentList.get(0));
                binding.readtwo.setText(readContentList.get(1));
                binding.readthree.setText(readContentList.get(2));
                binding.readone.moveCursorToVisibleOffset();
                binding.readtwo.moveCursorToVisibleOffset();
                binding.readthree.moveCursorToVisibleOffset();
                try {
                    mdownReader.saveReadHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}