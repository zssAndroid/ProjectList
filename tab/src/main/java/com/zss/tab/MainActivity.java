package com.zss.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_main);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            final int finalI = i;
            linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == 0) {
                        startActivity(new Intent(MainActivity.this, OneActivity.class));
                    } else if (finalI == 1) {
                        startActivity(new Intent(MainActivity.this, TwoActivity.class));
                    } else if (finalI == 2) {
                        startActivity(new Intent(MainActivity.this, ThreeActivity.class));
                    } else if (finalI == 3) {
                        startActivity(new Intent(MainActivity.this, FourActivity.class));
                    }
                }
            });
        }
    }
}
