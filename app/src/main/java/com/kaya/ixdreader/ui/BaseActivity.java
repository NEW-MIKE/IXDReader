package com.kaya.ixdreader.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kaya.ixdreader.IXDRApplication;
import com.kaya.ixdreader.utils.DisplayUtil;

public class BaseActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtil.setCustomDensity(this, IXDRApplication.getInstance());
    }
}
