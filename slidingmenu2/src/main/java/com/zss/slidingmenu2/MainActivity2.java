package com.zss.slidingmenu2;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_head;
    private ListView lv_left;
    private DragLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main);

        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_head.setOnClickListener(this);

        lv_left = (ListView) findViewById(R.id.lv_left);
        lv_left.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView)view).setTextColor(Color.WHITE);
                return view;
            }
        });

        ListView lv_main = (ListView) findViewById(R.id.lv_main);
        lv_main.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));

        dl = (DragLayout) findViewById(R.id.dl);
        final Random random = new Random();


        dl.setOnDragChangeListener(new DragLayout.OnDragChangeListener() {


            @Override
            public void onOpen() {
                System.out.println("---打开了!!!");

                lv_left.smoothScrollToPosition(random.nextInt(50));
            }

            @Override
            public void onClose() {
                System.out.println("---关闭了!!!");
//                iv_head.setTranslationX();
                ObjectAnimator animator = ObjectAnimator.ofFloat(iv_head, "translationX", 15f);
                animator.setInterpolator(new CycleInterpolator(4));
                animator.setDuration(500);
                animator.start();

            }

            @Override
            public void onDraing(float percent) {
                // 0.0 -> 1.0
                System.out.println("---拖拽!" + percent);
                ViewCompat.setAlpha(iv_head, 1 - percent);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(dl != null) dl.open();
    }
}
