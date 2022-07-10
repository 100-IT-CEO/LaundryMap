package com.kakaologin_sample;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.useage_record);
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

