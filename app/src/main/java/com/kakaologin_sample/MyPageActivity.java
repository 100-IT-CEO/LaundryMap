package com.kakaologin_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPageActivity extends AppCompatActivity {

    private static final String HOST = "143.248.199.169";
    private static final String PORT = "80";
    private static String profile_image_url;
    private static String nickname;
    private static String kakao_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Intent intent = getIntent();
        kakao_id = intent.getStringExtra("kakao_id");
        nickname = intent.getStringExtra("nickname");
        profile_image_url = intent.getStringExtra("profile_image_url");

        Log.d("intent_get", kakao_id);

        TextView name = findViewById(R.id.mypage_name);
        name.setText(nickname);

        ImageView imageView = findViewById(R.id.mypage_profileImg);
        Glide.with(MyPageActivity.this).load(profile_image_url).into(imageView);

        int initial_imageView_height = imageView.getLayoutParams().height;
        int initial_textView_height = name.getLayoutParams().height;
        ScrollView scrollView = findViewById(R.id.mypage_scrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView

                Log.d("scroll", String.valueOf(scrollY));

                if(imageView.getLayoutParams().height > initial_imageView_height*0.75 || initial_imageView_height - scrollY > imageView.getLayoutParams().height){
                    imageView.getLayoutParams().height = initial_imageView_height - scrollY;
                    imageView.requestLayout();
                    name.getLayoutParams().height = initial_textView_height - scrollY;
                    name.requestLayout();
                }
            }
        });

        TextView reg_date = findViewById(R.id.reg_date);

        RequestQueue requestQueue = Volley.newRequestQueue(MyPageActivity.this);
        String url = String.format("http://"+HOST+":"+PORT+"/user?kakao_id="+kakao_id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener(){
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject result = jsonObject.getJSONArray("result").getJSONObject(0);

                    String reg_dates = result.getString("regdate");

                    reg_date.setText("가입일 : " + reg_dates.substring(0, 19).replace('T',' '));
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