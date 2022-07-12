package com.kakaologin_sample;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kakao.sdk.user.UserApiClient;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MapFragmentActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private JSONArray washterias;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private IntentIntegrator qrScan;

    private Timer timer;
    private TimerTask timerTask;
    private Date due;
    private ArrayList<Marker> markers;
    private static final String HOST = "143.248.199.169";
    private static final String PORT = "80";
    private static String profile_image_url;

    //추가
    private static String nickname;
    private static String kakao_id;
    private String due_time_string;
    private TextView tv_name;
    private TextView TEL;
    private ImageView image;
    private Boolean flag=true;
    private Boolean flag2=true;
    private TextView btn_logout;
    private TextView reserve_start_time;

    // Channel에 대한 id 생성
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;

    // Notification에 대한 ID 생성
    private static final int NOTIFICATION_ID = 0;



    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            reserve_start_time.setText(due_time_string);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_fragment_activity);

        //추가 채널 만들기
        createNotificationChannel();

        Intent intent = getIntent();

        profile_image_url = intent.getStringExtra("profile_image");
        nickname=intent.getStringExtra("nickname");
        kakao_id= String.valueOf(intent.getLongExtra("kakao_id",0L));

        Button use_button = findViewById(R.id.use_button);
        use_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                qrScan = new IntentIntegrator(MapFragmentActivity.this);
                qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
                qrScan.setPrompt("Sample Text!");
                qrScan.initiateScan();
            }
        });

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.app_logo); //왼쪽 상단 버튼 아이콘 지정

