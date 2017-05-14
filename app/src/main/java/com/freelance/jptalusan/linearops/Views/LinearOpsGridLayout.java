package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;

/**
 * Created by JPTalusan on 07/05/2017.
 */


//TODO: Add algo for left overs and stuff
public class LinearOpsGridLayout extends CustomGridLayout {
    private static String TAG = "LinearOpsGridLayout";
    public int positiveXCount = 0;
    public int negativeXCount = 0;
    public int positive1Count = 0;
    public int negative1Count = 0;
    private int currChild = 0;
    public Dimensions defaultDimensions = new Dimensions();

    public LinearOpsGridLayout(Context context) {
        super(context);
    }

    public LinearOpsGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean addScaledImage(int imageResource) {
        Log.d(TAG, "LinearOpsGridLayout:addScaledImage");
        if (getChildCount() < super.rows * super.cols) {
            LinearOpsImageView linearOpsImageView = new LinearOpsImageView(getContext());
            linearOpsImageView.setLayoutParams(super.generateParams());
            linearOpsImageView.setImageResource(imageResource);
            addView(linearOpsImageView);

            setImageViewType(imageResource);

            defaultDimensions.height = getChildAt(0).getHeight();
            defaultDimensions.width = getChildAt(0).getWidth();

            return true;
        }
        return false;
    }

    private String setImageViewType(int imageResource) {
        switch (imageResource) {
            case R.drawable.white_circle:
                positiveXCount++;
                return Constants.POSITIVE_X;
            case R.drawable.black_circle:
                negativeXCount++;
                return Constants.NEGATIVE_X;
            case R.drawable.white_box:
                positive1Count++;
                return Constants.POSITIVE_1;
            case R.drawable.black_box:
                negative1Count++;
                return Constants.NEGATIVE_1;
            default:
                return "";
        }
    }

    private String removeImageViewType(int imageResource) {
        switch (imageResource) {
            case R.drawable.white_circle:
                positiveXCount--;
                return Constants.POSITIVE_X;
            case R.drawable.black_circle:
                negativeXCount--;
                return Constants.NEGATIVE_X;
            case R.drawable.white_box:
                positive1Count--;
                return Constants.POSITIVE_1;
            case R.drawable.black_box:
                negative1Count--;
                return Constants.NEGATIVE_1;
            default:
                return "";
        }
    }

    private void prepareNewImageViews(int count) {

    }

    @Override
    public void reset() {
        super.reset();
        positiveXCount = 0;
        negativeXCount = 0;
        positive1Count = 0;
        negative1Count = 0;
    }

    @Override
    public String toString() {
        return "LinearOpsGridLayout{" +
                "positiveXCount=" + positiveXCount +
                ", negativeXCount=" + negativeXCount +
                ", positive1Count=" + positive1Count +
                ", negative1Count=" + negative1Count +
                '}';
    }

    public void moveViews(int dividend, String direction) {
        Log.d(TAG, "moveViews");
        for (int i = 0; i < dividend; ++i) {
            Log.d(TAG, "in moveViews: " + currChild);
            animateView(getChildAt(currChild), 0, direction);
            currChild++;
        }
    }



    //TODO: check what layout is used so know which side to fly to.
    private void animateView(View child, int delay, final String direction) {
        Log.d(TAG, "animateView");
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        final LinearOpsImageView temp = (LinearOpsImageView) child;
        Log.d(TAG, "wxh" + width + " x " + height);
        LayoutParams params = (LayoutParams) child.getLayoutParams();
        Log.d(TAG, "left/top" + params.leftMargin + "/" + params.topMargin);
        AnimationSet animSet = new AnimationSet(false);
        animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.linear_interpolator));

        if ("LEFT" == direction) {
            int childLoc[] = {0, 0};
            child.getLocationOnScreen(childLoc);
            int parentLoc[] = {0, 0};
            getLocationInWindow(parentLoc);
            Log.d(TAG, "child: " + childLoc[0] + "," + childLoc[1]);
            Log.d(TAG, "parent: " + parentLoc[0] + "," + parentLoc[1]);
            //Translate animation coordinates are based on the current object. 0, 0 is the current position of the object
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0 - (params.leftMargin + child.getWidth()), 0, 0);

            translateAnimation.setDuration(Constants.ANIMATION_DURATION);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setStartOffset(delay);
            animSet.addAnimation(translateAnimation);
            temp.startAnimation(animSet);

        } else {
            //TODO: fix for right
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f);
            scaleAnimation.setDuration(Constants.ANIMATION_DURATION);
            scaleAnimation.setRepeatCount(1);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setStartOffset(delay);
            animSet.addAnimation(scaleAnimation);
            temp.startAnimation(animSet);
        }


        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (direction == "LEFT") {
                    removeImageViewType(temp.getId());
                    temp.setVisibility(View.GONE);
//                Handler h = new Handler();
//                h.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        removeView(temp);
//                    }
//                }, 500);
                    if (listener != null)
                        listener.onAnimationEnd(temp.getId());
                } else {
                    //Change drawable here.
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //TODO: How to use when in another file
    public interface LinearOpsGridLayoutListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        // or when data has been loaded
        void onAnimationEnd(int val);
//        void onSubAnimationEnded(int val, int type);
    }

    private LinearOpsGridLayoutListener listener;

    // Assign the listener implementing events interface that will receive the events
    public void onLinearOpsGridLayoutListener(LinearOpsGridLayoutListener listener) {
        this.listener = listener;
    }
}
