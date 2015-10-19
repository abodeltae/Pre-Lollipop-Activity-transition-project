package com.example.nazeer.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.nazeer.myapplication.library.AnimationListner;
import com.example.nazeer.myapplication.library.IndpendentWindowAnimator;

public class ExampleActivityTransition extends AppCompatActivity implements View.OnClickListener{

    ImageView redIv,blueIv,greenIv;
    RelativeLayout containerLayout,targetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exampletransition1);
        redIv= (ImageView) findViewById(R.id.imageViewRedCircle);
        blueIv= (ImageView) findViewById(R.id.imageViewBlueCircle);
        greenIv= (ImageView) findViewById(R.id.imageViewGreenCircle);

        redIv.setOnClickListener(this);
        greenIv.setOnClickListener(this);
        blueIv.setOnClickListener(this);

        //this layout will be used to contain a dummy hidden view to get the target views needed to be passed to the animator
        containerLayout= (RelativeLayout) findViewById(R.id.containerLayout);
        //the layout of the target activity
         targetLayout= (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_example_transition2, null);
        //keep it hidden as its not needed
        // *** DONT SET IT TO GONE ***
        targetLayout.setVisibility(View.INVISIBLE);

        containerLayout.addView(targetLayout);

    }

    @Override
    public void onClick(View v) {


        int id=v.getId();
        switch (id){
            case R.id.imageViewRedCircle:
                startAnimation(v,R.drawable.red_circle);
                break;
            case R.id.imageViewGreenCircle:
                startAnimation(v,R.drawable.green_circle);
                break;
            case R.id.imageViewBlueCircle:
                startAnimation(v,R.drawable.blue_circle);
                break;
        }
    }

    private void startAnimation(final View v, int imageResource) {
        View targetView=targetLayout.findViewById(R.id.imageViewTarget);
        IndpendentWindowAnimator indpendentWindowAnimator=new IndpendentWindowAnimator(this);
        indpendentWindowAnimator.setAnimatoionListner(new AnimationListner() {
            @Override
            public void onStart() {
                v.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onupdate(double animationfraction) {
                Log.w("mAnimationsListner", "onUpdate" + animationfraction);
            }

            @Override
            public void onEnd() {
                Log.w("mAnimationsListner", "onEnd");
                v.setVisibility(View.VISIBLE);
                Handler h = new Handler();
                h.post(App.runnables.get(R.id.imageViewTarget));

            }

            @Override
            public void onCacneled() {

            }
        });
        ImageView animationIv=new ImageView(getApplicationContext());
        animationIv.setImageResource(imageResource);
        indpendentWindowAnimator.starViewAnimation(v, targetView, animationIv,400);
        Intent intent =new Intent(this, ExampleTransitionActivity2.class);
        intent.putExtra("selectedImage",imageResource);
        startActivity(intent);
    }
}
