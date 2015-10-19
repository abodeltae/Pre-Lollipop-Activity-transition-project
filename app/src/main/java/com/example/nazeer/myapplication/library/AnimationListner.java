package com.example.nazeer.myapplication.library;

import android.animation.ValueAnimator;

/**
 * Created by nazeer on 19/10/15.
 */
public interface AnimationListner {
     void onStart();
     void onupdate(double animationfraction);
     void onEnd();
     void onCacneled();

}
