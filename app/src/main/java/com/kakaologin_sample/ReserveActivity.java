package com.kakaologin_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReserveActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                update();
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

    public void update(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String page = "localhost";
                    // URL 객체 생성
                    URL url = new URL(page);
                    // 연결 객체 생성
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    // Post 파라미터
                        String params = "param=1"
                                + "&param2=2" + "sale";
                    // 결과값 저장 문자열
                    final StringBuilder sb = new StringBuilder();
                    // 연결되면
                    if(conn != null) {
                        Log.i("tag", "conn 연결");
                        // 응답 타임아웃 설정
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setConnectTimeout(10000);
                        // POST 요청방식
                        conn.setRequestMethod("POST");
                        // 포스트 파라미터 전달
                        conn.getOutputStream().write(params.getBytes("utf-8"));
                        // url에 접속 성공하면 (200)
                        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            // 결과 값 읽어오는 부분
                            BufferedReader br = new BufferedReader(new InputStreamReader(
                                    conn.getInputStream(), "utf-8"
                            ));
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            // 버퍼리더 종료
                            br.close();
                            Log.i("tag", "결과 문자열 :" +sb.toString());
                            // 응답 Json 타입일 경우
                            //JSONArray jsonResponse = new JSONArray(sb.toString());
                            //Log.i("tag", "확인 jsonArray : " + jsonResponse);

                        }
                        // 연결 끊기
                        conn.disconnect();
                    }

                    //백그라운드 스레드에서는 메인화면을 변경 할 수 없음
                    // runOnUiThread(메인 스레드영역)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "응답" + sb.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (Exception e) {
                    Log.i("tag", "error :" + e);
                }
            }
        });
        th.start();
    }


    public void create(LinearLayout linearLayout){
        Button button = new Button(this);
        button.setText("기호 1번");
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        linearLayout.addView(button);
    }

}
