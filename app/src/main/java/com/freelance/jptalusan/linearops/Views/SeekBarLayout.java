package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Views.ComboSeekBar.ComboSeekBar;

import static android.R.attr.value;

//How to expose listener:http://stackoverflow.com/questions/10776764/what-is-the-right-way-to-communicate-from-a-custom-view-to-the-activity-in-which
//TODO: Expose listener for seekbar value when changed so user just needs to use that instead
public class SeekBarLayout extends ConstraintLayout {
    private RelativeLayout icons, numbers;
    public ComboSeekBar comboSeekBar;
    private static String TAG = "CustomSeekBar";
    private int resourceId = R.mipmap.ic_launcher;
    private int tempInt = 0;
    private Dimensions dimensions = new Dimensions();
    private Dimensions iconDimension = new Dimensions();
    private double center = 0;

    public SeekBarLayout(@NonNull Context context) {
        super(context);
        initializeViews(context);
    }

    public SeekBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.seekbar_with_icons_and_text, this);

        icons = (RelativeLayout)findViewById(R.id.icons);
        numbers = (RelativeLayout)findViewById(R.id.numbers);
        comboSeekBar = (ComboSeekBar) findViewById(R.id.multislider);

        getViewDimensions();

//        reset();

        this.listener = null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.

        comboSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addIcons(value);
                if (listener != null)
                    listener.onSeekBarValueChanged(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

                iconDimension.width  = dimensions.width / (comboSeekBar.getMax() * 2);
                iconDimension.height = dimensions.height;

                center = dimensions.width / 2;
            }
        });
    }

    //can extend this to modify what is added.
    private void addIcons(int val) {
        if (val != tempInt) {
            tempInt = val;
            icons.removeAllViews();
            if (val > 0) {
                for (int i = 1; i <= val; ++i) {
                    ImageView iv = new ImageView(getContext());
                    iv.setImageResource(resourceId);
                    iv.setLayoutParams(generateParams(i));
                    icons.addView(iv);
                }
            } else if (val < 0) {
                for (int i = val; i < 0; ++i) {
                    ImageView iv = new ImageView(getContext());
                    iv.setImageResource(resourceId);
                    iv.setLayoutParams(generateParams(i));
                    icons.addView(iv);
                }
            }
        }
    }

    private RelativeLayout.LayoutParams generateParams(int val) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) iconDimension.width,
                (int) iconDimension.height);
        if (val > 0) {
            params.leftMargin = (int)center + params.width / 2 + (params.width * (val - 1));
        } else if (val < 0) {
            params.leftMargin = (int)center + params.width / 2 - (params.width * Math.abs(val - 1));
        }

        return params;
    }

    //TODO: Why is this always being called repeatedly
    //TODO: Any way to extend the drawing of views to outside the layout? (even with clipping)
    public void addNumbers() {
        Log.d(TAG, "addNumbers()");
        for (int i = 0; i <= comboSeekBar.getMax(); ++i) {
            //TODO: since i can be 0, change this
            if (i != 0) {
                AutoResizeTextView tv = new AutoResizeTextView(getContext());
                tv.setText(Integer.toString(i));
                tv.setMinTextSize((float) iconDimension.height * 0.8f);
                //TODO: check if gravity.right is better (though not according to android)
                tv.setGravity(Gravity.END);
                tv.setLayoutParams(generateParams(i));
                numbers.addView(tv);
            }
        }
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setSeekBarMax(int val) {
        comboSeekBar.setMax(val);
        invalidate();
        requestLayout();
    }

    public interface SeekbarChangeValueListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        // or when data has been loaded
        void onSeekBarValueChanged(int val);
    }

    private SeekbarChangeValueListener listener;

    // Assign the listener implementing events interface that will receive the events
    public void setSeekBarChangeValueListener(SeekbarChangeValueListener listener) {
        this.listener = listener;
    }

    public void reset() {
        comboSeekBar.setProgress(comboSeekBar.getMax() / 2);
    }
}
