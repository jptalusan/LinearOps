package com.freelance.jptalusan.linearops.Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Utilities.Utilities;

import java.util.ArrayList;

public class LinearOpsGridLayout extends CustomGridLayout {
    private static String TAG = "LinearOpsGridLayout";
    public int positiveXCount = 0;
    public int negativeXCount = 0;
    public int positive1Count = 0;
    public int negative1Count = 0;
    public Dimensions defaultDimensions = new Dimensions();
    private int[] drawables;
    public String side = "";
    private int objectCount = 0;
    private String lastAddedViewType = "";

    private Paint mTextPaint;
    private Paint mPiePaint;
    private Paint mShadowPaint;

    public LinearOpsGridLayout(Context context) {
        super(context);
    }

    public LinearOpsGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LinearOpsGridLayoutOptions,
                0, 0);

        try {
            side = a.getString(R.styleable.LinearOpsGridLayoutOptions_side);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff101010);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the line separator (only right for now)
        if (side.equals("RIGHT")) {
            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mTextPaint);
        }
    }

    @Override
    public boolean addScaledImage(int imageResource) {
        if (getChildCount() < super.rows * super.cols) {
            /*
            Loop through all children first and see if any is set to INVISIBLE, and use them first
            before creating new one and adding it to the view.

            lastAddedViewType is updated every time a view is "added" so that we can cancel out
            the opposite views correctly.
             */
            for (int i = 0; i < getChildCount(); ++i) {
                LinearOpsImageView tempIv = (LinearOpsImageView) getChildAt(i);
                if (tempIv.getVisibility() == INVISIBLE) {
                    tempIv.setBackgroundResource(imageResource);
                    tempIv.setValueText(Utilities.getOneOrX(imageResource));
                    tempIv.setType(Utilities.getTypeFromResource(imageResource));
                    tempIv.setNumberOfContained(0);
                    lastAddedViewType = Utilities.getTypeFromResource(imageResource);
                    setImageViewType(imageResource);
                    tempIv.setVisibility(VISIBLE);
                    return true;
                }
            }

            //If nothing is found then create a new one from scratch.
            LinearOpsImageView linearOpsImageView = new LinearOpsImageView(getContext());
            linearOpsImageView.setLayoutParams(super.generateParams());
            linearOpsImageView.setBackgroundResource(imageResource);
            linearOpsImageView.setValueText(Utilities.getOneOrX(imageResource));
            linearOpsImageView.setType(Utilities.getTypeFromResource(imageResource));
            linearOpsImageView.setNumberOfContained(0);

            lastAddedViewType = Utilities.getTypeFromResource(imageResource);

//            linearOpsImageView.setPadding(1, 1, 1, 1);
            setImageViewType(imageResource);
            addView(linearOpsImageView);

            return true;
        } else {
            return false;
        }
    }

    private void setImageViewType(int imageResource) {
        switch (imageResource) {
            case R.drawable.white_box:
                positiveXCount++;
                break;
            case R.drawable.black_box:
                negativeXCount++;
                break;
            case R.drawable.white_circle:
                positive1Count++;
                break;
            case R.drawable.black_circle:
                negative1Count++;
                break;
        }
    }

    private void removeImageViewType(String type) {
        switch (type) {
            case Constants.POSITIVE_X:
                positiveXCount--;
                if (positiveXCount < 0)
                    positiveXCount = 0;
                break;
            case Constants.NEGATIVE_X:
                negativeXCount--;
                if (negativeXCount < 0)
                    negativeXCount = 0;
                break;
            case Constants.POSITIVE_1:
                positive1Count--;
                if (positive1Count < 0)
                    positive1Count = 0;
                break;
            case Constants.NEGATIVE_1:
                negative1Count--;
                if (negative1Count < 0)
                    negative1Count = 0;
                break;
            default:
                break;
        }
    }

    public void cancelOutOppositeViewTypes() {
        if (getChildCount() != 0) {
            int indexOfLastAddedType = 0;
            int indexOfOppositeOfLastAdedType = 0;
//            int lastAddedChildIndex = getChildCount() - 1;
//            LinearOpsImageView lastAddedViewType = (LinearOpsImageView)getChildAt(lastAddedChildIndex);
//            String opposite = Utilities.getOppositeType(lastAddedViewType.getType());
            //Loop through all children in reverse and see which has the same type as the last added type and is visible
            String opposite = Utilities.getOppositeType(lastAddedViewType);
            for (int i = getChildCount() - 1; i >= 0; --i) {
                LinearOpsImageView tempIv = (LinearOpsImageView)getChildAt(i);
                if (tempIv.getType().equals(lastAddedViewType)
                        && tempIv.getVisibility() == VISIBLE) {
                    indexOfLastAddedType = i;
                    break;
                }
            }
            //Loop through all children and check which are visible and only then cancel out.
            for (int i = getChildCount() - 1; i >= 0; --i) {
                LinearOpsImageView possibleOppositeChild = (LinearOpsImageView)getChildAt(i);
                if (possibleOppositeChild.getType().equals(opposite)
                        && possibleOppositeChild.getVisibility() == VISIBLE) {
                    indexOfOppositeOfLastAdedType = i;
                    cancelOutViews(indexOfLastAddedType, indexOfOppositeOfLastAdedType);
                    return;
                }
            }
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
        return "LinearOpsGridLayout{" + getValuesInside() +
                ": positiveXCount=" + positiveXCount +
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

    public int getCountOfTypeContainedIn() {
        if (positiveXCount > 0)
            return positiveXCount;
        if (negativeXCount > 0)
            return negativeXCount;
        if (positive1Count > 0)
            return positive1Count;
        if (negative1Count > 0)
            return negative1Count;
        else
            return -1;
    }

    public String getValuesInside() {
        if (positiveXCount > 0 || negativeXCount > 0) {
            return Constants.X;
        }
        if (positive1Count > 0 || negative1Count > 0) {
            return Constants.ONE;
        }
        return "";
    }

    public boolean isLayoutUniform() {
//        Log.d(TAG, "isLayoutUniform");
        if (positiveXCount + negativeXCount + positive1Count == 0) {
            return true;
        }
        if (positiveXCount + negativeXCount + negative1Count == 0) {
            return true;
        }
        if (positiveXCount + positive1Count + negative1Count == 0) {
            return true;
        }
        if (negativeXCount + positive1Count + negative1Count == 0) {
            return true;
        }
        return false;
    }

    public int getImageViewTypeWhenUniform() {
        Log.d(TAG, "getImageViewTypeWhenUniform: " + this.toString());
        if (isLayoutUniform()) {
            if (positiveXCount > 0)
                return R.drawable.white_box;
            if (negativeXCount > 0)
                return R.drawable.black_box;
            if (positive1Count > 0)
                return R.drawable.white_circle;
            if (negative1Count > 0)
                return R.drawable.black_circle;
            else
                return -1;
        }
        return -1;
    }

    //Determine what array of drawables is going to be placed in X drawable after animations
    public void setOneViewDrawables(LinearOpsGridLayout x, LinearOpsGridLayout one, boolean isAnswerPositive) {
        String xViewDrawables = x.getTypeContainedIn();
        String oneViewDrawables = one.getTypeContainedIn();
        Log.d(TAG, "setOneViewDrawables(" + xViewDrawables + "," + oneViewDrawables + ")");

        if (xViewDrawables.equals(Constants.POSITIVE_X) &&
                oneViewDrawables.equals(Constants.POSITIVE_1)) {
            drawables = Constants.WHITE_BOX_WHITE_CIRCLE;
        } else if (xViewDrawables.equals(Constants.NEGATIVE_X) &&
                oneViewDrawables.equals(Constants.NEGATIVE_1)) {
            drawables = Constants.BLACK_BOX_WHITE_CIRCLE;
        } else if (xViewDrawables.equals(Constants.POSITIVE_X) &&
                oneViewDrawables.equals(Constants.NEGATIVE_1)) {
            drawables = Constants.WHITE_BOX_BLACK_CIRCLE;
        } else if (xViewDrawables.equals(Constants.NEGATIVE_X) &&
                oneViewDrawables.equals(Constants.POSITIVE_1)) {
            drawables = Constants.BLACK_BOX_BLACK_CIRCLE;
        }
    }

    private void cancelOutViews(int childOne, int childTwo) {
//        Log.d(TAG, "cancelOutViews: " + childOne + "," + childTwo);
        final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(childOne);
        if (temp == null)
            return;

        final LinearOpsImageView temp2 = (LinearOpsImageView) getChildAt(childTwo);
        if (temp2 == null)
            return;

        AnimationSet animSet = new AnimationSet(false);
        animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.cycle_interpolator));
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
//        alphaAnimation.setDuration(100);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setStartOffset(0);

        animSet.addAnimation(alphaAnimation);
        temp.startAnimation(animSet);
        temp2.startAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null) {
                    listener.onAnimationStart(temp.getId());
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeImageViewType(temp.getType());
                temp.setVisibility(View.INVISIBLE);

                removeImageViewType(temp2.getType());
                temp2.setVisibility(View.INVISIBLE);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
//                        redrawLayout();
                    }
                });

                if (listener != null) {
                    listener.onCancelOutEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void redrawLayout() {
        removeAllViews();
        for (int i = 0; i < positiveXCount; ++i) {
            LinearOpsImageView linearOpsImageView = new LinearOpsImageView(getContext());
            linearOpsImageView.setLayoutParams(super.generateParams());
            linearOpsImageView.setBackgroundResource(R.drawable.white_box);
            linearOpsImageView.setValueText("X");
            linearOpsImageView.setType(Utilities.getTypeFromResource(R.drawable.white_box));
            addView(linearOpsImageView);
        }
        for (int i = 0; i < negativeXCount; ++i) {
            LinearOpsImageView linearOpsImageView = new LinearOpsImageView(getContext());
            linearOpsImageView.setLayoutParams(super.generateParams());
            linearOpsImageView.setBackgroundResource(R.drawable.black_box);
            linearOpsImageView.setValueText("X");
            linearOpsImageView.setType(Utilities.getTypeFromResource(R.drawable.black_box));
            addView(linearOpsImageView);
        }
        for (int i = 0; i < positive1Count; ++i) {
            LinearOpsImageView linearOpsImageView = new LinearOpsImageView(getContext());
            linearOpsImageView.setLayoutParams(super.generateParams());
            linearOpsImageView.setBackgroundResource(R.drawable.white_circle);
            linearOpsImageView.setValueText("1");
            linearOpsImageView.setType(Utilities.getTypeFromResource(R.drawable.white_circle));
            addView(linearOpsImageView);
        }
        for (int i = 0; i < negative1Count; ++i) {
            LinearOpsImageView linearOpsImageView = new LinearOpsImageView(getContext());
            linearOpsImageView.setLayoutParams(super.generateParams());
            linearOpsImageView.setBackgroundResource(R.drawable.black_circle);
            linearOpsImageView.setValueText("1");
            linearOpsImageView.setType(Utilities.getTypeFromResource(R.drawable.black_circle));
            addView(linearOpsImageView);
        }
    }

    public interface LinearOpsGridLayoutListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        // or when data has been loaded
        void onAnimationEnd(int val);
        void onAnimationStart(int val);
        void onCancelOutEnd(); //TODO: add listener too on when image is added
        void onAllAnimationsEnd(); //TODO: Start with count of all objects, subtract each one after successful animation, when 0, throw this so listener in activity can start new eq (restart())
    }

    private LinearOpsGridLayoutListener listener;

    // Assign the listener implementing events interface that will receive the events
    public void onLinearOpsGridLayoutListener(LinearOpsGridLayoutListener listener) {
//        Log.d(TAG, "onLinearOpsGridLayoutListener");
        this.listener = listener;
    }

    public void performCleanup(int correctAnswer) {
        if (getValuesInside().equals(Constants.X)) {
            //Pulse x
            for (int i = 0; i < getChildCount(); ++i) {
                LinearOpsImageView linearOpsImageView = (LinearOpsImageView) getChildAt(i);
                if (linearOpsImageView.getNumberOfContained() == 0 ||
                        (i == (getChildCount() - 1) && linearOpsImageView.getNumberOfContained() > correctAnswer)) {
                    ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(linearOpsImageView,
                            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                            PropertyValuesHolder.ofFloat("scaleY", 1.2f));
                    scaleDown.setDuration(500);

                    scaleDown.setRepeatCount(3);
                    scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

                    scaleDown.start();
                }
            }
        } else {
            for (int i = 0; i < getChildCount(); ++i) { //everything left over
                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(getChildAt(i),
                        PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.2f));
                scaleDown.setDuration(500);

                scaleDown.setRepeatCount(3);
                scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

                scaleDown.start();
            }
        }
    }

    //Animate 1 box - scale
    public void animateStepOne(int numberOfAnimations, final ArrayList<Integer> containedInEach, boolean willAnimate) {

        for (int i = 0; i < numberOfAnimations; ++i) {
            final int fI = i;
            final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(i);
            ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(temp,
                    PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.2f));
            scaleDown.setDuration(500);

            int delayFactor = willAnimate ? 1: 0;

            scaleDown.setRepeatCount(3);
            scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
            scaleDown.setStartDelay(2000 * i * delayFactor);

            scaleDown.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Log.d(TAG, "contained in: " + fI + ", " + containedInEach.get(fI));
                    //TODO: clean this, im lazy so this is just a hack

                    temp.setBackgroundResource(drawables[containedInEach.get(fI)]);

                    temp.setNumberOfContained(containedInEach.get(fI));
                    temp.setText("");
                    listener.onAllAnimationsEnd();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    if (listener != null) {
                        listener.onAnimationStart(temp.getId());
                    }
                }
            });

            scaleDown.start();
        }
    }

    public void animateStepTwo(int startingChild, int numberOfBallsToAnimate, int delay, boolean willAnimate) {
        Log.d(TAG, "numberOfBallsToAnimate: " + numberOfBallsToAnimate + ", starting at: " + startingChild);
        int animatedBalls = 0;
        for (int i = startingChild; i < getChildCount(); ++i) {
            if (getChildAt(i).getVisibility() == VISIBLE &&
                    animatedBalls != numberOfBallsToAnimate) {
                final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(i);
                if (temp == null)
                    return;
                LayoutParams params = (LayoutParams) temp.getLayoutParams();
                AnimationSet animSet = new AnimationSet(false);
                animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                        android.R.anim.linear_interpolator));

                //Translate animation coordinates are based on the current object. 0, 0 is the current position of the object
                TranslateAnimation translateAnimation;
                if (this.side.equals(Constants.RIGHT)) { //Right going to left
                    translateAnimation = new TranslateAnimation(0, 0 - (params.leftMargin + temp.getWidth()), 0, 0);
                } else { //Left going to right
                    translateAnimation = new TranslateAnimation(0, getWidth() + temp.getWidth(), 0, 0);
                }

                int delayFactor = willAnimate ? 1: 0;

                translateAnimation.setDuration(1000);
                translateAnimation.setStartOffset((2000 * delay + 500) * delayFactor);
                translateAnimation.setRepeatCount(0);

                animatedBalls++;

                animSet.addAnimation(translateAnimation);
                temp.startAnimation(animSet);

                animSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (listener != null) {
                            listener.onAnimationStart(temp.getId());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        removeImageViewType(temp.getType());
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
        }
    }
}
