package com.kakaologin_sample;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.kakao.sdk.user.UserApiClient;
import com.kakaologin_sample.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;


public class MapFragmentActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private JSONArray washterias;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private ArrayList<Marker> markers;
    private static final String HOST = "143.248.199.22";
    private static final String PORT = "80";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_fragment_activity);

        Intent intent = getIntent();

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.naverMap);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.naverMap, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground); //왼쪽 상단 버튼 아이콘 지정

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

//header change
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        View headerview = navigationView.getHeaderView(0);
        TextView reserve_washteria_name= (TextView)headerview.findViewById(R.id.reserve_washteria_name);
        TextView reserve_washer_type = (TextView)headerview.findViewById(R.id.reserve_washer_type);
        TextView reserve_start_time = (TextView)headerview.findViewById(R.id.reserve_start_time);
        Button reserve_cancel_btn = (Button)headerview.findViewById(R.id.reserve_cancel_btn);

        RequestQueue requestQueue = Volley.newRequestQueue(MapFragmentActivity.this);
        //여기 카카오 아이디로..
// String uri2 = String.format("http://"+HOST+"/load_reservation/"+String.valueOf(kakao_id));
        String uri2 = String.format("http://"+HOST+"/load_reservation/123");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri2, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("response",jsonObject.toString());
                    String name = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("name"));
                    String machine_type = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("machine_type"));
                    String start_time = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("reserve_start_time"));
                    start_time=start_time.substring(10,16);
                    switch(machine_type) {
                        case "bid_dryer":
                            machine_type = "대형 건조기";
                            break;
                        case "dryer":
                            machine_type ="중형 건조기";
                            break;
                        case "big_washer" :
                            machine_type ="대형 세탁기";
                            break;
                        case "washer":
                            machine_type ="중형 세탁기";
                            break;
                    }
                    reserve_washteria_name.setText(name);
                    reserve_washer_type.setText(machine_type);
                    reserve_start_time.setText(start_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("asdf", "에러: " + error.toString());
            }
        });
        requestQueue.add(stringRequest);

        reserve_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String uri2 = String.format("http://"+HOST+"/reservation/cancel/?id="+kakao_id);
                String uri2 = String.format("http://"+HOST+"/reservation/cancel/?id=123");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri2, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.d("response",jsonObject.toString());
                            String name = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("name"));
                            String machine_type = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("machine_type"));
                            String start_time = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("reserve_start_time"));
                            start_time=start_time.substring(10,16);
                            switch(machine_type) {
                                case "bid_dryer":
                                    machine_type = "대형 건조기";
                                    break;
                                case "dryer":
                                    machine_type ="중형 건조기";
                                    break;
                                case "big_washer" :
                                    machine_type ="대형 세탁기";
                                    break;
                                case "washer":
                                    machine_type ="중형 세탁기";
                                    break;
                            }
                            reserve_washteria_name.setText(name);
                            reserve_washer_type.setText(machine_type);
                            reserve_start_time.setText(start_time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("asdf", "에러: " + error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            }});

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //뒤로가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.getUiSettings().setLocationButtonEnabled(true);

        try {
            load_markers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void load_markers() throws JSONException {

        String url = "http://" + HOST + ":" + PORT + "/washteria_location";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.d("asdf", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    markers = new ArrayList<Marker>();
                    washterias = jsonObject.getJSONArray("result");
                    Log.d("asdf", washterias.toString());

                    for (int i = 0; i < washterias.length(); i++) {
                        JSONObject washteria = washterias.getJSONObject(i);

                        int washteria_id = washteria.getInt("washteria_id");
                        String washteria_name = washteria.getString("name");
                        int washer_big_num = washteria.getInt("washer_big_num");
                        int washer_medium_num = washteria.getInt("washer_medium_num");
                        int dryer_big_num  = washteria.getInt("dryer_big_num");
                        int dryer_medium_num  = washteria.getInt("dryer_medium_num");

                        Marker marker = new Marker();
                        marker.setPosition(new LatLng(washteria.getDouble("locationX"), washteria.getDouble("locationY")));
                        marker.setCaptionText(washteria_name);
                        marker.setCaptionRequestedWidth(200);
                        marker.setTag(washteria_id);

                        Log.d("asdf", marker.getTag() + " : " + marker.getPosition().toString());
                        marker.setMap(naverMap);

                        marker.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                Dialog mDialog = new Dialog(MapFragmentActivity.this);
                                mDialog.setContentView(R.layout.map_popup_dialog);


                                String url = "http://"+HOST+":"+PORT+"/washteria_machines/"+washteria_id;

                                RequestQueue requestQueue = Volley.newRequestQueue(MapFragmentActivity.this);

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener(){
                                    @Override
                                    public void onResponse(Object response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.toString());

                                            JSONArray machines = jsonObject.getJSONArray("result");

                                            int washer_num = 0;
                                            int dryer_num = 0;
                                            int etc_num = 0;

                                            for(int i=0; i<machines.length(); i++){
                                                JSONObject machine = machines.getJSONObject(i);
                                                int status = machine.getInt("status");
                                                String machine_type = machine.getString("machine_type");

                                                if(status == 0){
                                                    switch(machine_type){
                                                        case "big_washer" : washer_num += 1; break;
                                                        case "washer" : washer_num += 1; break;
                                                        case "big_dryer" : dryer_num += 1; break;
                                                        case "dryer" : dryer_num += 1; break;
                                                        default : etc_num += 1; break;
                                                    }
                                                }
                                                TextView washer_num_tv = mDialog.findViewById(R.id.washer_num);
                                                washer_num_tv.setText("세탁기 : " + washer_num + "대");
                                                TextView dryer_num_tv = mDialog.findViewById(R.id.dryer_num);
                                                dryer_num_tv.setText("건조기 : " + dryer_num + "대");
                                                TextView etc_num_tv = mDialog.findViewById(R.id.etc_num);
                                                etc_num_tv.setText("기타 : " + etc_num + "대");
                                            }



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



                                TextView washteria_name_tv = mDialog.findViewById(R.id.washteria_name);
                                washteria_name_tv.setText(marker.getCaptionText());

                                TextView washer_num = mDialog.findViewById(R.id.washer_num);
//                                washer_num.setText()
                                TextView dryer_num = mDialog.findViewById(R.id.dryer_num);
//                                dryer_num.setText()
                                TextView etc_num = mDialog.findViewById(R.id.etc_num);
//                                etc_num.setText()

                                Button open_reservation = mDialog.findViewById(R.id.open_reservation);
                                open_reservation.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(MapFragmentActivity.this, ReserveActivity.class);
                                        intent.putExtra("washteria_id", washteria_id);
                                        intent.putExtra("washteria_name", washteria_name);
                                        intent.putExtra("washer_big_num", washer_big_num);
                                        intent.putExtra("washer_num", washer_medium_num);
                                        intent.putExtra("dryer_big_num", dryer_big_num);
                                        intent.putExtra("dryer_num", dryer_medium_num);
                                        startActivity(intent);
                                    }
                                });

                                mDialog.getWindow().setDimAmount(0);
                                mDialog.getWindow().setGravity(Gravity.BOTTOM);
                                mDialog.show();

                                return false;
                            }
                        });
                        markers.add(marker);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러
                Log.d("asdf", "에러: " + error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


}