package com.example.nazeer.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.buttonSimpleAnimation:
                startActivity(new Intent(MainActivity.this,simpleAnimationActivity.class));
                break;
            case R.id.buttonActivityTransition:
                startActivity(new Intent(MainActivity.this,ExampleActivityTransition.class));
                break;
        }
    }
}
