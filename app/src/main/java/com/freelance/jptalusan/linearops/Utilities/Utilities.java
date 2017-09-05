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
        //Should now take into account the empty boxes as well
        Log.d(TAG, "Exceed/match x reset: " + (xCount * Constants.RESET_FACTOR) + " ms");
        return xCount * Constants.RESET_FACTOR;// + 2000; //have to add 2000 for the left over balls
    }

    public static void performCleanup(LinearOpsGridLayout left, LinearOpsGridLayout right, double correctAnswer) {
        left.performCleanup(Math.abs((int) correctAnswer));
        right.performCleanup(Math.abs((int) correctAnswer));
    }

    public boolean animateObjects(Equation eq, final int userAnswer, boolean willAnimate) {
        boolean output = false;
        final LinearOpsGridLayout boxContainer, ballContainer;
        if (l.getValuesInside().equals(Constants.X)) {
            boxContainer = l;
            ballContainer = r;
        } else {
            ballContainer = l;
            boxContainer = r;
        }

        boolean isCorrectSign = (userAnswer * -1) != eq.getX();
        if (l.getTypeContainedIn().equals(Constants.POSITIVE_X) ||
                l.getTypeContainedIn().equals(Constants.NEGATIVE_X)) {
            l.setOneViewDrawables(l, r, isCorrectSign);
        } else {
            r.setOneViewDrawables(r, l, isCorrectSign);
        }

        final int numberOfRemainingBalls = ballContainer.getChildCount();
        ArrayList<Integer> containedInEach = new ArrayList<>();

        int absUserAnswer = Math.abs(userAnswer);
        double absCorrectAnswer = Math.abs(eq.getX());

        if (absUserAnswer > ballContainer.getChildCount()) {
            return false;
        }

        int numberOfBoxesToAnimate, numberOfBallsPerBox;
        if (userAnswer == eq.getX()) {
            Log.d(TAG, "correct.");
            numberOfBoxesToAnimate = boxContainer.getChildCount();
            numberOfBallsPerBox = absUserAnswer;
            for (int i = 0; i < numberOfBoxesToAnimate; ++i) {
                containedInEach.add(numberOfBallsPerBox);
            }
            output = true;
        } else {
            Log.d(TAG, "incorrect.");
            //TODO: Check when this becomes zero (absUserAnswer), i dont think it should ever be zero
            if (absUserAnswer == 0) {
                return false;
            }
            int dividend = numberOfRemainingBalls / absUserAnswer;
            int remainder = numberOfRemainingBalls % absUserAnswer;

            if (remainder != 0) {
                numberOfBoxesToAnimate = dividend + 1;
            } else {
                numberOfBoxesToAnimate = dividend;
            }

            if (numberOfBoxesToAnimate > boxContainer.getChildCount()) {
                numberOfBoxesToAnimate = boxContainer.getChildCount();
            }

            if (remainder != 0) {
                if (absUserAnswer > absCorrectAnswer) {
                    for (int i = 0; i < numberOfBoxesToAnimate - 1; ++i) {
                        containedInEach.add(absUserAnswer);
                    }
                    containedInEach.add(remainder);
                } else {
                    for (int i = 0; i < numberOfBoxesToAnimate; ++i) {
                        containedInEach.add(absUserAnswer);
                    }
                }
            } else {
                for (int i = 0; i < numberOfBoxesToAnimate; ++i) {
                    containedInEach.add(absUserAnswer);
                }
            }
        }

        boxContainer.animateStepOne(numberOfBoxesToAnimate, containedInEach, willAnimate);

        for (int i = 0, startingChild = 0; i < numberOfBoxesToAnimate; ++i) {
            ballContainer.animateStepTwo(startingChild, absUserAnswer, i, willAnimate);
            startingChild += absUserAnswer;
        }

        return output;
    }
}
