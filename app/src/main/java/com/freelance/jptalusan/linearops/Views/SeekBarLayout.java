package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
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
        comboSeekBar = (ComboSeekBar) findViewById(R.id.multislider);

        getViewDimensions();
        this.listener = null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        comboSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == comboSeekBar.getMax()) {
                    i = comboSeekBar.getMax() - 1;
                }
                addIcons(i - (comboSeekBar.getMax() - 1) / 2);
                if (listener != null)
                    listener.onSeekBarValueChanged(i - (comboSeekBar.getMax() - 1) / 2);
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

                iconDimension.width  = dimensions.width / ((comboSeekBar.getMax() - 1));
                iconDimension.height = dimensions.height;

                center = dimensions.width / 2;
            }
        });
    }

    //can extend this to modify what is added.
    private void addIcons(int val) {
        icons.removeAllViews();
        if (val > 0) {
            for (int i = 1; i <= val; ++i) {
                ImageView iv = new ImageView(getContext());
                iv.setImageResource(resourceId);
                iv.setLayoutParams(generateParams(i));
                icons.addView(iv);
            }
        } else if (val < 0) {
            for (int i = -1; i >= val; --i) {
                ImageView iv = new ImageView(getContext());
                iv.setImageResource(resourceId);
                iv.setLayoutParams(generateParams(i));
                icons.addView(iv);
            }
        } else {

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
}