//header change
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        View headerview = navigationView.getHeaderView(0);

        ImageView imageView = (ImageView)headerview.findViewById(R.id.iv_image);
        Glide.with(MapFragmentActivity.this).load(profile_image_url).into(imageView);
        TextView tv_name = headerview.findViewById(R.id.tv_name);
        tv_name.setText(nickname);

        btn_logout = headerview.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().logout(error -> {
                    if (error != null) {
                        Log.e("tag", "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                    } else {
                        Log.e("tag", "로그아웃 성공, SDK에서 토큰 삭제됨");
                        Intent goto_login_intent = new Intent(v.getContext(), LoginActivity.class);
                        startActivity(goto_login_intent);
                    }
                    return null;
                });
            }
        });

        //예약기록 header요소들 모음집
        Button reserve_cancel_btn = (Button)headerview.findViewById(R.id.reserve_cancel_btn);
        TextView reserve_washteria_name= (TextView)headerview.findViewById(R.id.reserve_washteria_name);
        TextView reserve_washer_type = (TextView)headerview.findViewById(R.id.reserve_washer_type);
        reserve_start_time = (TextView)headerview.findViewById(R.id.reserve_start_time);
        TextView reserve_open_reservation = (TextView)headerview.findViewById(R.id.navigation_bar_open_reservation);
        LinearLayout layout_reservation = (LinearLayout)headerview.findViewById(R.id.layout_reservation);
        LinearLayout layout_goto_reservation = (LinearLayout)headerview.findViewById(R.id.layout_goto_reservation);
        layout_goto_reservation.setVisibility(View.GONE);
        layout_reservation.setVisibility(View.GONE);

        TextView no_reservation_date = (TextView)headerview.findViewById(R.id.no_reservation_date);
        Button go_to_reserveCreateion = (Button)headerview.findViewById(R.id.go_to_reserveCreateion);


        //이용중 headeer요소들 모음집
        TextView navigation_bar_open_useList = (TextView)headerview.findViewById(R.id.navigation_bar_open_useList);
        TextView use_washteria_name = (TextView)headerview.findViewById(R.id.use_washteria_name);
        TextView use_washer_type = (TextView)headerview.findViewById(R.id.use_washer_type);
        TextView use_start_time = (TextView)headerview.findViewById(R.id.use_start_time);
        LinearLayout layout_useList = (LinearLayout)headerview.findViewById(R.id.layout_useList);
        LinearLayout goto_use = (LinearLayout)headerview.findViewById(R.id.goto_use);
        Button go_to_useListCreateion = (Button)headerview.findViewById(R.id.go_to_useListCreateion);
        //처음 시작은 안보이게
        layout_useList.setVisibility(View.GONE);
        goto_use.setVisibility(View.GONE);
        go_to_useListCreateion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        //마이페이지 버튼
        ImageView gotoMypage_btn = (ImageView)headerview.findViewById(R.id.gotoMypage_btn);
        gotoMypage_btn.setOnClickListener(v-> {
            Intent intent_mypage = new Intent(MapFragmentActivity.this, MyPageActivity.class);
            intent_mypage.putExtra("kakao_id", kakao_id);
            intent_mypage.putExtra("nickname", nickname);
            intent_mypage.putExtra("profile_image_url", profile_image_url);
            Log.d("intent", kakao_id);
            startActivity(intent_mypage);
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MapFragmentActivity.this);
        String uri2 = String.format("http://"+HOST+":"+PORT+"/reservation/recent?kakao_id="+kakao_id);
        String url = String.format("http://"+HOST+":"+PORT+"/load_useageList/recent?kakao_id="+kakao_id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri2, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
//                    Log.d("response",jsonObject.toString());
                    String name = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("name"));
                    if(name == "null"){
                        throw(new NullPointerException());
                    }
                    String machine_type = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("machine_type"));
                    String start_time = String.valueOf(jsonObject.getJSONArray("result").getJSONObject(0).getString("reserve_start_time"));
                    Log.d("response", start_time);

                    timer = new Timer();
                    timerTask = new TimerTask(){
                        @Override
                        public void run() {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                            SimpleDateFormat format1 = new SimpleDateFormat("mm:ss");

                            Date start_date = null;
                            try {
                                start_date = format.parse(start_time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date current_date = new Date(System.currentTimeMillis());

                            Calendar c = Calendar.getInstance();

                            c.setTime(start_date);
                            c.add(Calendar.YEAR, -current_date.getYear());
                            c.add(Calendar.MONTH, -current_date.getMonth());
                            c.add(Calendar.DATE, -current_date.getDate());
                            c.add(Calendar.HOUR, -current_date.getHours());
                            c.add(Calendar.MINUTE, 1-current_date.getMinutes());
                            c.add(Calendar.SECOND, -current_date.getSeconds());

                            Date due = c.getTime();

                            due_time_string = format1.format(due);

                            if(due.getMinutes() == 0 && due.getSeconds() == 0){
                                timer.cancel();
                                sendNotification();
                            }
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                        }
                        @Override
                        public boolean cancel() {
                            Log.v("asdf","timer cancel");
                            return super.cancel();
                        }
                    };

                    timer.schedule(timerTask, 0, 1000);

                    switch(machine_type) {
                        case "big_dryer":
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
                        default :
                            machine_type ="기타 기기";
                            break;
                    }
                    reserve_washteria_name.setText(name);
                    reserve_washer_type.setText(machine_type);
                    flag=true;
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                    flag=false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("asdf", "에러: " + error.toString());
            }
        });
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener(){
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject usage = jsonObject.getJSONArray("result").getJSONObject(0);
                    String washteria_name = usage.getString("name");
                    String machine_type = usage.getString("machine_type");
                    int useage_status = usage.getInt("useage_status");
                    if(useage_status != 1){
                        throw(new NullPointerException());
                    }
                    String date = usage.getString("date");

                    use_washteria_name.setText(washteria_name);

                    switch (machine_type){
                        case "big_washer": use_washer_type.setText("대형 세탁기"); break;
                        case "washer": use_washer_type.setText("중형 세탁기"); break;
                        case "big_dryer": use_washer_type.setText("대형 건조기"); break;
                        case "dryer": use_washer_type.setText("중형 건조기"); break;
                        default : use_washer_type.setText("기타 기기"); break;
                    }

                    use_start_time.setText(date.substring(11, 16).replace('-',':'));

                    flag2=true;

                }
                catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                    flag2=false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("asdf","에러: " + error.toString());
            }
        });
        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest2);




        reserve_open_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout_reservation = findViewById(R.id.layout_reservation);
                LinearLayout layout_goto_reservation = findViewById(R.id.layout_goto_reservation);
                if (flag) {
                    if (layout_reservation.getVisibility()==View.VISIBLE){
                        layout_reservation.setVisibility(View.GONE);
                    }else{
                        layout_reservation.setVisibility(View.VISIBLE);
                    }
                    layout_goto_reservation.setVisibility(View.GONE);
                }
                else{
                    if (layout_goto_reservation.getVisibility()==View.VISIBLE){
                        layout_goto_reservation.setVisibility(View.GONE);
                    }else{
                        layout_goto_reservation.setVisibility(View.VISIBLE);
                    }
                    layout_reservation.setVisibility(View.GONE);
                }

            }
        });

        navigation_bar_open_useList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout_useList = (LinearLayout)headerview.findViewById(R.id.layout_useList);
                LinearLayout goto_use = (LinearLayout)headerview.findViewById(R.id.goto_use);
                if (flag2) {
                    if (layout_useList.getVisibility()==View.VISIBLE){
                        layout_useList.setVisibility(View.GONE);
                    }else{
                        layout_useList.setVisibility(View.VISIBLE);
                    }
                    goto_use.setVisibility(View.GONE);
                }
                else{
                    if (goto_use.getVisibility()==View.VISIBLE){
                        goto_use.setVisibility(View.GONE);
                    }else{
                        goto_use.setVisibility(View.VISIBLE);
                    }
                    layout_useList.setVisibility(View.GONE);
                }

            }
        });

        reserve_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri2 = String.format("http://"+HOST+"/reservation/cancel?id="+kakao_id);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri2, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            layout_reservation.setVisibility(View.GONE);
                            layout_goto_reservation.setVisibility(View.VISIBLE);
                            no_reservation_date.setText(getTime().toString().substring(0, 10));
                            //추가

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


        go_to_reserveCreateion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.END);
