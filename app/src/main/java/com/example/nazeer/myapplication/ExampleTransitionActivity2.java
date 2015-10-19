package com.example.nazeer.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ExampleTransitionActivity2 extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_transition2);
        imageView= (ImageView) findViewById(R.id.imageViewTarget);
        imageView.setVisibility(View.INVISIBLE);
        int imageResource=getIntent().getIntExtra("selectedImage",-1);
        if(imageResource!=-1){
            imageView.setImageResource(imageResource);
        }
        App.targetViews.put(R.id.imageViewTarget,imageView);
    }
}
