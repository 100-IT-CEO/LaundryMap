package com.kakaologin_sample;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReserveActivity extends AppCompatActivity{

    private static final String HOST = "143.248.199.127";
    private static final String PORT = "80";

    private int washteria_id;
    private String washteria_name;
    private int machine_id;
    private String kakao_id;
    private String reserve_start_time;

    private int washer_big_num;
    private int washer_num;
    private int dryer_big_num;
    private int dryer_num;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReserveActivity.this, MapFragmentActivity.class);
        intent.putExtra("kakao_id", (getIntent().getLongExtra("kakao_id", 0L)));
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        washteria_id = intent.getIntExtra("washteria_id", 0);
        washteria_name = intent.getStringExtra("washteria_name");
        kakao_id = String.valueOf(intent.getLongExtra("kakao_id",0L));

        setContentView(R.layout.reserve_activity);


        CardView button1 = findViewById(R.id.reserve_button_1);
        CardView button2 = findViewById(R.id.reserve_button_2);
        CardView button3 = findViewById(R.id.reserve_button_3);
        CardView button4 = findViewById(R.id.reserve_button_4);
        CardView button5 = findViewById(R.id.reserve_button_5);

        String url = "http://"+HOST+":"+PORT+"/washteria_machines?id="+washteria_id;

        RequestQueue requestQueue = Volley.newRequestQueue(ReserveActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener(){
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray machines = jsonObject.getJSONArray("result");

                    Log.d("asdf", machines.toString());

                    LinearLayout layout_expand1 = findViewById(R.id.layout_expand_1);
                    LinearLayout layout_expand2 = findViewById(R.id.layout_expand_2);
                    LinearLayout layout_expand3 = findViewById(R.id.layout_expand_3);
                    LinearLayout layout_expand4 = findViewById(R.id.layout_expand_4);
                    LinearLayout layout_expand5 = findViewById(R.id.layout_expand_5);

                    TextView big_washer_num = findViewById(R.id.big_washer_num);
                    TextView washer_num = findViewById(R.id.washer_num);
                    TextView big_dryer_num = findViewById(R.id.big_dryer_num);
                    TextView dryer_num = findViewById(R.id.dryer_num);
                    TextView etc_num = findViewById(R.id.etc_num);

                    TextView reserve_text_where = findViewById(R.id.reserve_text_where);
                    reserve_text_where.setText(washteria_name);

                    int[] count= {0, 0, 0, 0, 0};

                    for(int i=0; i<machines.length(); i++){
                        JSONObject machine = machines.getJSONObject(i);
                        int status = machine.getInt("status");
                        String machine_type = machine.getString("machine_type");

                        if(status == 0){
                            switch(machine_type){
                                case "big_washer" : count[0] += 1; break;
                                case "washer" : count[1] += 1; break;
                                case "big_dryer" : count[2] += 1; break;
                                case "dryer" : count[3] += 1; break;
                                default : count[4] += 1; break;
                            }
                        }

                        switch(machine_type){
                            case "big_washer" : create(layout_expand1, machine, status); break;
                            case "washer" : create(layout_expand2, machine, status); break;
                            case "big_dryer" : create(layout_expand3, machine, status); break;
                            case "dryer" : create(layout_expand4, machine, status); break;
                            default : create(layout_expand5, machine, status);
                        }
                    }

                    big_washer_num.setText(String.valueOf(count[0]));
                    washer_num.setText(String.valueOf(count[1]));
                    big_dryer_num.setText(String.valueOf(count[2]));
                    dryer_num.setText(String.valueOf(count[3]));
                    etc_num.setText(String.valueOf(count[4]));


                    if(count[0] == 0) button1.setCardBackgroundColor(ContextCompat.getColor(ReserveActivity.this, R.color.gray));
                    if(count[1] == 0) button2.setCardBackgroundColor(ContextCompat.getColor(ReserveActivity.this, R.color.gray));
                    if(count[2] == 0) button3.setCardBackgroundColor(ContextCompat.getColor(ReserveActivity.this, R.color.gray));
                    if(count[3] == 0) button4.setCardBackgroundColor(ContextCompat.getColor(ReserveActivity.this, R.color.gray));
                    if(count[4] == 0) button5.setCardBackgroundColor(ContextCompat.getColor(ReserveActivity.this, R.color.gray));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("asdf","에러: " + error.toString());
            }
        });

        requestQueue.add(stringRequest);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout_expand = findViewById(R.id.layout_expand_1);

                if(layout_expand.getVisibility() == View.VISIBLE){
                    layout_expand.setVisibility(View.GONE);
                }
                else if(layout_expand.getVisibility() == View.GONE){
                    layout_expand.setVisibility(View.VISIBLE);
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout_expand = findViewById(R.id.layout_expand_2);

                if(layout_expand.getVisibility() == View.VISIBLE){
                    layout_expand.setVisibility(View.GONE);
                }
                else if(layout_expand.getVisibility() == View.GONE){
                    layout_expand.setVisibility(View.VISIBLE);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout_expand = findViewById(R.id.layout_expand_3);

                if(layout_expand.getVisibility() == View.VISIBLE){
                    layout_expand.setVisibility(View.GONE);
                }
                else if(layout_expand.getVisibility() == View.GONE){
                    layout_expand.setVisibility(View.VISIBLE);
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout_expand = findViewById(R.id.layout_expand_4);

                if(layout_expand.getVisibility() == View.VISIBLE){
                    layout_expand.setVisibility(View.GONE);
                }
                else if(layout_expand.getVisibility() == View.GONE){
                    layout_expand.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void create(LinearLayout linearLayout, JSONObject machine, int status){
        Button button = new Button(ReserveActivity.this);
        try{
            button.setBackground(getDrawable(R.drawable.white_btn));

            switch(machine.getString("machine_type")){

                case "big_washer" : button.setText("대형 세탁기 #" + String.valueOf(machine.getInt("machine_id"))); break;
                case "washer" : button.setText("중형 세탁기 #" + String.valueOf(machine.getInt("machine_id"))); break;
                case "big_dryer" : button.setText("대형 건조기 #" + String.valueOf(machine.getInt("machine_id"))); break;
                case "dryer" : button.setText("중형 건조기 #" + String.valueOf(machine.getInt("machine_id"))); break;
                default : button.setText(machine.getString("machine_type")+ " #" + String.valueOf(machine.getInt("machine_id"))); break;
            }
            button.setTag(machine.getInt("machine_id"));

            if(status!=0){

            }else{
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            machine_id = machine.getInt("machine_id");
                            reserve_start_time = getTime();
//                            Log.d("Button,  machine id ", String.valueOf(machine.getInt("machine_id")));
//                            Log.d("Button,kakao id ", String.valueOf(kakao_id));
//                            Log.d("Button start ",getTime());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //machine_id, kakao_id, reserve_start_time, reservaton_status =1 로 바꾸기  //에러처리 하면 좋음
                        String url = "http://"+HOST+":"+PORT+"/create_reservation?machine_id="+machine_id+"&kakao_id="+kakao_id+"&reserve_start_time="+reserve_start_time+"&reservation_status="+1;
                        RequestQueue requestQueue = Volley.newRequestQueue(ReserveActivity.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener(){
                            @Override
                            public void onResponse(Object response) {
                                Toast.makeText(ReserveActivity.this, "예약이 완료됐습니다. ",Toast.LENGTH_SHORT).show();
                                Log.d("새로고침 안됨 ", "여기");
                                finish();
                                startActivity(getIntent());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("asdf","에러: " + error.toString());
                            }
                        });
                        requestQueue.add(stringRequest);
                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        LinearLayout linearlayout = new LinearLayout(ReserveActivity.this);

        linearlayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.FILL_PARENT
        );

        layoutParams.setMargins(0, 0, 0, 30);

        linearLayout.setBackgroundColor(Color.parseColor("#00000000"));

        linearlayout.addView(button);
        linearLayout.addView(linearlayout, layoutParams);
    }

    private String getTime() {
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
