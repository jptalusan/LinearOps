package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.os.Handler;
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

import static android.R.attr.direction;

public class LinearOpsGridLayout extends CustomGridLayout {
    private static String TAG = "LinearOpsGridLayout";
    public int positiveXCount = 0;
    public int negativeXCount = 0;
    public int positive1Count = 0;
    public int negative1Count = 0;
    public Dimensions defaultDimensions = new Dimensions();
    private int[] drawables;

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
            case R.drawable.white_box:
                positiveXCount++;
                return Constants.POSITIVE_X;
            case R.drawable.black_box:
                negativeXCount++;
                return Constants.NEGATIVE_X;
            case R.drawable.white_circle:
                positive1Count++;
                return Constants.POSITIVE_1;
            case R.drawable.black_circle:
                negative1Count++;
                return Constants.NEGATIVE_1;
            default:
                return "";
        }
    }

    private String removeImageViewType(int imageResource) {
        switch (imageResource) {
            case R.drawable.white_box:
                positiveXCount--;
                return Constants.POSITIVE_X;
            case R.drawable.black_box:
                negativeXCount--;
                return Constants.NEGATIVE_X;
            case R.drawable.white_circle:
                positive1Count--;
                return Constants.POSITIVE_1;
            case R.drawable.black_circle:
                negative1Count--;
                return Constants.NEGATIVE_1;
            default:
                return "";
        }
    }

    @Override
    public void reset() {
        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).clearAnimation();;
        }
        positiveXCount = 0;
        negativeXCount = 0;
        positive1Count = 0;
        negative1Count = 0;
        super.reset();
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

    public String getTypeContainedIn() {
        if (positiveXCount > 0)
            return Constants.POSITIVE_X;
        if (negativeXCount > 0)
            return Constants.NEGATIVE_X;
        if (positive1Count > 0)
            return Constants.POSITIVE_1;
        if (negative1Count > 0)
            return Constants.NEGATIVE_1;
        else
            return "";
    }

    public void setOneViewDrawables(String oneViewDrawables) {
        String xViewDrawables = getTypeContainedIn();

        if (xViewDrawables.equals(Constants.POSITIVE_X) && oneViewDrawables.equals(Constants.POSITIVE_1)) {
            drawables = Constants.WHITE_BOX_WHITE_CIRLE;
        } else if (xViewDrawables.equals(Constants.POSITIVE_X) && oneViewDrawables.equals(Constants.NEGATIVE_1)) {
            drawables = Constants.WHITE_BOX_BLACK_CIRLE;
        } else if (xViewDrawables.equals(Constants.NEGATIVE_X) && oneViewDrawables.equals(Constants.POSITIVE_1)) {
            drawables = Constants.BLACK_BOX_WHITE_CIRLE;
        } else {
            drawables = Constants.BLACK_BOX_BLACK_CIRLE;
        }
    }

    public void animateXView(int child, int delay, final int dividend) {
        Log.d(TAG, "animateXView");
        final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(child);
        if (temp == null)
            return;
        AnimationSet animSet = new AnimationSet(false);
        animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.cycle_interpolator));
        //TODO: fix for right
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f);
        scaleAnimation.setDuration(250);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setStartOffset(delay);

        //TODO: add color change
        animSet.addAnimation(scaleAnimation);
        temp.startAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                temp.setImageResource(drawables[dividend]);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    //TODO: check what layout is used so know which side to fly to.
    public void animateOneView(int child, int delay) {
        Log.d(TAG, "animateOneView");
        final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(child);
        if (temp == null)
            return;
        LayoutParams params = (LayoutParams) temp.getLayoutParams();
        AnimationSet animSet = new AnimationSet(false);
        animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.linear_interpolator));

        int childLoc[] = {0, 0};
        temp.getLocationOnScreen(childLoc);
        int parentLoc[] = {0, 0};
        getLocationInWindow(parentLoc);
        //Translate animation coordinates are based on the curresnt object. 0, 0 is the current position of the object
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0 - (params.leftMargin + temp.getWidth()), 0, 0);

        translateAnimation.setDuration(Constants.ANIMATION_DURATION);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setStartOffset(delay);
        animSet.addAnimation(translateAnimation);
        temp.startAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeImageViewType(temp.getId());
                temp.setVisibility(View.GONE);
                if (listener != null) {
                    listener.onAnimationEnd(temp.getId());
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
        void onAnimationStart(int val);
    }

    private LinearOpsGridLayoutListener listener;

    // Assign the listener implementing events interface that will receive the events
    public void onLinearOpsGridLayoutListener(LinearOpsGridLayoutListener listener) {
        Log.d(TAG, "onLinearOpsGridLayoutListener");
        this.listener = listener;
    }
}
