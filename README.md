# Animation Manager and A pre lollipop Transition Example # 
"IndpendentWindowAnimator" can Start An Animation between two views , Also can Animate between two given locations.

It uses a view that you provide and animate it between the positions of the two given views or locations, And will animate the size of the view from the start view to target view will moving . 




```
#!java

                ImageView transientIv = new ImageView(simpleAnimationActivity.this);
                transientIv.setImageResource(android.R.color.black);
                indpendentWindowAnimator.starViewAnimation(topLeftView, bottomRight, transientIv, 600);
```

Also You can Use location and width of size of the start and target view 

```
#!java

starViewAnimation(int [] startLocation, int startWidth,int startHeight ,
                  int []targetLocation, int targetWidth,int targetHeight, View transV, int duration) 
```
You can set An Animation Listner to track updates through the animation 

```
#!java

        IndpendentWindowAnimator animator = new IndpendentWindowAnimator(this);
        animator.setAnimatoionListner(new AnimationListner() {
            @Override
            public void onStart() {


            }

            @Override
            public void onupdate(double animationfraction) {
            }

            @Override
            public void onEnd() {

            }

            @Override
            public void onCacneled() {

            }
        });

```

# Pre Lollipop Transition  #
This is inspired by the devByte from this link 
[DevBytes: Custom Activity Animations
](https://www.youtube.com/watch?v=CPxkoe2MraA) 
The idea is to disable custom Activity transition 
with 

```
#!java
        startActivity(intent);
        overridePendingTransition(0,0);

```

And display the target activity as transparent and then perform the wanted views animation while animating the background of the activity 

You Can make An Activity Transparent 
by Setting A Style to it with a transparent windowbackground property like this 

** make sure you use the same parent you are using for you app theme ** 

```
#!xml

    <style name="Transparent" parent="Theme.AppCompat.NoActionBar">
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
    </style>
```
  And set The style to the activity 

```
#!xml

        <activity
            android:name=".ExampleTransitionActivity2"
            android:theme="@style/Transparent" >
        </activity>
```
Then You Can Use The Animator Library to display an animation and use the Call Backs to hide the Views On Start Of animation and then show it at the end to give The illusion that the view its self is moving . 

```
#!java

        IndpendentWindowAnimator animator = new IndpendentWindowAnimator(this);
        //the view to be used in the animation 
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
```
# Contributions and pull requests are welcomed  #
This is still very young and if you have any comments or anything I'd be happy to hear them 