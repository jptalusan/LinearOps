package com.freelance.jptalusan.linearops.Utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class IntegerAndDecimal {
    private static final String TAG = "EquationGeneration";
    private double mWhole = 0.0;
    private double mDecimal = 0.0;
    private ArrayList<Double> validDecimals = new ArrayList<>(Arrays.asList(
            0.16667, 0.33333, 0.66667, 0.83333, //recurring, thirds, sixths
            0.2, 0.4, 0.6, 0.8, //fourths
            0.25, 0.5, 0.75)); //quarters/fifths/halves

    public IntegerAndDecimal(double divisor, double dividend) {
        Log.d(TAG, "number: " + divisor + "/" + dividend);
        double quotient = Math.abs(divisor / dividend);

        mWhole = (int) quotient;
        mDecimal = Math.round((quotient % 1) * 100000.0) / 100000.0;
    }

    public boolean isValid() {
        return mWhole > 0 && validDecimals.contains(mDecimal);
    }

    @Override
    public String toString() {
        return "Whole: " + mWhole + ", decimal: " + mDecimal;
    }
}
