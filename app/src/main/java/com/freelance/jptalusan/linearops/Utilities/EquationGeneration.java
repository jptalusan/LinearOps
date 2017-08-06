package com.freelance.jptalusan.linearops.Utilities;

import android.util.Log;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by JPTalusan on 01/05/2017.
 */

public class EquationGeneration {
    private static final String TAG = "EquationGeneration";

    public static Equation generateEqualityEquation(int level) {
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
            case Constants.LEVEL_1:
                ax = pickRandom(rnd, 2, 9);
                b = ax * pickRandom(rnd, 2, 9);
                ax *= pickRandomSign(rnd);
                b *= pickRandomSign(rnd);
                cx = 0;
                d  = 0;
                generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_1);
                break;
            case Constants.LEVEL_2:
                d = 0;
                int temp = cx - b;
                int absTemp = Math.abs(temp);
                if (ax != 0) {
                    double result = Math.abs(temp / ax);
                    if (temp % ax != 0 || absTemp > Constants.X_MAX || Math.abs(ax) > absTemp || Math.abs(ax) == 1 || result == 1) {
                        generatedEquation = generateEqualityEquation(Constants.LEVEL_2);
                    } else {
                        generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_2);
                    }
                } else {
                    generatedEquation = generateEqualityEquation(Constants.LEVEL_2);
                }
                break;
            case Constants.LEVEL_3:
            case Constants.LEVEL_4:
                tempX = ax - cx;
                temp1 = d - b;
                if (tempX != 0) {
                    double result = Math.abs(temp1 / tempX);
                    if (temp1 % tempX != 0 || Math.abs(tempX) == 1 || temp1 == 0 || result == 1) {
                        generatedEquation = generateEqualityEquation(Constants.LEVEL_4);
                    } else {
                        generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_4);
                    }
                } else {
                    generatedEquation = generateEqualityEquation(Constants.LEVEL_4);
                }
                break;
            case Constants.LEVEL_5:
                double tX = ax - cx;
                double t1 = d - b;
                IntegerAndDecimal id = new IntegerAndDecimal(t1, tX);
                String validity = id.isValid() ? " is" : " is not";
                Log.d(TAG, "Int and Dec: " + id.toString() + validity + " valid.");
                if (tX != 0) {
                    if (!id.isValid() || Math.abs(tX) == 1 || Math.abs(generatedEquation.getX()) == 1) {
                        generatedEquation = generateEqualityEquation(Constants.LEVEL_5);
                    } else {
                        generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_5);
                    }
                } else {
                    generatedEquation = generateEqualityEquation(Constants.LEVEL_5);
                }
                break;
            default:
                generateEqualityEquation(level);
                break;
        }
        System.out.println(generatedEquation);
        return generatedEquation;
    }

    public static int pickRandom(Random rnd, int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int temp = rnd.nextInt((max - min) + 1) + min;
        if (temp != 0)
            return temp;
        else
            return pickRandom(rnd, min, max);
    }

    static int pickRandomSign(Random rnd) {
        return (rnd.nextInt() % 2 == 0) ? -1 : 1;
    }
}