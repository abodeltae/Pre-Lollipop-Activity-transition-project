package com.example.nazeer.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;




public class MainActivity extends Activity {
 ImageView topLeft,bottomRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topLeft=(ImageView)findViewById(R.id.imageViewtopleft);
        bottomRight=(ImageView)findViewById(R.id.imageViewbottmRight);
        final IndpendentWindowAnimator indpendentWindowAnimator=new IndpendentWindowAnimator(this);
        final ImageView transientIv=new ImageView(this);
        WindowManager.LayoutParams params=new WindowManager.LayoutParams(100,100, WindowManager.LayoutParams.TYPE_APPLICATION,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        transientIv.setImageResource(R.color.colorAccent);
        transientIv.setLayoutParams(params);
        Handler h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                indpendentWindowAnimator.startImageViewAnimation(topLeft,bottomRight,transientIv);

            }
        },300);

    }


}
