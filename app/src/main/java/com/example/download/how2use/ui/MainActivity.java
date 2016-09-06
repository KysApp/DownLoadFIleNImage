package com.example.download.how2use.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.download.R;
import com.example.download.how2use.adpter.MainAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> testList = new ArrayList<>();
    private ListView lv_main;
    private MainAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);
        initData();
        initView();
    }
    private void initView(){
        mainAdapter = new MainAdapter(this,testList);
        lv_main = (ListView)findViewById(R.id.lv_main);
        lv_main.setAdapter(mainAdapter);
    }
    private void initData(){
        testList.add(getResources().getString(R.string.downloadApp_API9Upper));
        testList.add(getResources().getString(R.string.downloadApp));
        testList.add(getResources().getString(R.string.downloadImage));
        testList.add(getResources().getString(R.string.fileOperation));
    }
}