//                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnCommunityNotification: { // 왼쪽 상단 버튼 눌렀을 때
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //뒤로가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
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
                //       Log.d("asdf", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    markers = new ArrayList<Marker>();
                    washterias = jsonObject.getJSONArray("result");
                    //Log.d("asdf", washterias.toString());

                    for (int i = 0; i < washterias.length(); i++) {
                        JSONObject washteria = washterias.getJSONObject(i);

                        int washteria_id = washteria.getInt("washteria_id");
                        String washteria_name = washteria.getString("name");
                        int washer_big_num = washteria.getInt("washer_big_num");
                        int washer_medium_num = washteria.getInt("washer_medium_num");
                        int dryer_big_num  = washteria.getInt("dryer_big_num");
                        int dryer_medium_num  = washteria.getInt("dryer_medium_num");

                        Marker marker = new Marker();
                        marker.setPosition(new LatLng(washteria.getDouble("locationY"), washteria.getDouble("locationX")));
                        marker.setCaptionText(washteria_name);
                        marker.setCaptionRequestedWidth(200);
                        marker.setTag(washteria_id);
                        marker.setWidth(120);
                        marker.setHeight(120);
                        marker.setIcon(OverlayImage.fromResource(R.drawable.washer2));

                        // Log.d("asdf", marker.getTag() + " : " + marker.getPosition().toString());
                        marker.setMap(naverMap);

                        marker.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                Dialog mDialog = new Dialog(MapFragmentActivity.this);
                                mDialog.setContentView(R.layout.map_popup_dialog);
                                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                String url = "http://"+HOST+":"+PORT+"/washteria_machines?id="+washteria_id;

                                RequestQueue requestQueue = Volley.newRequestQueue(MapFragmentActivity.this);

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener(){
                                    @Override
                                    public void onResponse(Object response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.toString());

                                            JSONArray machines = jsonObject.getJSONArray("result");

                                            int washer_num_tot = 0;
                                            int washer_num = 0;
                                            int dryer_num_tot = 0;
                                            int dryer_num = 0;
                                            int etc_num_tot = 0;
                                            int etc_num = 0;

                                            //추가
                                            String Tel = washteria.getString("TEL") ;
                                            String washteriaImageurl = washteria.getString("thumUrl");
                                            Log.d("Tel", Tel);
                                            Log.d("Url",washteriaImageurl);
                                            for(int i=0; i<machines.length(); i++){
                                                JSONObject machine = machines.getJSONObject(i);
                                                int status = machine.getInt("status");
                                                String machine_type = machine.getString("machine_type");


                                                switch(machine_type){
                                                    case "big_washer" : {
                                                        if(status==0) washer_num += 1;
                                                        washer_num_tot += 1;
                                                        break;
                                                    }
                                                    case "washer" : {
                                                        if(status==0) washer_num += 1;
                                                        washer_num_tot += 1;
                                                        break;
                                                    }
                                                    case "big_dryer" : {
                                                        dryer_num += 1;
                                                        dryer_num_tot += 1;
                                                        break;
                                                    }
                                                    case "dryer" : {
                                                        dryer_num += 1;
                                                        dryer_num_tot += 1;
                                                        break;
                                                    }
                                                    default : {
                                                        etc_num += 1;
                                                        etc_num_tot += 1;
                                                        break;
                                                    }
                                                }

                                                //추가
                                                TextView TEL = mDialog.findViewById(R.id.TEL);
                                                TEL.setText(Tel);
                                                Tel = Tel.replace("-", "");
                                                String tel = "tel:" + Tel;


                                                // startActivity(new Intent("android.intent.action.DIAL", Uri.parse(Tel)));
                                                TEL.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                                                        Log.d("전화번호", tel);
                                                    }
                                                });
                                                ImageView Dialogimage = mDialog.findViewById(R.id.image);
                                                Glide.with(MapFragmentActivity.this).load(washteriaImageurl).into(Dialogimage);

                                                TextView washer_num_tv = mDialog.findViewById(R.id.washer_num);
                                                washer_num_tv.setText("세탁기 : " + washer_num + "/" + washer_num_tot + "대");
                                                TextView dryer_num_tv = mDialog.findViewById(R.id.dryer_num);
                                                dryer_num_tv.setText("건조기 : " + dryer_num + "/" + dryer_num_tot + "대");
                                                TextView etc_num_tv = mDialog.findViewById(R.id.etc_num);
                                                etc_num_tv.setText("기타 : " + etc_num + "/"+ etc_num_tot + "대");
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
                                        intent.putExtra("kakao_id", kakao_id);


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                int machine_id = Integer.parseInt(result.getContents());

                RequestQueue requestQueue = Volley.newRequestQueue(MapFragmentActivity.this);
                String url = String.format("http://"+HOST+":"+PORT+"/machine_info?machine_id="+machine_id);

                Log.d("asdf", url);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener(){
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = (new JSONObject(response.toString())).getJSONArray("result").getJSONObject(0);

                            int status = jsonObject.getInt("status");

                            if(status != 0){
                                Toast.makeText(MapFragmentActivity.this, "현재 이용이 불가능한 기기입니다.", Toast.LENGTH_SHORT).show();
                                throw(new NullPointerException());
                            }

                            String machine_type = jsonObject.getString("machine_type") ;
                            int operation_time;
                            Dialog mDialog = new Dialog(MapFragmentActivity.this);
                            mDialog.setContentView(R.layout.use_ask_dialog);
                            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                            TextView machine_type_tv = mDialog.findViewById(R.id.machine_type);

                            switch(machine_type){
                                case "big_washer" : machine_type_tv.setText("대형 세탁기 (45분)"); operation_time=45; break;
                                case "washer" : machine_type_tv.setText("중형 세탁기 (30분)"); operation_time=30; break;
                                case "big_dryer" : machine_type_tv.setText("대형 건조기 (45분)"); operation_time=45; break;
                                case "dryer" : machine_type_tv.setText("중형 건조기 (45분)"); operation_time=30; break;
                                default : machine_type_tv.setText("기타 기기 (15분)"); operation_time=15; break;
                            }


                            Button use_yes = mDialog.findViewById(R.id.use_yes);
                            Button use_no = mDialog.findViewById(R.id.use_no);

                            use_yes.setOnClickListener(v->{
                                String uri = String.format("http://"+HOST+":"+PORT+"/using_machine?machine_id="+machine_id+"&kakao_id="+kakao_id+"&date="+getTime()+"&operation_time="+operation_time);
                                Log.d("asdf", uri);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("asdf", "에러: " + error.toString());

                                    }
                                });
                                requestQueue.add(stringRequest);
                                mDialog.dismiss();
                            });
                            use_no.setOnClickListener(v->{
                                mDialog.dismiss();
                            });

                            mDialog.getWindow().setDimAmount(0);
                            mDialog.getWindow().setGravity(Gravity.BOTTOM);
                            mDialog.show();

                        }
                        catch (JSONException | NullPointerException e) {
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
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void createNotificationChannel()
    {
        //notification manager 생성
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if(android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.O){
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID
                    ,"Test Notification",mNotificationManager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            // Manager을 이용하여 Channel 생성
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }

    // Notification Builder를 만드는 메소드
    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("예약이 시간 완료! ")
                .setContentText("예약 시간이 완료 됐습니다!")
                .setSmallIcon(R.drawable.washer2);
        return notifyBuilder;
    }

    // Notification을 보내는 메소드
    public void sendNotification(){
        // Builder 생성
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        // Manager를 통해 notification 디바이스로 전달
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());
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