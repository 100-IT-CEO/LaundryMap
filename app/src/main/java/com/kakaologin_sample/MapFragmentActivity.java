package com.kakaologin_sample;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MapFragmentActivity extends AppCompatActivity {
    private static final String TAG = "사용자";
    private ImageView btn_login, btn_login_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment_activity);



//        btn_login.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this,(oAuthToken, error) -> {
//                    if (error != null) {
//                        Log.e(TAG, "로그인 실패", error);
//                    } else if (oAuthToken != null) {
//                        Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
//
//                        UserApiClient.getInstance().me((user, meError) -> {
//                            if (meError != null) {
//                                Log.e(TAG, "사용자 정보 요청 실패", meError);
//                            } else {
//                                System.out.println("로그인 완료");
//                                Log.i(TAG, user.toString());
//                                {
//                                    Log.i(TAG, "사용자 정보 요청 성공" +
//                                            "\n회원번호: "+user.getId() +
//                                            "\n이메일: "+user.getKakaoAccount().getEmail());
//                                }
//                                Account user1 = user.getKakaoAccount();
//                                System.out.println("사용자 계정" + user1);
//                            }
//                            return null;
//                        });
//                    }
//                    return null;
//                });
//
//            }
//        });


    }
}