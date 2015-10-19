package com.example.nazeer.myapplication;

import android.app.Application;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by nazeer on 19/10/15.
 */
public class App extends Application {
    public static HashMap<Integer,View> targetViews =new HashMap<Integer,View>();
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
