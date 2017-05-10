package com.freelance.jptalusan.linearops.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by JPTalusan on 01/05/2017.
 */

public class EquationGeneration {
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
            case Constants.LEVEL_4:
                tempX = ax - cx;
                temp1 = d - b;

                System.out.println(ax + ", " + cx);
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

                System.out.println(ax + ", " + cx);
                if (tempX == 1 || temp1 == 0 || tempX == 0) {
                    generatedEquation = generateEqualityEquation(Constants.LEVEL_5);
                } else {
                    generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_5);
                }
                break;
            default:
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
