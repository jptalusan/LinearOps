package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;

import java.util.ArrayList;

//How to expose listener:http://stackoverflow.com/questions/10776764/what-is-the-right-way-to-communicate-from-a-custom-view-to-the-activity-in-which
public class SeekBarLayout extends ConstraintLayout {
    public RelativeLayout icons;
    public RelativeLayout numbers;
    public AppCompatSeekBar comboSeekBar;
    private TextView score;
    private static String TAG = "SeekBarLayout";
    private int resourceId = 0;
    private int tempInt = 0;
    private Dimensions dimensions = new Dimensions();
    private Dimensions mainFrameDimensions = new Dimensions();
    private Dimensions iconDimension = new Dimensions();
    private Dimensions numbersDimension = new Dimensions();
    private double center = 0;
    private ArrayList<String> mValues = new ArrayList<>();

    private FrameLayout boundaryLayout, mainFrameLayout;
    private View verticalLine;

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
        numbers.invalidate();
        super.invalidate();
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.seekbar_with_icons_and_text, this);

        icons = findViewById(R.id.icons);
        numbers = findViewById(R.id.numbers);
        comboSeekBar = findViewById(R.id.multislider);
        boundaryLayout = findViewById(R.id.boundaryFrameLayout);
        mainFrameLayout = findViewById(R.id.mainFrameLayout);
        verticalLine = findViewById(R.id.verticalLine);
        score = findViewById(R.id.score);

        getViewDimensions();
        this.listener = null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        comboSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                Log.e(TAG, "---");
//                Log.d(TAG, "i: " + i + ", progress: " + seekBar.getProgress());
//                Log.d(TAG, "max: " + comboSeekBar.getMax());
                Log.d(TAG, "val: " + (i - (comboSeekBar.getMax() / 2)));
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
                mainFrameDimensions.width = mainFrameLayout.getMeasuredWidth();
                mainFrameDimensions.height = mainFrameLayout.getMeasuredHeight();

                Log.d(TAG, "main: " + mainFrameDimensions);

                dimensions.width  = icons.getMeasuredWidth();
                dimensions.height = icons.getMeasuredHeight();

                numbersDimension.width  = numbers.getMeasuredWidth() / ((comboSeekBar.getMax()));
                numbersDimension.height = numbers.getMeasuredHeight();

                iconDimension.width  = dimensions.width / ((comboSeekBar.getMax()));
                iconDimension.height = dimensions.height;

                //TODO: chnge these?

//                center = dimensions.width / 2;

                numbers.removeAllViews();
                drawNumbers();
            }
        });
    }

    public void drawOnBoundaryLayout(Rect r) {
        verticalLine.setVisibility(VISIBLE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                5, ViewGroup.LayoutParams.MATCH_PARENT);

        params.leftMargin = r.right + ((r.right - r.left) / 2);
        verticalLine.setLayoutParams(params);
    }

    public void drawIconOnRect(int val) {
        icons.removeAllViews();
        Log.d(TAG, "drawIcon: " + val);
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(generateParams(val));
        iv.setPadding(2, 0, 2, 0);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (val > 0) {
            iv.setImageResource(R.drawable.white_circle);
        } else if (val < 0) {
            iv.setImageResource(R.drawable.black_circle);
        }
        icons.addView(iv);
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
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                icons.addView(iv);
            }
        } else if (val < 0) {
            for (int i = -1; i >= val; --i) {
                ImageView iv = new ImageView(getContext());
                iv.setImageResource(R.drawable.black_circle);
                iv.setLayoutParams(generateParams(i));
                iv.setPadding(2, 0, 2, 0);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                icons.addView(iv);
            }
        }
    }

    private RelativeLayout.LayoutParams generateParams(int val) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (iconDimension.width * 1.0),
                (int) (iconDimension.height * 1.0));

        int tickOffset = icons.getMeasuredWidth() / comboSeekBar.getMax();

        int center = (int) (iconDimension.width / 2);
        int originalValue = val + Constants.ONE_MAX;

        Log.d(TAG, "originalValue: " + originalValue);
        if (originalValue < 9) {
            params.leftMargin = (tickOffset * originalValue) - center + (int)iconDimension.width / 2;
        } else {
            params.leftMargin = (tickOffset * originalValue) - center - (int)iconDimension.width / 2;
        }
        return params;
    }

    private RelativeLayout.LayoutParams generateNumbersParams(int val) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) mainFrameDimensions.width,
                (int) mainFrameDimensions.height);

        int tickOffset = (numbers.getMeasuredWidth() - 40) / comboSeekBar.getMax();

        int center = (numbers.getMeasuredWidth() - 40) / 2;
        if (val == 0) {
            params.leftMargin = center + 8;
        } else if (val > 0) { //add some factor since it does not have the '-' symbol.
            params.leftMargin = center + tickOffset + (tickOffset * (val - 1)) + 8;
        } else if (val < 0) {
            params.leftMargin = center + tickOffset - (tickOffset * Math.abs(val - 1)) - 8;
        }

        params.topMargin = 20;

        return params;
    }

    public void drawNumbers() {
        if (mValues == null) {
            return;
        }
        for (int i = 0; i < mValues.size(); ++i) {
            AutoResizeTextView tv = new AutoResizeTextView(getContext());
            tv.setEms(8);
            tv.setText(mValues.get(i));
            tv.setLayoutParams(generateNumbersParams(i - Constants.ONE_MAX));
            numbers.addView(tv);
        }
    }

    public void drawResourceOn(Rect r) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) numbersDimension.width, (int) numbersDimension.height);

        params.setMargins(r.right - (r.right - r.left) - 16, 0, 0 , 0);

        ImageView iv = new ImageView(getContext());
        iv.setImageResource(resourceId);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setTag("divider");
        numbers.addView(iv, params);
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setValues(ArrayList<String> values) {
        numbers.removeAllViews();
        mValues = values;
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
        score.setText("Score: 0/0");
        comboSeekBar.setProgress(Constants.X_MAX);
        verticalLine.setVisibility(GONE);
    }

    public void updateScore(int newScore, int total) {
        score.setText("Score: " + newScore + "/" + total);
    }
}
