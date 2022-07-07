package com.kakaologin_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.User;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.overlay.Marker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="사용자";
    private ImageView btn_login, btn_login_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("사용자", getKeyHash());





        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MapFragmentActivity.class);
                startActivity(intent);
            }
        });

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

    // 키해시 얻기
    public String getKeyHash(){
        try{
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_SIGNATURES);
            if(packageInfo == null) return null;
            for(Signature signature: packageInfo.signatures){
                try{
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                }catch (NoSuchAlgorithmException e){
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature="+signature, e);
                }
            }
        }catch(PackageManager.NameNotFoundException e){
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }
}