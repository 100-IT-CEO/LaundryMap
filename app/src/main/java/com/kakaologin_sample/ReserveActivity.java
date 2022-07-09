package com.kakaologin_sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReserveActivity extends AppCompatActivity{

    private static final String HOST = "143.248.199.22";
    private static final String PORT = "80";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        

        setContentView(R.layout.reserve_activity);

        Button button1 = findViewById(R.id.reserve_button_1);
        Button button2 = findViewById(R.id.reserve_button_2);
        Button button3 = findViewById(R.id.reserve_button_3);
        Button button4 = findViewById(R.id.reserve_button_4);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout_expand = findViewById(R.id.layout_expand_1);

                // get DB



                create(layout_expand);

                if(layout_expand.getVisibility() == View.VISIBLE){
                    layout_expand.setVisibility(View.GONE);
                    layout_expand.animate().setDuration(200).alpha(0F);
                }
                else if(layout_expand.getVisibility() == View.GONE){
                    layout_expand.setVisibility(View.VISIBLE);
                    layout_expand.animate().setDuration(200).alpha(0F);
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void getReserveStatusByWasherID(int washer_id) throws JSONException {

        JSONObject parameters = new JSONObject();
        parameters.put("washer_id", String.valueOf(washer_id));

//        requester.get("/reserve_status", );

    }


    public void create(LinearLayout linearLayout){
        Button button = new Button(this);
        button.setText("기호 1번");
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        linearLayout.addView(button);
    }

}
