package com.kaya.ixdreader;

import android.app.Application;

import com.kaya.ixdreader.utils.ListDataSaveUtil;

public class IXDRApplication extends Application {

    private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ListDataSaveUtil.init(this);
    }

    public synchronized static Application getInstance(){
        if(instance == null){
            instance = new IXDRApplication();
        }
        return instance;
    }
}
