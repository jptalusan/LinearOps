package com.freelance.jptalusan.linearops.Utilities;

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
//        Log.d(TAG, "Level at generation: " + level);
        //TODO: fix this so it generates equations without failing (infinite recursion)
        switch(level) {
//            case Constants.LEVEL_2:
//            case Constants.LEVEL_3:
//            case Constants.LEVEL_4:
//                generatedEquation = generateTest(level);
//                break;
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
                        try {
                            Thread.sleep(Constants.RANDOMIZER);
                            generatedEquation = generateEqualityEquation(Constants.LEVEL_2);
                        } catch (InterruptedException e) {

                        }
                    } else {
                        generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_2);
                    }
                } else {
                    try {
                        Thread.sleep(Constants.RANDOMIZER);
                        generatedEquation = generateEqualityEquation(Constants.LEVEL_2);
                    } catch (InterruptedException e) {

                    }
                }
                break;
            case Constants.LEVEL_3:
            case Constants.LEVEL_4:
                tempX = ax - cx;
                temp1 = d - b;
                if (tempX != 0) {
                    double result = Math.abs(temp1 / tempX);
                    if (temp1 % tempX != 0 || Math.abs(tempX) == 1 || temp1 == 0 || result == 1) {
                        try {
                            Thread.sleep(Constants.RANDOMIZER);
                            generatedEquation = generateEqualityEquation(Constants.LEVEL_4);
                        } catch (InterruptedException e) {

                        }
                    } else {
                        generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_4);
                    }
                } else {
                    try {
                        Thread.sleep(Constants.RANDOMIZER);
                        generatedEquation = generateEqualityEquation(Constants.LEVEL_4);
                    } catch (InterruptedException e) {

                    }
                }
                break;
            case Constants.LEVEL_5:
                double tX = ax - cx;
                double t1 = d - b;
                IntegerAndDecimal id = new IntegerAndDecimal(t1, tX);
//                String validity = id.isValid() ? " is" : " is not";
//                Log.d(TAG, "Int and Dec: " + id.toString() + validity + " valid.");
                if (tX != 0) {
                    if (!id.isValid() || Math.abs(tX) == 1 || Math.abs(generatedEquation.getX()) == 1) {
                        try {
                            Thread.sleep(Constants.RANDOMIZER);
                            generatedEquation = generateEqualityEquation(Constants.LEVEL_5);
                        } catch (InterruptedException e) {

                        }
                    } else {
                        generatedEquation = new Equation(ax, b, cx, d, Constants.LEVEL_5);
                    }
                } else {
                    try {
                        Thread.sleep(Constants.RANDOMIZER);
                        generatedEquation = generateEqualityEquation(Constants.LEVEL_5);
                    } catch (InterruptedException e) {

                    }
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

    public static Equation generateTest(int level) {
        Equation output = new Equation();
        long seed = Calendar.getInstance().getTimeInMillis();
        Random rnd = new Random((int)seed);
        double a, b, c, d, cb, ac, db;
        switch (level) {
            case 2:
                a = pickRandom(rnd, 2, 9);
                cb = a * (pickRandom(rnd, 2, 9));
                System.out.println(a + ", " + cb);
                b = pickRandom(rnd, 2, (int) cb - 1);
                c = cb - b;
                b *= pickRandomSign(rnd);
                if (b > 0) {
                    c *= -1;
                }
                output = new Equation(a, b, c, 0, 2);
                System.out.println(output.toString());
                break;
            case 3:
            case 4:
                a = pickRandom(rnd, 3, 9);
                db = a * (pickRandom(rnd, 3, 9));

                b = pickRandom(rnd, 2, (int) db - 2);
                d = db - b;

                c = a;
                //Hopefully not dangerous
                while (a - c == 0) {
                    c = pickRandom(rnd, 2, 7);
                }

                a = a - c;

//                System.out.println("a: " + a + ", db: " + db);
//                System.out.println("c: " + c + ", b: " + b);
                System.out.println("ax: " + (a + c) + ", db: " + (d + b));

                c *= pickRandomSign(rnd);
                if (c > 0) {
                    a *= -1;
                }

                d *= pickRandomSign(rnd);
                if (d > 0) {
                    b *= -1;
                }
                output = new Equation(a, b, c, d, 3);
                System.out.println(output.toString());
                break;
            case 5:
                IntegerAndDecimal iAndD = new IntegerAndDecimal();
                double dec = iAndD.validDecimals.get(pickRandom(rnd, 0, iAndD.validDecimals.size() - 1));

                a = pickRandom(rnd, 3, 9);
                db = a * (pickRandom(rnd, 3, 9)) * dec;
                c = a;
                //Hopefully not dangerous
                while (a - c == 0) {
                    c = pickRandom(rnd, 2, 7);
                }

                b = pickRandom(rnd, 2, (int) db);
                d = db - b;
                a = a - c;

                c *= pickRandomSign(rnd);
                if (c > 0) {
                    a *= -1;
                }

                d *= pickRandomSign(rnd);
                if (d > 0) {
                    b *= -1;
                }

                output = new Equation(a, b, c, d, 5);
//                System.out.println(output.getX());
                System.out.println(output.toString());
//                System.out.println(a + " x " + dec + " = " + (a * dec));
                System.out.println(iAndD.isThisNumberValid((-((b - d)/(a - c)))));
                break;
            default:
                break;

        }
        return output;
    }
}