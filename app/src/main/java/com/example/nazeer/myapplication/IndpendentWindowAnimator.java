package com.example.nazeer.myapplication;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by nazeer on 10/17/15.
 */
public class IndpendentWindowAnimator {
    Activity context;
    WindowManager windowManager;
    int statusBarHeight;

    public IndpendentWindowAnimator(Activity activity) {
        this.context = activity;
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Rect statusBarrectangle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBarrectangle);
        statusBarHeight = statusBarrectangle.top;

    }


    public void startImageViewAnimation(ImageView fromIv, ImageView toIv, ImageView transientIv, int duration) {
        matchLayoutParams(fromIv, transientIv);
        windowManager.addView(transientIv, transientIv.getLayoutParams());
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(getValueAnimatorListner(fromIv, toIv, transientIv));
        valueAnimator.start();


    }


    public void startImageViewAnimation(ImageView fromIv, ImageView toIv, ImageView transientIv) {
        startImageViewAnimation(fromIv, toIv, transientIv, 600);


    }

    private ValueAnimator.AnimatorUpdateListener getValueAnimatorListner(final View fromIv,
                                                                         final View toIv,
                                                                         final View transientIv) {
        final int targetlocation[] = new int[2];
        toIv.getLocationOnScreen(targetlocation);
        final WindowManager.LayoutParams transientParams = (WindowManager.LayoutParams) transientIv.getLayoutParams();
        final int startX = transientParams.x,
                startY = transientParams.y,
                xDifference = targetlocation[0] - startX,
                yDifference = targetlocation[1] - getStatusBarHeight(context) - startY,
                startWidth = transientParams.width,
                startHeight = transientParams.height,
                widthDifference = toIv.getLayoutParams().width - startWidth,
                heightDifference = toIv.getLayoutParams().height - startHeight;

        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            int step = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                double fraction = (Integer) animation.getAnimatedValue() / 100.0;
                //The step thingy to skip a problem with the window manager not behaving properly when changing the position
                // and size of view at the same time , so I change one property every time
                // (mostly it will go unnoticed unless a realy slow animation )
                // There's an open issue with this problem on this link https://code.google.com/p/android/issues/detail?id=74099
                if (step++ % 2 == 0) {
                    //modify position
                    int addedX = (int) (fraction * xDifference);
                    int addedY = (int) (fraction * yDifference);
                    transientParams.x = startX + addedX;
                    transientParams.y = startY + addedY;
                }
                if (step % 2 == 0) {
                    //modifiy width and height
                    int addedWidth = (int) (fraction * widthDifference);
                    int addedHeight = (int) (fraction * heightDifference);
                    transientParams.height = startHeight + addedHeight;
                    transientParams.width = startWidth + addedWidth;
                }

                windowManager.updateViewLayout(transientIv, transientParams);
                if (fraction == 1) windowManager.removeView(transientIv);
            }

        };
        return listener;
    }

    private void matchLayoutParams(ImageView fromIv, ImageView transientIv) {
        int location[] = new int[2];
        fromIv.getLocationInWindow(location);
        ViewGroup.LayoutParams fromParams = fromIv.getLayoutParams();
        WindowManager.LayoutParams transientParams = new WindowManager.LayoutParams(fromParams.width,
                fromParams.height,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        transientParams.gravity = Gravity.TOP | Gravity.LEFT;
        transientParams.x = location[0];
        transientParams.y = location[1] - getStatusBarHeight(context);
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
}
