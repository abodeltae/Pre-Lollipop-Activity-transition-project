package com.example.nazeer.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.nazeer.myapplication.library.AnimationListner;
import com.example.nazeer.myapplication.library.IndpendentWindowAnimator;

public class ExampleTransitionActivity2 extends AppCompatActivity {
ImageView imageView;
RelativeLayout containerLayout;//parent  layout
    View backGround;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_transition2);
        imageView= (ImageView) findViewById(R.id.imageViewTarget);
        containerLayout= (RelativeLayout) findViewById(R.id.relativeLayoutContainer);
        backGround=findViewById(R.id.backGround);

        if(savedInstanceState==null)backGround.setAlpha(0f);
        else backGround.setAlpha(1f)
                ;
        int imageResource=getIntent().getIntExtra("selectedImage",-1);
        if(imageResource!=-1){
            imageView.setImageResource(imageResource);
        }



            final ViewTreeObserver viewTreeObserver=containerLayout.getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if(savedInstanceState==null)
                    doStartAnimation();
                    containerLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });



    }

    private void doStartAnimation() {
        int imageResource=getIntent().getIntExtra("selectedImage",-1);
        int []startLocation=(int[])getIntent().getSerializableExtra("startLocation");
        int startWidth=getIntent().getIntExtra("startWidth",-1);
        int startHeight=getIntent().getIntExtra("startHeight",-1);
        if(imageResource!=-1){
            imageView.setImageResource(imageResource);
            imageView.setVisibility(View.INVISIBLE);
        }
        IndpendentWindowAnimator animator = new IndpendentWindowAnimator(this);
        ImageView animationView=new ImageView(this);
        animationView.setImageResource(imageResource);
        animator.setAnimatoionListner(new AnimationListner() {
            @Override
            public void onStart() {
                imageView.setVisibility(View.INVISIBLE);
                ExampleActivityTransition.selectedView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onupdate(double animationfraction) {
                backGround.setAlpha((float)animationfraction);
            }

            @Override
            public void onEnd() {
                imageView.setVisibility(View.VISIBLE);
                ExampleActivityTransition.selectedView.setVisibility(View.VISIBLE);
                ExampleActivityTransition.selectedView=null;//release View
            }

            @Override
            public void onCacneled() {

            }
        });
        animator.starViewAnimation(startLocation, startWidth, startHeight, imageView, animationView, 600);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
