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
 from the given from a given "start Image view"  to a given "target Image view "
  the animation doesnt depend on the activity nor the fragment so it can be started while changing activities or fragments to give the
  lollipop transition feeling

  **** its the user  responsibilty to provide a dummy target view to help determine the view location on the screen
  **** this can be done by viewing the target activity as invisible and sending the views from it see ExampleActivityTransition (Not the most effiecient but it works )
  **** Call backs are provided to the user to let him determine when to hide and show the views to give the illusion of the moving View
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


    public void starViewAnimation(View fromView, View toView, View transV, int duration) {
        this.transientView=transV;
        matchLayoutParams(fromView, transientView);
        windowManager.addView(transientView, transientView.getLayoutParams());
        valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(getValueAnimatorUpdateListner(fromView, toView));
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
                        windowManager.removeViewImmediate(transientView);
                    }
                },100);
              // windowManager.removeView(transientView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if(mAnimationListner!=null){
                    windowManager.removeView(transientView);
                    mAnimationListner.onCacneled();
                }
                windowManager.removeView(transientView);

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
                return listener;
    }



    public void starViewAnimation(View fromIv, View toIv, View transientIv) {
        starViewAnimation(fromIv, toIv, transientIv, 600);


    }

    private ValueAnimator.AnimatorUpdateListener getValueAnimatorUpdateListner(final View fromView,
                                                                         final View toView) {
        final int targetlocation[] = new int[2];
        toView.getLocationOnScreen(targetlocation);
        final WindowManager.LayoutParams transientParams = (WindowManager.LayoutParams) transientView.getLayoutParams();
        final int startX = transientParams.x,
                startY = transientParams.y,
                xDifference = targetlocation[0] - startX,
                yDifference = targetlocation[1] - getStatusBarHeight(activity) - startY,
                startWidth = transientParams.width,
                startHeight = transientParams.height,
                widthDifference = toView.getLayoutParams().width - startWidth,
                heightDifference = toView.getLayoutParams().height - startHeight;

        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
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

                windowManager.updateViewLayout(transientView, transientParams);

            }

        };
        return listener;
    }

    private void matchLayoutParams(View fromIv, View transientIv) {
        int location[] = new int[2];
        fromIv.getLocationInWindow(location);
        ViewGroup.LayoutParams fromParams = fromIv.getLayoutParams();
        WindowManager.LayoutParams transientParams = new WindowManager.LayoutParams(fromParams.width,
                fromParams.height,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        transientParams.gravity = Gravity.TOP | Gravity.LEFT;
        transientParams.x = location[0];
        transientParams.y = location[1] - getStatusBarHeight(activity);
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
