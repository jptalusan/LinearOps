package com.freelance.jptalusan.linearops.Utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by JPTalusan on 01/05/2017.
 */

public class EquationGeneration {
    private static final String TAG = "EquationGeneration";

    public static Equation generateEqualityEquation(int level) {
        ArrayList<Integer> out = new ArrayList<>();
        long seed = Calendar.getInstance().getTimeInMillis();
        Random rnd = new Random((int)seed);

        int ax = pickRandom(rnd, Constants.X_MIN, Constants.X_MAX);
        int b  = pickRandom(rnd, Constants.ONE_MIN, Constants.ONE_MAX);
        int cx = pickRandom(rnd, Constants.X_MIN, Constants.X_MAX);
        int d  = pickRandom(rnd, Constants.ONE_MIN, Constants.ONE_MAX);

        Equation generatedEquation = new Equation();
        int tempX = 0;
        int temp1 = 0;
        Log.d(TAG, "Level at generation: " + level);
        switch(level) {
            //TODO: Remove 1x?
            case Constants.LEVEL_1:
                cx = 0;
                d  = 0;
                if (b % ax != 0) {
                    generatedEquation = generateEqualityEquation(Constants.LEVEL_1);
                } else {
                    generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_1);
                }
                break;
            case Constants.LEVEL_2:
                d = 0;
                int temp = cx - b;

                if (temp % ax != 0) {
                    generatedEquation = generateEqualityEquation(Constants.LEVEL_2);
                } else {
                    generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_2);
                }
                break;
            case Constants.LEVEL_3:
            case Constants.LEVEL_4: //TODO: Fix this, sometimes no value appearing, due to layout appearing faster than generation of equation
                tempX = ax - cx;
                temp1 = d - b;

                if (tempX != 0) {
                    if (temp1 % tempX != 0 || tempX == 1 || temp1 == 0) {
                        generatedEquation = generateEqualityEquation(Constants.LEVEL_4);
                    } else {
                        generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_4);
                    }
                }
                break;
            //TODO: Must fix level 5 generation, it is wacked, i think only ~0.5, 0.333, 0.25, 0.2 and 0.1666 are allowed (how to check?)
            //TODO: i think the x = 1 must be removed here
            case Constants.LEVEL_5:
                tempX = ax - cx;
                temp1 = d - b;
                double tX = ax - cx;
                double t1 = d - b;
                IntegerAndDecimal id = new IntegerAndDecimal(t1, tX);
                String validity = id.isValid() ? " is" : " is not";
                Log.d(TAG, "Int and Dec: " + id.toString() + validity + " valid.");
                if (!id.isValid()) {
                    generatedEquation = generateEqualityEquation(Constants.LEVEL_5);
                } else {
                    generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_5);
                }
                break;
            default:
                generateEqualityEquation(level);
                break;
        }
        return generatedEquation;
    }

    private static int pickRandom(Random rnd, int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int temp = rnd.nextInt((max - min) + 1) + min;
        if (temp != 0)
            return temp;
        else
            return pickRandom(rnd, min, max);
    }
}

class IntegerAndDecimal {
    private static final String TAG = "EquationGeneration";
    double mWhole = 0.0;
    double mDecimal = 0.0;
    ArrayList<Double> validDecimals = new ArrayList<>(Arrays.asList(
            0.16667, 0.33333, 0.66667, 0.83333, //recurring, thirds, sixths
            0.2, 0.4, 0.6, 0.8, //fourths
            0.25, 0.5, 0.75)); //quarters/fifths/halves

    IntegerAndDecimal(double divisor, double dividend) {
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
