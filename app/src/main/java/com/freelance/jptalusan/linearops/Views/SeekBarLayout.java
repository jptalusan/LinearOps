package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Views.ComboSeekBar.ComboSeekBar;

import java.util.ArrayList;
import java.util.List;

//How to expose listener:http://stackoverflow.com/questions/10776764/what-is-the-right-way-to-communicate-from-a-custom-view-to-the-activity-in-which
public class SeekBarLayout extends ConstraintLayout {
    public RelativeLayout icons;
    public RelativeLayout numbers;
    public AppCompatSeekBar comboSeekBar;
    private static String TAG = "SeekBarLayout";
    private int resourceId = 0;
    private int tempInt = 0;
    private Dimensions dimensions = new Dimensions();
    private Dimensions iconDimension = new Dimensions();
    private Dimensions numbersDimension = new Dimensions();
    private double center = 0;
    private int mSignedMax = 0;
    private ArrayList<String> mValues = new ArrayList<>();
    private int tickOffset = 0;

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

                numbersDimension.width  = numbers.getMeasuredWidth() / ((comboSeekBar.getMax()));
                numbersDimension.height = numbers.getMeasuredHeight();

                iconDimension.width  = dimensions.width / ((comboSeekBar.getMax()));
                iconDimension.height = dimensions.height;

                Log.d(TAG, iconDimension.toString());
                Log.d(TAG, dimensions.toString());
                Log.d(TAG, "Numbers: " + numbersDimension.toString());

                tickOffset = icons.getMeasuredWidth() / comboSeekBar.getMax();
                drawNumbers();
                //Log.d(TAG, "max: "  + comboSeekBar.getMax());
                //Log.d(TAG, "width: " + comboSeekBar.getMeasuredWidth());
                //Log.d(TAG, "offset: " + comboSeekBar.getThumbOffset());

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

        int originalValue = val + Constants.ONE_MAX;
        params.leftMargin = (tickOffset * originalValue) - (params.width / 2);

//        if (val > 0) {
//            params.leftMargin = (int)center + params.width / 2 + (params.width * (val - 1));
//        } else if (val < 0) {
//            params.leftMargin = (int)center + params.width / 2 - (params.width * Math.abs(val - 1));
//        } else {
//            params.leftMargin = (int)center - params.width / 2;
//        }
        return params;
    }

    private RelativeLayout.LayoutParams generateNumbersParams(int val) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) numbersDimension.width,
                (int) numbersDimension.height);

//        int thumb = comboSeekBar.getThumb().getMinimumWidth();
//        int originalValue = val + Constants.ONE_MAX;
//        params.leftMargin = (tickOffset * originalValue);

        if (val > 0) { //add some factor since it does not have the '-' symbol.
            params.leftMargin = (int)center + tickOffset + (tickOffset * (val - 1)) + 8;
        } else if (val < 0) {
            params.leftMargin = (int)center + tickOffset - (tickOffset * Math.abs(val - 1));
        } else {
            params.leftMargin = (int)center + 8;
        }

        params.topMargin = 20;

        return params;
    }

    public void drawNumbers() {
        if (mValues == null) {
            return;
        }
        Log.d(TAG, "drawNumbers(): " + mValues.size());
        for (int i = 0; i < mValues.size(); ++i) {
            Log.d(TAG, "inside.");
            AutoResizeTextView tv = new AutoResizeTextView(getContext());
            tv.setText(mValues.get(i));
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                    (int) numbersDimension.width,
//                    (int) numbersDimension.height);
//            tv.setLayoutParams(params);
            tv.setLayoutParams(generateNumbersParams(i));
            Log.d(TAG, "value: " + mValues.get(i) + ", w x h: " + tv.getLayoutParams().width + " x " + tv.getLayoutParams().height);
            numbers.addView(tv);
        }
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
        //comboSeekBar.setAdapter(values);
    }

    public void setValues(ArrayList<String> values) {
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
        comboSeekBar.setProgress(Constants.X_MAX);
    }

    public void setComboSeekBarSignedMax(int signedMax) {
        mSignedMax = signedMax;
    }

    public List<ComboSeekBar.Dot> getSeekBarDots() {
        return null;
    }
