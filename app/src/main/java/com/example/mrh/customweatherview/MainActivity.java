package com.example.mrh.customweatherview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.example.mrh.customweatherview.custom.CustomDrawView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CustomDrawView mCustomView;
    private RelativeLayout mActivityMain;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ArrayList<Integer> integers = new ArrayList<>();
        int v;
        for (int i = 0; i < 12; i++){
            v = (int) (Math.random() * 10 + 30f);
            integers.add(v);
        }
        mCustomView.setData(integers);
    }

    private void initView () {
        mCustomView = (CustomDrawView) findViewById(R.id.custom_view);
        mActivityMain = (RelativeLayout) findViewById(R.id.activity_main);
    }
}
