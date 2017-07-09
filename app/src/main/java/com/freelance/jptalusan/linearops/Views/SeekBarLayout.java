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
import android.widget.SeekBar;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Views.ComboSeekBar.ComboSeekBar;

import java.util.List;

//How to expose listener:http://stackoverflow.com/questions/10776764/what-is-the-right-way-to-communicate-from-a-custom-view-to-the-activity-in-which
public class SeekBarLayout extends ConstraintLayout {
    private RelativeLayout icons;
    public ComboSeekBar comboSeekBar;
    private static String TAG = "SeekBarLayout";
    private int resourceId = 0;
    private int tempInt = 0;
    private Dimensions dimensions = new Dimensions();
    private Dimensions iconDimension = new Dimensions();
    private double center = 0;
    private int mSignedMax = 0;

    public SeekBarLayout(@NonNull Context context) {
        super(context);
        initializeViews(context);
    }

    public SeekBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    @Override
    public void invalidate() {
        icons.invalidate();
        comboSeekBar.invalidate();
        super.invalidate();
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.seekbar_with_icons_and_text, this);

        icons = findViewById(R.id.icons);
        comboSeekBar = findViewById(R.id.multislider);

        getViewDimensions();
        this.listener = null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        comboSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(TAG, "---");
                Log.d(TAG, "i: " + i + ", progress: " + seekBar.getProgress());
                Log.d(TAG, "max: " + comboSeekBar.getMax());
                Log.d(TAG, "val: " + (i - (comboSeekBar.getMax() / 2)));
//                if (i == comboSeekBar.getMax()) {
//                    i = comboSeekBar.getMax() - 1;
//                }
                addIcons(i - (comboSeekBar.getMax() / 2));
                if (listener != null)
                    listener.onSeekBarValueChanged(i - (comboSeekBar.getMax() / 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void getViewDimensions() {
        icons.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                icons.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dimensions.width  = icons.getMeasuredWidth();
                dimensions.height = icons.getMeasuredHeight();

                iconDimension.width  = dimensions.width / ((comboSeekBar.getMax()));
                iconDimension.height = dimensions.height;

                Log.d(TAG, iconDimension.toString());
                Log.d(TAG, dimensions.toString());

                center = dimensions.width / 2;
            }
        });
    }

    //can extend this to modify what is added.
    private void addIcons(int val) {
//        Log.d(TAG, "addIcons: " + val);
        icons.removeAllViews();
        if (val > 0) {
            for (int i = 1; i <= val; ++i) {
                ImageView iv = new ImageView(getContext());
                iv.setImageResource(R.drawable.white_circle);
                iv.setLayoutParams(generateParams(i));
                iv.setPadding(2, 0, 2, 0);
                icons.addView(iv);
            }
        } else if (val < 0) {
            for (int i = -1; i >= val; --i) {
                ImageView iv = new ImageView(getContext());
                iv.setImageResource(R.drawable.black_circle);
                iv.setLayoutParams(generateParams(i));
                iv.setPadding(2, 0, 2, 0);
                icons.addView(iv);
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
        } else {
            params.leftMargin = (int)center - params.width / 2;
        }

        return params;
    }

    public void drawResourceOn(int index) {
        icons.removeAllViews();
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(resourceId);
        iv.setLayoutParams(generateParams(index));
        iv.setPadding(2, 0, 2, 0);
        icons.addView(iv);
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setSeekBarMax(int val) {
        comboSeekBar.setMax(val);
    }

    public void setComboSeekBarAdapter(List<String> values) {
        comboSeekBar.setAdapter(values);
    }

    public void setComboSeekBarProgress(int value) {
        comboSeekBar.setProgress(value);
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
        comboSeekBar.setProgress((comboSeekBar.getMax() - 1) / 2);
    }

    public void setComboSeekBarSignedMax(int signedMax) {
        mSignedMax = signedMax;
    }

}
