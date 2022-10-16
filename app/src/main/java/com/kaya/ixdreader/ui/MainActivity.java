package com.kaya.ixdreader.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kaya.ixdreader.R;
import com.kaya.ixdreader.adapter.BookListAdapter;
import com.kaya.ixdreader.model.OneItemBook;
import com.kaya.ixdreader.utils.FileUtils;
import com.kaya.ixdreader.utils.ListDataSaveUtil;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView booklistRecycleView;
    private BookListAdapter bookListAdapter;
    private List<OneItemBook> databooklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                        } else {
                            Toast.makeText(MainActivity.this, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        setSupportActionBar(myToolbar);
        booklistRecycleView = findViewById(R.id.book_list_recyclerview);
        databooklist = new ArrayList<>();
        if(ListDataSaveUtil.getInstance().getDataList(OneItemBook.BOOK_TAG).size() == 0){
            databooklist.add(new OneItemBook(OneItemBook.ADD_TYPE,"","",""));
        }
        {
            databooklist.addAll(ListDataSaveUtil.getInstance().getDataList(OneItemBook.BOOK_TAG));
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        booklistRecycleView.setLayoutManager(layoutManager);
        bookListAdapter = new BookListAdapter(databooklist,this);
        booklistRecycleView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        booklistRecycleView.setAdapter(bookListAdapter);
        //添加Android自带的分割线
        booklistRecycleView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        bookListAdapter.setOnItemClickListener(new BookListAdapter.OnAddItemClickListener() {

            @Override
            public void DeleteBook(int position) {
                ListDataSaveUtil.getInstance().deleteSaveContentHistory(databooklist.get(position).getBookurl());
                databooklist.remove(position);
                ListDataSaveUtil.getInstance().setDataList(OneItemBook.BOOK_TAG,databooklist);
                bookListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mode_talk:
                ListDataSaveUtil.getInstance().SetReadMode(ListDataSaveUtil.TALK_MODE);
                break;
            case R.id.mode_read:
                ListDataSaveUtil.getInstance().SetReadMode(ListDataSaveUtil.READ_MODE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookListAdapter.notifyDataSetChanged();
    }

    public void updateDataList(String openUrl){
        OneItemBook temp;
        for(OneItemBook mbookshelf_item:databooklist){
            if(mbookshelf_item.getBookurl().equals(openUrl)){
                temp = mbookshelf_item;
                databooklist.remove(mbookshelf_item);
                databooklist.add(1,temp);
                break;
            }
        }
        ListDataSaveUtil.getInstance().setDataList(OneItemBook.BOOK_TAG,databooklist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            int flag = 0;
            for(OneItemBook mbookshelf_item:databooklist){
                if(mbookshelf_item.getBookurl().equals(filePath)){
                    flag = 1;
                }
            }
            if(flag == 0){
                databooklist.add(new OneItemBook(OneItemBook.SHOW_TYEP, FileUtils.getFileNameNotType(filePath),filePath,""+System.currentTimeMillis()));
                ListDataSaveUtil.getInstance().setDataList(OneItemBook.BOOK_TAG,databooklist);
                bookListAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(this,"重复添加书籍",Toast.LENGTH_LONG).show();
            }
        }
    }
}