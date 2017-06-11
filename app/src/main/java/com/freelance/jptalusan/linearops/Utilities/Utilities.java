package com.freelance.jptalusan.linearops.Utilities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.freelance.jptalusan.linearops.Activities.LinearEqualityActivity;
import com.freelance.jptalusan.linearops.Activities.LinearEqualityActivityLevel2;
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

    //TODO: must fix for level 3
    public static boolean animateObjects(Equation eq, LinearOpsGridLayout left, LinearOpsGridLayout right, int userAnswer, AppCompatActivity act) {
        int absAx = 0;
        int absB  = 0;
        int absUserAnswer    = Math.abs(userAnswer);

        double correctAnswer = eq.getX();

        Log.d(TAG, "eq: " + eq.toString());
        Log.d(TAG, "correct: " + correctAnswer);
        Log.d(TAG, "user: " + userAnswer);

        boolean isCorrectSign = (userAnswer * -1) != correctAnswer;
        if (left.getTypeContainedIn().equals(Constants.POSITIVE_X) ||
                left.getTypeContainedIn().equals(Constants.NEGATIVE_X)) {
            absAx = Math.abs(left.getChildCount());
            absB  = Math.abs(right.getChildCount());
            left.setOneViewDrawables(left, right, isCorrectSign);

        } else {
            absAx = Math.abs(right.getChildCount());
            absB  = Math.abs(left.getChildCount());
            right.setOneViewDrawables(right, left, isCorrectSign);
        }

        if (absUserAnswer == 0) {
            //DEBUG Only //TODO:Remove in release
            if (act instanceof LinearEqualityActivity) {
                ((LinearEqualityActivity)act).setupLayoutForEquation(eq);
            }
            if (act instanceof LinearEqualityActivityLevel2) {
                ((LinearEqualityActivityLevel2)act).setupLayoutForEquation(eq);
            }

            return false;
        }

        if (absUserAnswer > absB) {
            return false;
        }

        if (absAx == absB && absUserAnswer == 1) {
            Log.d(TAG, "absAx == absB && absUserAnswer == 1");
            for (int i = 0; i < absAx; ++i) {
//                Log.e(TAG, "move: \t" + i);
                if (right.getValuesInside() == Constants.ONE) {
                    right.animateOneView(i, 1000 * i);
//                    Log.e(TAG, "scale: \t\t" + i + ", has: 1");
                    left.animateXView(i, 500 * i, absUserAnswer);
                } else {
                    left.animateOneView(i, 1000 * i);
//                    Log.e(TAG, "scale: \t\t" + i + ", has: 1");
                    right.animateXView(i, 500 * i, absUserAnswer);
                }
            }
        } else if (absAx == 1) {
            Log.d(TAG, "absAx == 1");
            for (int i = 0; i < absUserAnswer; ++i) {
//                Log.e(TAG, "move: \t" + i);
                if (right.getValuesInside() == Constants.ONE) {
                    right.animateOneView(i, 0);
                } else {
                    left.animateOneView(i, 0);
                }
            }
//            Log.e(TAG, "scale: \t\t" + 0 + ", has: " + absUserAnswer);
            if (right.getValuesInside() == Constants.ONE) {
                left.animateXView(0, 0, absUserAnswer);
            } else {
                right.animateXView(0, 0, absUserAnswer);
            }
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
//                        Log.e(TAG, "move: \t" + currentChild);
                        if (right.getValuesInside() == Constants.ONE) {
                            right.animateOneView(currentChild, 1000 * i);
                        } else {
                            left.animateOneView(currentChild, 1000 * i);
                        }
                        currentChild++;
                    }
                    Log.e(TAG, "scale: \t\t" + i + ", has: " + absUserAnswer);
                    if (right.getValuesInside() == Constants.ONE) {
                        left.animateXView(i, 500 * i, absUserAnswer);
                    } else {
                        right.animateXView(i, 500 * i, absUserAnswer);
                    }
                } else {
                    for (int j = 0; j < remainingChildren; ++j) {
//                        Log.e(TAG, "move: \t" + currentChild);
                        if (right.getValuesInside() == Constants.ONE) {
                            right.animateOneView(currentChild, 1000 * i);
                        } else {
                            left.animateOneView(currentChild, 1000 * i);
                        }
                        currentChild++;
                    }
                    Log.e(TAG, "scale: \t\t" + i + ", has: " + remainingChildren);
                    if (right.getValuesInside() == Constants.ONE) {
                        left.animateXView(i, 500 * i, remainingChildren);
                    } else {
                        right.animateXView(i, 500 * i, remainingChildren);
                    }
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

    public static boolean dontAnimateObjects(Equation eq, LinearOpsGridLayout left, LinearOpsGridLayout right, int userAnswer, AppCompatActivity act) {
        int absAx = 0;
        int absB  = 0;
        int absUserAnswer    = Math.abs(userAnswer);

        double correctAnswer = eq.getX();

        boolean isCorrectSign = (userAnswer * -1) != correctAnswer;
        if (left.getTypeContainedIn().equals(Constants.POSITIVE_X) ||
                left.getTypeContainedIn().equals(Constants.NEGATIVE_X)) {
            absAx = Math.abs(left.getChildCount());
            absB  = Math.abs(right.getChildCount());
            left.setOneViewDrawables(left, right, isCorrectSign);

        } else {
            absAx = Math.abs(right.getChildCount());
            absB  = Math.abs(left.getChildCount());
            right.setOneViewDrawables(right, left, isCorrectSign);
        }

        if (absUserAnswer == 0) {
            //DEBUG Only //TODO:Remove in release
            if (act instanceof LinearEqualityActivity) {
                ((LinearEqualityActivity)act).setupLayoutForEquation(eq);
            }
            if (act instanceof LinearEqualityActivityLevel2) {
                ((LinearEqualityActivityLevel2)act).setupLayoutForEquation(eq);
            }

            return false;
        }

        if (absUserAnswer > absB) {
            return false;
        }

        if (absAx == absB && absUserAnswer == 1) {
            for (int i = 0; i < absAx; ++i) {
                if (right.getValuesInside() == Constants.ONE) {
                    right.animateOneView(i, 0);
                    left.animateXView(i, 0, absUserAnswer);
                } else {
                    left.animateOneView(i, 0);
                    right.animateXView(i, 0, absUserAnswer);
                }
            }
        } else if (absAx == 1) {
            for (int i = 0; i < absUserAnswer; ++i) {
                if (right.getValuesInside() == Constants.ONE) {
                    right.animateOneView(i, 0);
                } else {
                    left.animateOneView(i, 0);
                }
            }
            if (right.getValuesInside() == Constants.ONE) {
                left.animateXView(0, 0, absUserAnswer);
            } else {
                right.animateXView(0, 0, absUserAnswer);
            }
        } else {
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
                        if (right.getValuesInside() == Constants.ONE) {
                            right.animateOneView(currentChild, 0);
                        } else {
                            left.animateOneView(currentChild, 0);
                        }
                        currentChild++;
                    }
                    if (right.getValuesInside() == Constants.ONE) {
                        left.animateXView(i, 0, absUserAnswer);
                    } else {
                        right.animateXView(i, 0, absUserAnswer);
                    }
                } else {
                    for (int j = 0; j < remainingChildren; ++j) {
                        if (right.getValuesInside() == Constants.ONE) {
                            right.animateOneView(currentChild, 0);
                        } else {
                            left.animateOneView(currentChild, 0);
                        }
                        currentChild++;
                    }
                    if (right.getValuesInside() == Constants.ONE) {
                        left.animateXView(i, 0, remainingChildren);
                    } else {
                        right.animateXView(i, 0, remainingChildren);
                    }
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
}
