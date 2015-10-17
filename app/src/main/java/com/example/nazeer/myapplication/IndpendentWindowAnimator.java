package com.example.nazeer.myapplication;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by nazeer on 10/17/15.
 */
public class IndpendentWindowAnimator {
    Context context;
    WindowManager windowManager;

    int statusBarHeight;
    WindowManager.LayoutParams layoutParams;
    public  IndpendentWindowAnimator (Context context){
        this.context=context;
        windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        setUpWindowParams();
        Rect statusBarrectangle= new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBarrectangle);;
        statusBarHeight= statusBarrectangle.top;
        Display display = windowManager.getDefaultDisplay();


    }

    private void setUpWindowParams() {
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                // Display it on top of other application windows, but only for the current user
                WindowManager.LayoutParams.TYPE_APPLICATION,
                // Don't let it grab the input focus
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                // Make the underlying application window visible through any transparent parts
                PixelFormat.TRANSLUCENT);

        // Define the position of the window within the screen
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
    }

    public void startImageViewAnimation(ImageView fromIv,ImageView toIv,  ImageView transientIv){
        matchLayoutParams(fromIv, transientIv);
        windowManager.addView(transientIv, transientIv.getLayoutParams());


    }

    private void matchLayoutParams(ImageView fromIv, ImageView transientIv) {
        int location[]=new int[2];
        fromIv.getLocationInWindow(location);
        ViewGroup.LayoutParams fromParams=fromIv.getLayoutParams();
        WindowManager.LayoutParams transientParams=new WindowManager.LayoutParams(fromParams.width,
                fromParams.height,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        transientParams.gravity=Gravity.TOP | Gravity.LEFT;
        transientParams.x=location[0];
        transientParams.y=location[1]-getStatusBarHeight(context);
        transientIv.setLayoutParams(transientParams);



    }
    public static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil(25 * resources.getDisplayMetrics().density);
    }
}
