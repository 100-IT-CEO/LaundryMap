package com.kakaologin_sample;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;


public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this, "823bfccbaed1b11ec9d2d72feb1edf5b");
    }


}
