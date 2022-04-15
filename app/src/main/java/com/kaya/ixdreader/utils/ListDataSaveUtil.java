package com.kaya.ixdreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaya.ixdreader.model.bookshelf_item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListDataSaveUtil {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static ListDataSaveUtil listDataSaveUtil;

    public static int READ_MODE = 1;
    public static int TALK_MODE = 0;
    public static String SP_KEY_SET_MODE_READ = "ID_SET_MODE_READ";
    public static String SP_KEY_SET_PROGRESS_READ = "ID_SET_PROGRESS_READ";

    public static void init(Context mContext){
        preferences = mContext.getSharedPreferences(bookshelf_item.BOOK_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public synchronized static ListDataSaveUtil getInstance() {
        if(listDataSaveUtil == null){
            listDataSaveUtil = new ListDataSaveUtil();
        }
        return listDataSaveUtil;
    }
    public void toSaveContentHistory(String key,SaveContentHistory saveContentHistory) throws Exception {
        if(saveContentHistory instanceof Serializable) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(saveContentHistory);//把对象写到流里
                String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                editor.putString(key, temp);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new Exception("User must implements Serializable");
        }
    }
    public void deleteSaveContentHistory(String key)  {
        editor.remove(key);
        editor.commit();
    }
    public SaveContentHistory toGetContentHistory(String key) {
        String temp = preferences.getString(key, "");
        if(temp == "") {
            return null;
        }
        ByteArrayInputStream bais =  new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        SaveContentHistory saveContentHistory = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            saveContentHistory = (SaveContentHistory) ois.readObject();
        } catch (IOException e) {
        }catch(ClassNotFoundException e1) {

        }
        return saveContentHistory;
    }
    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            return;
        }

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist=new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<bookshelf_item>>() {
        }.getType());
        return datalist;

    }
    public void SetReadMode(int mode){
        editor.putInt(SP_KEY_SET_MODE_READ,mode).commit();
    }
    public int GetReadMode(){
        return preferences.getInt(SP_KEY_SET_MODE_READ,TALK_MODE);
    }
}