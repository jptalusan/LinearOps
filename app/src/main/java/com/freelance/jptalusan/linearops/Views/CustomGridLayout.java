package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Dimensions;

/**
 * Created by JPTalusan on 30/04/2017.
 */

public class CustomGridLayout extends RelativeLayout {
    private static String TAG = "CustomGridLayout";
    private int rows = 0;
    private int cols = 0;
    private CustomGridLayout customGridLayout = this;
    private Dimensions dimensions = new Dimensions();
    private Dimensions scaledDimensons = new Dimensions();
    private Context context;

    public CustomGridLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomGridLayoutOptions,
                0, 0);

        try {
            rows = a.getInteger(R.styleable.CustomGridLayoutOptions_rows, 0);
            cols = a.getInteger(R.styleable.CustomGridLayoutOptions_cols, 0);
        } finally {
            a.recycle();
        }
        init();
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    private void init() {
        getViewDimensions();
    }

    public void getViewDimensions() {
        customGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                customGridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dimensions.width  = customGridLayout.getMeasuredWidth();
                dimensions.height = customGridLayout.getMeasuredHeight();

                scaledDimensons.width  = dimensions.width  / cols;
                scaledDimensons.height = dimensions.height / rows;

                Log.d(TAG, "dimensions:" + dimensions.toString());
                Log.d(TAG, "scaled:" + scaledDimensons.toString());

//                populateLayoutWithImages();
            }
        });
    }

    public RelativeLayout.LayoutParams generateParams() {
        //(width, height)
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) scaledDimensons.width,
                (int) scaledDimensons.height);

        Log.d(TAG, "generateParams:" + scaledDimensons.toString());
        int rowFactor = getChildCount() / cols;
        int colFactor = getChildCount() % cols;

        double leftMargin = colFactor * scaledDimensons.width;
        double topMargin  = rowFactor * scaledDimensons.height;

        params.topMargin = (int) topMargin;
        params.leftMargin = (int) leftMargin;

        return params;
    }

    public void addScaledImage(int imageResource) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageResource);
        imageView.setLayoutParams(generateParams());
        Log.d(TAG, "imageView: " + imageView.getWidth() + "," + imageView.getHeight());
        addView(imageView);
    }

    private void populateLayoutWithImages() {
        for (int i = 0; i < 5; ++i) {
            addScaledImage(R.mipmap.ic_launcher);
        }
    }

    public void reset() {
    }

}
