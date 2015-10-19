package com.example.nazeer.myapplication;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by nazeer on 19/10/15.
 */
public class App extends Application {
    public static HashMap<Integer,Runnable> runnables=new HashMap<Integer,Runnable>();
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
