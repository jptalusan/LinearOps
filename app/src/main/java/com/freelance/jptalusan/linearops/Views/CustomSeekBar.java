package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Dimensions;

import io.apptik.widget.MultiSlider;

//How to expose listener:http://stackoverflow.com/questions/10776764/what-is-the-right-way-to-communicate-from-a-custom-view-to-the-activity-in-which
//TODO: Expose listener for seekbar value when changed so user just needs to use that instead
public class CustomSeekBar extends ConstraintLayout {
    private RelativeLayout icons, numbers;
    private MultiSlider multislider;
    private static String TAG = "CustomSeekBar";
    private MultiSlider.Thumb correctAnswerThumb;
    private MultiSlider.Thumb dummyThumb;
    private int resourceId = R.mipmap.ic_launcher;
    private int tempInt = 0;
    private Dimensions dimensions = new Dimensions();

    public CustomSeekBar(@NonNull Context context) {
        super(context);
        initializeViews(context);
    }

    public CustomSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.seekbar_with_icons_and_text, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        icons = (RelativeLayout)findViewById(R.id.icons);
        numbers = (RelativeLayout)findViewById(R.id.numbers);
        multislider = (MultiSlider) findViewById(R.id.multislider);
        getViewDimensions();

        correctAnswerThumb = multislider.new Thumb();
        dummyThumb = multislider.new Thumb();
        dummyThumb.setEnabled(false);
        dummyThumb.setInvisibleThumb(true);
        multislider.addThumb(dummyThumb);
        multislider.getThumb(0).setRange(null);
        multislider.getThumb(1).setRange(null);

        multislider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                addIcons(value);
            }
        });
    }

    private void getViewDimensions() {
        icons.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                icons.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dimensions.width  = icons.getMeasuredWidth();
                dimensions.height = icons.getMeasuredHeight();
            }
        });
    }

    //TODO: fix equation (same as algeops)
    private void addIcons(int val) {
        if (val != tempInt) {
            tempInt = val;
            icons.removeAllViews();
            for (int i = 0; i < val; ++i) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        (int) dimensions.width/multislider.getMax(),
                        (int) dimensions.height);
                params.leftMargin = params.width * i;
                params.topMargin  = 0;

                Log.d(TAG, dimensions.toString());
                ImageView iv = new ImageView(getContext());
                iv.setImageResource(resourceId);
                iv.setLayoutParams(params);
                icons.addView(iv);
            }
        }
    }

    public void addNumbers() {
        int max = multislider.getMax();
        for (int i = 0; i < max; ++i) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    (int) dimensions.width/multislider.getMax(),
                    (int) dimensions.height);
            params.leftMargin = params.width * i;
            params.topMargin  = 0;

            TextView tv = new TextView(getContext());
            tv.setText(Integer.toString(i));
            tv.setLayoutParams(params);
            numbers.addView(tv);
        }
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setSeekBarMax(int val) {
        multislider.setMax(val, true, true);
        invalidate();
        requestLayout();
    }
}
