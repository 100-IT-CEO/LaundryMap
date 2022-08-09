package com.kakaologin_sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsageListActivity extends AppCompatActivity {

    private static String kakao_id;
    private static final String HOST = "143.248.199.169";
    private static final String PORT = "80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_list);

        Intent intent = getIntent();
        kakao_id = intent.getStringExtra("kakao_id");

        RequestQueue requestQueue = Volley.newRequestQueue(UsageListActivity.this);
        String url = String.format("http://"+HOST+":"+PORT+"/load_useageList?kakao_id="+kakao_id);
        Log.d("useagelist", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener(){
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray result = jsonObject.getJSONArray("result");

                    ArrayList<UseageRecord> list = new ArrayList<UseageRecord>();

                    int washer_usage = 0;
                    int dryer_usage = 0;
                    int etc_usage = 0;

                    for(int i=0; i<result.length(); i++){
                        JSONObject usage = result.getJSONObject(i);

                        String washteria_name = usage.getString("name");
                        String machine_type = usage.getString("machine_type");
                        String date = usage.getString("date");
                        int operation_time = usage.getInt("operation_time");

                        Log.d("asdf", date);

                        switch(machine_type){
                            case "big_washer" :
                            case "washer" :
                                washer_usage += 1;
                                break;
                            case "big_dryer":
                            case "dryer":
                                dryer_usage += 1;
                                break;
                            default:
                                etc_usage += 1;
                                break;
                        }

                        UseageRecord useageRecord = new UseageRecord(
                                date, washteria_name, machine_type, String.valueOf(operation_time)
                        );

                        list.add(useageRecord);
                    }

                    TextView total_usage_tv = findViewById(R.id.total_usage);
                    TextView washer_usage_tv = findViewById(R.id.washer_usage);
                    TextView dryer_usage_tv = findViewById(R.id.dryer_usage);
                    TextView etc_usage_tv = findViewById(R.id.etc_usage);

                    total_usage_tv.setText(String.valueOf(washer_usage + dryer_usage + etc_usage) + "회");
                    washer_usage_tv.setText(String.valueOf(washer_usage) + "회");
                    dryer_usage_tv.setText(String.valueOf(dryer_usage) + "회");
                    etc_usage_tv.setText(String.valueOf(etc_usage) + "회");

                    RecyclerView recyclerView = findViewById(R.id.rv);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UsageListActivity.this));

                    UseagerecordAdapter adapter = new UseagerecordAdapter(list);
                    recyclerView.setAdapter(adapter);
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
    }
}