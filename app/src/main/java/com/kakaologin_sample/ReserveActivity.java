package com.kakaologin_sample;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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

    public void create(LinearLayout linearLayout){
        Button button = new Button(this);
        button.setText("기호 1번");
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        linearLayout.addView(button);
    }

}
