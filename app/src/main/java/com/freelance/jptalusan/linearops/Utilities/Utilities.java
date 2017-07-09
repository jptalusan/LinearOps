package com.freelance.jptalusan.linearops.Utilities;

import android.util.Log;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Views.LinearOpsGridLayout;

import java.util.ArrayList;
import java.util.Collections;

public class Utilities {
    private static String TAG = "Utilities";
    private LinearOpsGridLayout l = null;
    private LinearOpsGridLayout r = null;

    public Utilities(LinearOpsGridLayout left, LinearOpsGridLayout right) {
        l = left;
        r = right;
    }

    public static ArrayList<Integer> getFactors(int number) {
        if (number % 2 != 0) {
            number++;
        }
        ArrayList<Integer> out = new ArrayList<>();
        out.add(1);
        out.add(number);
        for(int i = 2; i < number; i++){
            if (number % i == 0)
                out.add(i);
        }

        Collections.sort(out);
        String temp = "";
        for (int i : out) {
            temp += i + ",";
        }
        Log.d(TAG, number + ":" + temp);
        return out;
    }

    public boolean animateObjects(Equation eq, int userAnswer, boolean willAnimate) {
        int absAx;
        int absB;
        int absUserAnswer = Math.abs(userAnswer);

        double correctAnswer = eq.getX();

        boolean isCorrectSign = (userAnswer * -1) != correctAnswer;
        if (l.getTypeContainedIn().equals(Constants.POSITIVE_X) ||
                l.getTypeContainedIn().equals(Constants.NEGATIVE_X)) {
            absAx = Math.abs(l.getChildCount());
            absB  = Math.abs(r.getChildCount());
            l.setOneViewDrawables(l, r, isCorrectSign);

        } else {
            absAx = Math.abs(r.getChildCount());
            absB  = Math.abs(l.getChildCount());
            r.setOneViewDrawables(r, l, isCorrectSign);
        }

        if (absUserAnswer > absB) {
            return false;
        }

        int delayFactor = willAnimate ? 1: 0;

        if (absAx == absB && absUserAnswer == 1) {
            for (int i = 0; i < absAx; ++i) {
                chooseWhichXToAnimate(i, 500 * i * delayFactor, absUserAnswer);
                chooseWhichOneToAnimate(i, 1000 * i * delayFactor);
            }
        } else if (absAx == 1) {
            for (int i = 0; i < absUserAnswer; ++i) {
                chooseWhichOneToAnimate(i, 0);
            }
            chooseWhichXToAnimate(0, 0, absUserAnswer);
        } else {
            int attemptToSolve = absB / absUserAnswer;
            int remainingChildren;
            if (r.getValuesInside().equals(Constants.ONE)) {
                remainingChildren = r.getChildCount();
            } else {
                remainingChildren = l.getChildCount();
            }
            int currentChild = 0;
            int outerLoop;
            if (absB % absUserAnswer != 0) {
                outerLoop = attemptToSolve + 1;
            } else {
                outerLoop = attemptToSolve;
            }

            if (outerLoop > absAx) {
                outerLoop = absAx;
            }

            for (int i = 0; i < outerLoop; ++i) {
                if (remainingChildren > absUserAnswer) {
                    remainingChildren -= absUserAnswer;
                    for (int j = 0; j < absUserAnswer; ++j) {
                        chooseWhichOneToAnimate(currentChild, 1000 * i * delayFactor);
                        currentChild++;
                    }
                    chooseWhichXToAnimate(i, 500 * i * delayFactor, absUserAnswer);
                } else {
                    for (int j = 0; j < remainingChildren; ++j) {
                        chooseWhichOneToAnimate(currentChild, 1000 * i * delayFactor);
                        currentChild++;
                    }
                    chooseWhichXToAnimate(i, 500 * i * delayFactor, remainingChildren);
                }
            }
        }

        //TODO: add actions here after answer is checked
        if (correctAnswer == (double) userAnswer) {
            Log.w(TAG, "correct!");
            return true;
        } else {
            Log.w(TAG, "incorrect");
            return false;
        }
    }

    private void chooseWhichOneToAnimate(int child, int delay) {
        if (r.getValuesInside().equals(Constants.ONE)) {
            r.animateOneView(child, delay);
        } else {
            l.animateOneView(child, delay);
        }
    }

    private void chooseWhichXToAnimate(int child, int delay, int dividend) {
        if (r.getValuesInside().equals(Constants.ONE)) {
            l.animateXView(child, delay, dividend);
        } else {
            r.animateXView(child, delay, dividend);
        }
    }

    public static String getTypeFromResource(int imageResource) {
        switch (imageResource) {
            case R.drawable.white_box:
                return Constants.POSITIVE_X;
            case R.drawable.black_box:
                return Constants.NEGATIVE_X;
            case R.drawable.white_circle:
                return Constants.POSITIVE_1;
            case R.drawable.black_circle:
                return Constants.NEGATIVE_1;
            default:
                return "";
        }
    }

    public static String getOppositeType(String type) {
        switch (type) {
            case Constants.POSITIVE_X:
                return Constants.NEGATIVE_X;
            case Constants.NEGATIVE_X:
                return Constants.POSITIVE_X;
            case Constants.POSITIVE_1:
                return Constants.NEGATIVE_1;
            case Constants.NEGATIVE_1:
                return Constants.POSITIVE_1;
            default:
                return "";
        }
    }

    public static String getOneOrX(int imageResource) {
        switch (imageResource) {
            case R.drawable.white_box:
            case R.drawable.black_box:
                return "X";
            case R.drawable.white_circle:
            case R.drawable.black_circle:
                return "1";
            default:
                return "";
        }
    }

    public static int determineResetPeriodInMillis(LinearOpsGridLayout l, LinearOpsGridLayout r, int userAnswer, Equation eq) {
        int xCount = 0;
        int oneCount = 0;
        int quotient;
        int remainder;
        int mUserAnswer = Math.abs(userAnswer);
        if (l.getValuesInside().equals(Constants.ONE)) {
            oneCount = Math.abs(l.getCountOfTypeContainedIn());
            xCount = Math.abs(r.getCountOfTypeContainedIn());
        } else if (r.getValuesInside().equals(Constants.ONE)) {
            oneCount = Math.abs(r.getCountOfTypeContainedIn());
            xCount = Math.abs(l.getCountOfTypeContainedIn());
        }
        if (userAnswer == 0) { //Empty answer
            return Constants.DEFAULT_RESET;
        }
        //Correct answer
        if (userAnswer == eq.getX()) {
            Log.d(TAG, "Correct reset: " + (Constants.RESET_FACTOR * xCount) + " ms");
            return Constants.RESET_FACTOR * xCount;
        }
        //Answer is greater than number of One's
        if (mUserAnswer > oneCount) {
            Log.d(TAG, "Default reset: Constants.DEFAULT_RESET ms");
            return Constants.DEFAULT_RESET;
        }
        quotient = oneCount / mUserAnswer;
        remainder = (oneCount % mUserAnswer) > 0 ? 1 : 0;
        //Should not exceed number of X's
        if (quotient + remainder >= xCount) {
            Log.d(TAG, "Exceed/match x reset: " + (xCount * Constants.RESET_FACTOR) + " ms");
            return xCount * Constants.RESET_FACTOR;
        } else { //All other answers
            Log.d(TAG, "Computed reset: " + ((quotient + remainder) * Constants.RESET_FACTOR) + " ms");
            return (quotient + remainder) * Constants.RESET_FACTOR;
        }
    }
}
