package com.freelance.jptalusan.linearops.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

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
}
