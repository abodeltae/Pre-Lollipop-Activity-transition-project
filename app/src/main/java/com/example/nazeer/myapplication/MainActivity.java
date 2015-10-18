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

        Button startButton= (Button) findViewById(R.id.buttonStartAnimation);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageView transientIv=new ImageView(MainActivity.this);
                transientIv.setImageResource(android.R.color.black);
                indpendentWindowAnimator.startImageViewAnimation(topLeft,bottomRight,transientIv,5000);
            }
        });

    }


}
