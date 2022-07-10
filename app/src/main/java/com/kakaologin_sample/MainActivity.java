package com.kakaologin_sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.navigation.NavigationView;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.model.KakaoSdkError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.User;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class MainActivity extends AppCompatActivity {
    private ImageView btn_login, btn_login_out;
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private Account account;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("사용자", getKeyHash());

        NaverMapSdk.getInstance(this).setClient(new NaverMapSdk.NaverCloudPlatformClient("p0w8vochex"));
        startMapFragmentActivity();
        if(AuthApiClient.getInstance().hasToken()){
            UserApiClient.getInstance().accessTokenInfo((accessTokenInfo, error) -> {
                if(error != null){
                    if(error instanceof KakaoSdkError && ((KakaoSdkError) error).isInvalidTokenError() == true){
                        startLoginActivity();
                        Log.d("asdf", "a");
                    }
                    else{
                        Log.d("asdf", "b");
                        startLoginActivity();
                    }
                }
                else{
                    UserApiClient.getInstance().me((user, meError) -> {
                        if (meError != null) {
                            Log.e("asdf", "사용자 정보 요청 실패", meError);
                        } else {
                            account = user.getKakaoAccount();
                            Log.d("asdf", account.toString());
                            Intent intent = new Intent(MainActivity.this, MapFragmentActivity.class);
                            //추가
                            intent.putExtra("profile_image", account.getProfile().getProfileImageUrl());
                            intent.putExtra("nickname",account.getProfile().getNickname());
                            intent.putExtra("kakao_id",user.getId());

                            startActivity(intent);

                        }
                        return null;
                    });
                    Log.d("asdf", "c");
                    startMapFragmentActivity();
                }
                return null;
            });
        }else{
            Log.d("asdf", "d");
            startLoginActivity();
        }
    }

    public void startLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void startMapFragmentActivity(){
        Intent intent = new Intent(MainActivity.this, MapFragmentActivity.class);
        startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)){
            if(!locationSource.isActivated()){
                naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}