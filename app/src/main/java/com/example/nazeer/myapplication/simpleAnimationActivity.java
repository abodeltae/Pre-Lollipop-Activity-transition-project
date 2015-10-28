package com.example.nazeer.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nazeer.myapplication.library.AnimationListner;
import com.example.nazeer.myapplication.library.IndpendentWindowAnimator;


public class simpleAnimationActivity extends Activity {
 ImageView topLeft,bottomRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_animation);
        topLeft=(ImageView)findViewById(R.id.imageViewtopleft);
        bottomRight=(ImageView)findViewById(R.id.imageViewbottmRight);
        final IndpendentWindowAnimator indpendentWindowAnimator=new IndpendentWindowAnimator(this);
        indpendentWindowAnimator.setAnimatoionListner(new AnimationListner() {
            @Override
            public void onStart() {
                Log.w("mAnimationsListner", "onstart");
            }

            @Override
            public void onupdate(double animationfraction) {
                Log.w("mAnimationsListner", "onUpdate" + animationfraction);
            }

            @Override
            public void onEnd() {
                Log.w("mAnimationsListner", "onEnd");
            }

            @Override
            public void onCacneled() {

            }
        });

        Button startButton= (Button) findViewById(R.id.buttonStartAnimation);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageView transientIv = new ImageView(simpleAnimationActivity.this);
                transientIv.setImageResource(android.R.color.black);
                indpendentWindowAnimator.starViewAnimation(topLeft, bottomRight, transientIv, 600);

            }
        });


    }


}