/*
    public void setIntervals(ArrayList<String> intervals) {
        mValues = intervals;
        displayIntervals(intervals);
        comboSeekBar.setMax(intervals.size() - 1);
    }

    private void displayIntervals(List<String> intervals) {
        int idOfPreviousInterval = 0;

        if (numbers.getChildCount() == 0) {
            for (String interval : intervals) {
                TextView textViewInterval = createInterval(interval);
                alignTextViewToRightOfPreviousInterval(textViewInterval, idOfPreviousInterval);

                idOfPreviousInterval = textViewInterval.getId();

                numbers.addView(textViewInterval);
            }
        }
    }

    private TextView createInterval(String interval) {
        View textBoxView = LayoutInflater.from(getContext())
                .inflate(R.layout.seekbar_with_interval_labels, null);

        TextView textView = textBoxView
                .findViewById(R.id.textViewInterval);

        textView.setId(View.generateViewId());
        textView.setText(interval);

        return textView;
    }

    private void alignTextViewToRightOfPreviousInterval(TextView textView, int idOfPreviousInterval) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (idOfPreviousInterval > 0) {
            params.addRule(RelativeLayout.RIGHT_OF, idOfPreviousInterval);
        }

        textView.setLayoutParams(params);
    }

    private int WidthMeasureSpec = 0;
    private int HeightMeasureSpec = 0;

    @Override
    protected synchronized void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)    {
        WidthMeasureSpec = widthMeasureSpec;
        HeightMeasureSpec = heightMeasureSpec;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            alignIntervals();

            // We've changed the intervals layout, we need to refresh.
            numbers.measure(WidthMeasureSpec, HeightMeasureSpec);
            numbers.layout(numbers.getLeft(), numbers.getTop(),
                    numbers.getRight(), numbers.getBottom());
        }
    }

    private void alignIntervals() {
        int widthOfSeekbarThumb = getSeekbarThumbWidth();
        int thumbOffset = comboSeekBar.getThumbOffset();//widthOfSeekbarThumb / 2;

        //test
        int jpoffset = comboSeekBar.getWidth() / comboSeekBar.getMax();


        int widthOfSeekbar = comboSeekBar.getWidth();
        Log.d(TAG, "width of seekbar: " + widthOfSeekbar);
        int firstIntervalWidth = numbers.getChildAt(0).getWidth();
        int remainingPaddableWidth = widthOfSeekbar - firstIntervalWidth - widthOfSeekbarThumb;

        int numberOfIntervals = comboSeekBar.getMax();
        int maximumWidthOfEachInterval = remainingPaddableWidth / numberOfIntervals;

        alignFirstInterval(0);
        alignIntervalsInBetween(jpoffset);
        alignLastInterval(0, jpoffset);
    }

    private int getSeekbarThumbWidth() {
        return comboSeekBar.getThumb().getMinimumWidth();
//        return getResources().getDimensionPixelOffset(R.dimen.seekbar_thumb_width);
    }

    private void alignFirstInterval(int offset) {
        TextView firstInterval = (TextView) numbers.getChildAt(0);
        firstInterval.setPadding(offset, 0, 0, 0);
    }

    private void alignIntervalsInBetween(int maximumWidthOfEachInterval) {
        int widthOfPreviousIntervalsText = 0;

        // Don't align the first or last interval.
        for (int index = 1; index < (numbers.getChildCount() - 1); index++) {
            TextView textViewInterval = (TextView) numbers.getChildAt(index);
            int widthOfText = textViewInterval.getWidth();

            // This works out how much left padding is needed to center the current interval.
            int leftPadding = Math.round(maximumWidthOfEachInterval - (widthOfText / 2) - (widthOfPreviousIntervalsText / 2));
            textViewInterval.setPadding(leftPadding, 0, 0, 0);

            widthOfPreviousIntervalsText = widthOfText;
        }
    }

    private void alignLastInterval(int offset, int maximumWidthOfEachInterval) {
        int lastIndex = numbers.getChildCount() - 1;

        TextView lastInterval = (TextView) numbers.getChildAt(lastIndex);
        int widthOfText = lastInterval.getWidth();

        int leftPadding = Math.round(maximumWidthOfEachInterval - widthOfText - offset);
        lastInterval.setPadding(leftPadding, 0, 0, 0);
    }
    */
}
