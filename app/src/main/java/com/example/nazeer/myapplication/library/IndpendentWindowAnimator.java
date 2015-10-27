package com.example.nazeer.myapplication.library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by nazeer on 10/17/15.
 */

/*
starViewAnimation( ) method create a view on top of the screen that will animate the position and size
 from the given from a given "start view"  to a given "target  view "
  the animation doesnt depend on the activity nor the fragment so it can be started while changing activities or fragments to give the
  lollipop transition feeling

  *** the user should provide an animationView That will be used during the animation
  *   He can then set a photo to it , add text etc ...

  **** its the user  responsibilty to provide a dummy target view to help determine the view location on the screen
  **** this can be done by viewing the target activity as invisible and sending the views from it see ExampleActivityTransition (Not the most effiecient but it works )
  **** Call backs are provided to the user to let him determine when to hide and show the views to give the illusion of the moving View
  ****  Becarfule with slow Animation The will continue even if you changed activities
  */

public class IndpendentWindowAnimator {
    Activity activity;
    WindowManager windowManager;
    int statusBarHeight;
    private AnimationListner mAnimationListner=null;
    ValueAnimator valueAnimator;
    View transientView;

    public IndpendentWindowAnimator(Activity activity) {
        this.activity = activity;
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Rect statusBarrectangle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBarrectangle);
        statusBarHeight = statusBarrectangle.top;

    }
    public void starViewAnimation(View startView, View targetView, View transientIv) {
        starViewAnimation(startView, targetView, transientIv, 600);
    }

    public void starViewAnimation(View startView, View targetView, View transV, int duration) {
       int []startLocation=new int [2];
        int []targetLocation=new int [2];
        startView.getLocationOnScreen(startLocation);
        targetView.getLocationOnScreen(targetLocation);
        int startWidth=startView.getLayoutParams().width,
                startHeight=startView.getLayoutParams().height,
                targetWidth=targetView.getLayoutParams().width,
                targetHeight=targetView.getLayoutParams().height;
        starViewAnimation(startLocation,startWidth,startHeight,targetLocation,targetWidth,targetHeight,transV,duration);


    }
    public void starViewAnimation(int [] startLocation, int startWidth,int startHeight ,View targetView, View transV, int duration) {
        int []targetLocation=new int [2];
        int targetWidth=targetView.getLayoutParams().width,
                targetHeight = targetView.getLayoutParams().height;
        targetView.getLocationOnScreen(targetLocation);
        starViewAnimation(startLocation, startWidth, startHeight, targetLocation, targetWidth, targetHeight, transV, duration);

    }
    public void starViewAnimation(int [] startLocation, int startWidth,int startHeight ,
                                    int []targetLocation, int targetWidth,int targetHeight, View transV, int duration) {
        this.transientView=transV;
        matchLayoutParams(startLocation,startWidth,startHeight, transientView);
        windowManager.addView(transientView, transientView.getLayoutParams());
        valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(getValueAnimatorUpdateListner(startLocation,startWidth,startHeight,targetLocation,targetWidth,targetHeight));
        valueAnimator.addListener(getListner());
        valueAnimator.start();

    }


    private Animator.AnimatorListener getListner() {
        Animator.AnimatorListener listener=new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(mAnimationListner!=null){
                    mAnimationListner.onStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mAnimationListner!=null){
                    mAnimationListner.onEnd();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            windowManager.removeViewImmediate(transientView);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },100);
              // windowManager.removeView(transientView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

                try {
                    if(mAnimationListner!=null){

                    mAnimationListner.onCacneled();
                    }
                    windowManager.removeView(transientView);
                    mAnimationListner.onCacneled();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
                return listener;
    }




    private  ValueAnimator.AnimatorUpdateListener getValueAnimatorUpdateListner(
            int []startLocation, final int startWidth, final int startHeight,
            int []targetLocation, int targetWidth,int targetHeight){
        final WindowManager.LayoutParams transientParams = (WindowManager.LayoutParams) transientView.getLayoutParams();
        final int startX = startLocation[0],
                startY = startLocation[1],
                xDifference = targetLocation[0] - startX,
                yDifference = targetLocation[1] - getStatusBarHeight(activity) - startY,
                widthDifference = targetWidth - startWidth,
                heightDifference = targetHeight - startHeight;
        final ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            int step = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {



                double fraction = (Integer) animation.getAnimatedValue() /100.0;

                if(mAnimationListner!=null){
                    mAnimationListner.onupdate(fraction);
                }
                //The step thingy to skip a problem with the window manager not behaving properly when changing the position
                // and size of view at the same time , so I change one property every time
                // (mostly it will go unnoticed unless a realy slow animation )
                // There's an open issue with this problem on this link https://code.google.com/p/android/issues/detail?id=74099
                if (step++ % 2 == 0||(widthDifference==0&&heightDifference==0/*if size is the same update every time for smotthness*/)) {
                    //modify position
                    int addedX = (int) (fraction * xDifference);
                    int addedY = (int) (fraction * yDifference);
                    transientParams.x = startX + addedX;
                    transientParams.y = startY + addedY;
                }
                if (step % 2 == 0&&(widthDifference!=0||heightDifference!=0)) {
                    //modifiy width and height
                    int addedWidth = (int) (fraction * widthDifference);
                    int addedHeight = (int) (fraction * heightDifference);
                    transientParams.height = startHeight + addedHeight;
                    transientParams.width = startWidth + addedWidth;
                }
                try {
                    windowManager.updateViewLayout(transientView, transientParams);

                }catch (Exception e){

                    e.printStackTrace();
                    valueAnimator.cancel();
                }

            }

        };
            return listener;

    }




    private void matchLayoutParams(int [] startLocation,int startWidth,int startHeight, View transientIv) {


        WindowManager.LayoutParams transientParams = new WindowManager.LayoutParams(startWidth,
                startHeight,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        transientParams.gravity = Gravity.TOP | Gravity.LEFT;
        transientParams.x = startLocation[0];
        transientParams.y = startLocation[1] - getStatusBarHeight(activity);
        transientIv.setLayoutParams(transientParams);


    }

    private static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil(25 * resources.getDisplayMetrics().density);
    }

    public void setAnimatoionListner(AnimationListner listner){
        this.mAnimationListner=listner;
    }
    public AnimationListner getAnimationListner(){
        return mAnimationListner;
    }
    public void removeAnimationListners(){
        mAnimationListner=null;
    }
    public void cancelAnimation(){
        valueAnimator.cancel();
    }
}
