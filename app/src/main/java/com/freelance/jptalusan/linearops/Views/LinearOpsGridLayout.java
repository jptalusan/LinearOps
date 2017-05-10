package com.freelance.jptalusan.linearops.Views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;

/**
 * Created by JPTalusan on 07/05/2017.
 */

public class LinearOpsGridLayout extends CustomGridLayout {
    private static String TAG = "LinearOpsGridLayout";
    public int positiveXCount = 0;
    public int negativeXCount = 0;
    public int positive1Count = 0;
    public int negative1Count = 0;

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
            linearOpsImageView.setType(setImageViewType(imageResource));
            linearOpsImageView.setImageResource(imageResource);
            addView(linearOpsImageView);
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

    public void moveViews(int dividend) {
        for (int i = 0; i < getChildCount(); ++i) {
            animateView(getChildAt(i), 0);
        }

    }

    //TODO: check what layout is used so know which side to fly to.
    private void animateView(View child, int delay) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        final View temp = child;
        Log.d(TAG, "wxh" + width + " x " + height);
        LayoutParams params = (LayoutParams) child.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofFloat(params.leftMargin, width + child.getWidth());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                // 2
                temp.setTranslationX(value);
            }
        });

        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.setRepeatCount(0);
        animator.setStartDelay(delay);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //TODO: remove view and update values
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
