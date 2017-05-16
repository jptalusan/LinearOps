package com.freelance.jptalusan.linearops.Utilities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.freelance.jptalusan.linearops.Activities.LinearEqualityActivity;
import com.freelance.jptalusan.linearops.Activities.LinearEqualityActivityWithButtons;
import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Views.LinearOpsGridLayout;

import java.util.ArrayList;
import java.util.Collections;

public class Utilities {
    private static String TAG = "Utilities";
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

    //TODO: Fix for other levels
    public static boolean animateObjects(Equation eq, LinearOpsGridLayout left, LinearOpsGridLayout right, int userAnswer, AppCompatActivity act) {
        //TODO: will have to refactor for later levels
        int absAx            = Math.abs(eq.getAx());
        int absB             = Math.abs(eq.getB());
        int absUserAnswer    = Math.abs(userAnswer);
        double correctAnswer = eq.getX();
        String xType         = left.getTypeContainedIn();
        String oneType       = right.getTypeContainedIn();

        if (absUserAnswer == 0) {
            //DEBUG Only //TODO:Remove in release
            if (act instanceof LinearEqualityActivity) {
                ((LinearEqualityActivity)act).setupLayoutForEquation(eq);
            }
            if (act instanceof LinearEqualityActivityWithButtons) {
                ((LinearEqualityActivityWithButtons)act).setupLayoutForEquation(eq);
            }

            return false;
        }

        if (absUserAnswer > absB) {
            return false;
        }

        //TODO: will have to change depending on where 1 is
        Log.e(TAG, "Final drawables: " + xType + " with " + oneType + " inside.");
        left.setOneViewDrawables(oneType);

        if (absAx == absB && absUserAnswer == 1) {
            Log.d(TAG, "absAx == absB && absUserAnswer == 1");
            for (int i = 0; i < absAx; ++i) {
                Log.e(TAG, "move: \t" + i);
                right.animateOneView(i, 1000 * i);
                Log.e(TAG, "scale: \t\t" + i + ", has: 1");
                left.animateXView(i, 500 * i, absUserAnswer);
            }
        } else if (absAx == 1) {
            Log.d(TAG, "absAx == 1");
            for (int i = 0; i < absUserAnswer; ++i) {
                Log.e(TAG, "move: \t" + i);
                right.animateOneView(i, 0);
            }
            Log.e(TAG, "scale: \t\t" + 0 + ", has: " + absUserAnswer);
            left.animateXView(0, 0, absUserAnswer);
        } else {
            Log.d(TAG, "else");
            int attemptToSolve = absB / absUserAnswer;
            int remainingChildren = right.getChildCount(); //TODO: will have to change depending on where 1 is
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
                        Log.e(TAG, "move: \t" + currentChild);
                        right.animateOneView(currentChild, 1000 * i);
                        currentChild++;
                    }
                    Log.e(TAG, "scale: \t\t" + i + ", has: " + absUserAnswer);
                    left.animateXView(i, 500 * i, absUserAnswer);
                } else {
                    for (int j = 0; j < remainingChildren; ++j) {
                        Log.e(TAG, "move: \t" + currentChild);
                        right.animateOneView(currentChild, 1000 * i);
                        currentChild++;
                    }
                    Log.e(TAG, "scale: \t\t" + i + ", has: " + remainingChildren);
                    left.animateXView(i, 500 * i, remainingChildren);
                }
            }
        }

        //TODO: add actions here after answer is checked
        Log.d(TAG, "correct/user answ: " + correctAnswer + "/" + userAnswer);
        if (correctAnswer == (double) userAnswer) {
            Log.w(TAG, "correct!");
            return true;
        } else {
            Log.w(TAG, "incorrect");
            return false;
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
}
