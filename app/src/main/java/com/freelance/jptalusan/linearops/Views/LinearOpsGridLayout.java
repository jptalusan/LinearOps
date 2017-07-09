package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Utilities.Utilities;

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
    }

    @Override
    public boolean addScaledImage(int imageResource) {
//        Log.d(TAG, "LinearOpsGridLayout:addScaledImage");
//        Log.d(TAG, "child/max" + getChildCount() + "/" + (super.rows * super.cols));
        if (getChildCount() < super.rows * super.cols) {
            LinearOpsImageView linearOpsImageView = new LinearOpsImageView(getContext());
            linearOpsImageView.setLayoutParams(super.generateParams());
            linearOpsImageView.setBackgroundResource(imageResource);
            linearOpsImageView.setValueText(Utilities.getOneOrX(imageResource));
            linearOpsImageView.setType(Utilities.getTypeFromResource(imageResource));
            linearOpsImageView.setPadding(1, 1, 1, 1);
//            linearOpsImageView.setBackgroundResource(R.drawable.image_border);
            addView(linearOpsImageView);

            setImageViewType(imageResource);

            defaultDimensions.height = getChildAt(0).getHeight();
            defaultDimensions.width = getChildAt(0).getWidth();

//            Log.d(TAG, "count: " + getChildCount());

            return true;
        } else {
//            Log.d(TAG, "Full");
            return false;
        }
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
            LinearOpsImageView lastAddedViewType = (LinearOpsImageView)getChildAt(getChildCount() - 1);
            String opposite = Utilities.getOppositeType(lastAddedViewType.getType());
            for (int i = 0; i < getChildCount(); ++i) {
                if (((LinearOpsImageView)getChildAt(i)).getType().equals(opposite)) {

                    cancelOutViews(getChildCount() - 1, i);
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
        Log.d(TAG, "isLayoutUniform");
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
    public void setOneViewDrawables(LinearOpsGridLayout x, LinearOpsGridLayout one, boolean isCorrectSign) {
        String xViewDrawables = x.getTypeContainedIn();
        String oneViewDrawables = one.getTypeContainedIn();
        if (isCorrectSign) {
            if (xViewDrawables.equals(Constants.POSITIVE_X) && oneViewDrawables.equals(Constants.POSITIVE_1)) {
                drawables = Constants.WHITE_BOX_WHITE_CIRLE;
            } else if (xViewDrawables.equals(Constants.POSITIVE_X) && oneViewDrawables.equals(Constants.NEGATIVE_1)) {
                drawables = Constants.WHITE_BOX_BLACK_CIRLE;
            } else if (xViewDrawables.equals(Constants.NEGATIVE_X) && oneViewDrawables.equals(Constants.POSITIVE_1)) {
                drawables = Constants.BLACK_BOX_WHITE_CIRLE;
            } else {
                drawables = Constants.BLACK_BOX_BLACK_CIRLE;
            }
        } else {
            if (xViewDrawables.equals(Constants.POSITIVE_X) && oneViewDrawables.equals(Constants.POSITIVE_1)) {
                drawables = Constants.WHITE_BOX_BLACK_CIRLE;
            } else if (xViewDrawables.equals(Constants.POSITIVE_X) && oneViewDrawables.equals(Constants.NEGATIVE_1)) {
                drawables = Constants.WHITE_BOX_WHITE_CIRLE;
            } else if (xViewDrawables.equals(Constants.NEGATIVE_X) && oneViewDrawables.equals(Constants.POSITIVE_1)) {
                drawables = Constants.BLACK_BOX_BLACK_CIRLE;
            } else {
                drawables = Constants.BLACK_BOX_WHITE_CIRLE;
            }
        }
    }

    public void pulseOneView(int child, int delay) {
        final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(child);
        final Drawable tempResource = temp.getBackground();
        AnimationSet animSet = new AnimationSet(false);
        animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.cycle_interpolator));
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(250);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
//        alphaAnimation.setStartOffset(delay);

        animSet.addAnimation(alphaAnimation);
        animSet.setDuration(250);
        animSet.setStartOffset(delay);
//        animSet.setStartTime(delay);
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
                temp.setBackground(tempResource);
                temp.setText("");
                listener.onAllAnimationsEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void pulseXView(int child, int delay, final int dividend) {
        final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(child);
        AnimationSet animSet = new AnimationSet(false);
        animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.cycle_interpolator));
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(250);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setStartOffset(delay);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f);
        scaleAnimation.setDuration(250);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setStartOffset(delay);

        animSet.addAnimation(alphaAnimation);
        animSet.addAnimation(scaleAnimation);
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
                temp.setBackgroundResource(drawables[dividend]);
                temp.setText("");
                listener.onAllAnimationsEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void animateXView(int child, int delay, final int dividend) {
//        Log.d(TAG, "animateXView, div: " + dividend);
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
                if (listener != null) {
                    listener.onAnimationStart(temp.getId());
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                temp.setBackgroundResource(drawables[dividend]);
                temp.setText("");
                listener.onAllAnimationsEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void animateOneView(int child, int delay) {
//        Log.d(TAG, "animateOneView");
        final LinearOpsImageView temp = (LinearOpsImageView) getChildAt(child);
        if (temp == null)
            return;
        LayoutParams params = (LayoutParams) temp.getLayoutParams();
        AnimationSet animSet = new AnimationSet(false);
        animSet.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.anim.linear_interpolator));

        //Translate animation coordinates are based on the curresnt object. 0, 0 is the current position of the object
        TranslateAnimation translateAnimation;
//        Log.d(TAG, "Side of Layout: " + this.side);
        if (this.side.equals(Constants.RIGHT)) { //Right going to left
            translateAnimation = new TranslateAnimation(0, 0 - (params.leftMargin + temp.getWidth()), 0, 0);
        } else { //Left going to right
            translateAnimation = new TranslateAnimation(0, 0 + (getWidth() + temp.getWidth()), 0, 0);
        }

        translateAnimation.setDuration(Constants.ANIMATION_DURATION);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setStartOffset(delay);
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
        //alphaAnimation.setDuration(500);
        alphaAnimation.setDuration(100);
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
                temp.setVisibility(View.GONE);

                removeImageViewType(temp2.getType());
                temp2.setVisibility(View.GONE);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        redrawLayout();
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

    private void redrawLayout() {
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

    //TODO: How to use when in another file
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
}
