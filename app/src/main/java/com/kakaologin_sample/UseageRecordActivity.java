package com.kakaologin_sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class UseageRecordActivity extends AppCompatActivity {
    private UseagerecordAdapter useagerecordAdapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<UseageRecord> arrayList;
    private UseageRecord record1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_list);
        recyclerView = findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        record1 = new UseageRecord("2020","2020","2020","2020");
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList= new ArrayList<>();
        arrayList.add(record1);
        useagerecordAdapter = new UseagerecordAdapter(arrayList);
        recyclerView.setAdapter(useagerecordAdapter);

    }
}

