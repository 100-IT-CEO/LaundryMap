package com.kakaologin_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ImageView imageView = findViewById(R.id.mypage_profileImg);
        int initial_imageView_height = imageView.getLayoutParams().height;

        ScrollView scrollView = findViewById(R.id.mypage_scrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView

                Log.d("scroll", String.valueOf(scrollY));

                imageView.getLayoutParams().height = initial_imageView_height - scrollY;
                imageView.requestLayout();
            }
        });
    }
}