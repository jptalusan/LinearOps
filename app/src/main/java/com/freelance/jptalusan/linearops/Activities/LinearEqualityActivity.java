package com.freelance.jptalusan.linearops.Activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Utilities.Equation;
import com.freelance.jptalusan.linearops.Utilities.EquationGeneration;
import com.freelance.jptalusan.linearops.Utilities.Utilities;
import com.freelance.jptalusan.linearops.Views.LinearOpsGridLayout;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.freelance.jptalusan.linearops.Utilities.Utilities.getFactors;

public class LinearEqualityActivity extends AppCompatActivity {
    private static String TAG = "LinearEqualityActivity";
    protected SharedPreferences prefs;
    private int currLevel = 0;
    private Equation eq = new Equation();
    private ActivityLinearEqualityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality);

        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        if (prefs.getBoolean(Constants.FIRST_TIME, true)) {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();
            prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).apply();
        }
        currLevel = prefs.getInt(Constants.LINEAR_EQ_LEVEL, 0);

        eq = EquationGeneration.generateEqualityEquation(currLevel);
        setupLayoutForEquation(eq);

        List<String> points = new ArrayList<>();
        for (int i = Constants.ONE_MIN; i <= Constants.ONE_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setSeekBarMax((Constants.ONE_MAX * 2) + 1);
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.setComboSeekBarProgress(Constants.ONE_MAX);
        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                //Should only be called when layout is in the for ax = b only
                isAnswerCorrect(val);
            }
        });

        binding.seekbar.reset();
    }

    private void setupGrid(LinearOpsGridLayout l, int number) {
        ArrayList<Integer> factors = Utilities.getFactors(Math.abs(number));
        if (factors.size() == 1) {
            l.setRows(1);
            l.setCols(1);
        } else if (factors.size() % 2 != 0) {
            l.setRows(factors.get(factors.size() / 2));
            l.setCols(factors.get(factors.size() / 2));
        } else {
            l.setRows(factors.get(factors.size() / 2));
            l.setCols(factors.get(factors.size() / 2 - 1));
        }
    }

    private void setupLayoutForEquation(Equation equation) {
        final int ax = equation.getAx();
        final int b  = equation.getB();
        final int cx = equation.getCx();
        final int d  = equation.getD();

        Log.d(TAG, equation.toString());

        binding.leftSideGrid.reset();
        binding.rightSideGrid.reset();

        setupGrid(binding.leftSideGrid, ax);
        setupGrid(binding.rightSideGrid, b);

        binding.leftSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.leftSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                switch(currLevel) {
                    case Constants.LEVEL_1:
                        for (int i = 0; i < Math.abs(ax); ++i) {
                            if (ax > 0)
                                binding.leftSideGrid.addScaledImage(R.drawable.white_box);
                            else
                                binding.leftSideGrid.addScaledImage(R.drawable.black_box);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        binding.rightSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.rightSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                switch(currLevel) {
                    case Constants.LEVEL_1:
                        for (int i = 0; i < Math.abs(b); ++i) {
                            if (b > 0)
                                binding.rightSideGrid.addScaledImage(R.drawable.white_circle);
                            else
                                binding.rightSideGrid.addScaledImage(R.drawable.black_circle);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //TODO: move somewhere else for reusability, add layouts as params (context, left, right)
    private boolean animateObjects(int userAnswer, String xType, String oneType) {
        //TODO: will have to refactor for later levels
        int absAx            = Math.abs(eq.getAx());
        int absB             = Math.abs(eq.getB());
        int absUserAnswer    = Math.abs(userAnswer);
        double correctAnswer = eq.getX();

        if (absUserAnswer == 0) {
            //DEBUG Only //TODO:Remove in release
            setupLayoutForEquation(eq);
            return false;
        }

        if (absUserAnswer > absB) {
            return false;
        }

        //TODO: will have to change depending on where 1 is
        Log.e(TAG, "Final drawables: " + xType + " with " + oneType + " inside.");
        binding.leftSideGrid.setOneViewDrawables(oneType);

        if (absAx == absB && absUserAnswer == 1) {
            Log.d(TAG, "absAx == absB && absUserAnswer == 1");
            for (int i = 0; i < absAx; ++i) {
                Log.e(TAG, "move: \t" + i);
                binding.rightSideGrid.animateOneView(i, 1000 * i);
                Log.e(TAG, "scale: \t\t" + i + ", has: 1");
                binding.leftSideGrid.animateXView(i, 500 * i, absUserAnswer);
            }
        } else if (absAx == 1) {
            Log.d(TAG, "absAx == 1");
            for (int i = 0; i < absUserAnswer; ++i) {
                Log.e(TAG, "move: \t" + i);
                binding.rightSideGrid.animateOneView(i, 0);
            }
            Log.e(TAG, "scale: \t\t" + 0 + ", has: " + absUserAnswer);
            binding.leftSideGrid.animateXView(0, 0, absUserAnswer);
        } else {
            Log.d(TAG, "else");
            int attemptToSolve = absB / absUserAnswer;
            int remainingChildren = binding.rightSideGrid.getChildCount(); //TODO: will have to change depending on where 1 is
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
                        binding.rightSideGrid.animateOneView(currentChild, 1000 * i);
                        currentChild++;
                    }
                    Log.e(TAG, "scale: \t\t" + i + ", has: " + absUserAnswer);
                    binding.leftSideGrid.animateXView(i, 500 * i, absUserAnswer);
                } else {
                    for (int j = 0; j < remainingChildren; ++j) {
                        Log.e(TAG, "move: \t" + currentChild);
                        binding.rightSideGrid.animateOneView(currentChild, 1000 * i);
                        currentChild++;
                    }
                    Log.e(TAG, "scale: \t\t" + i + ", has: " + remainingChildren);
                    binding.leftSideGrid.animateXView(i, 500 * i, remainingChildren);
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

    //TODO: Add boolean or prevent seekbar from being used.
    private boolean isAnswerCorrect(int userAnswer) {
        String typesInLeft   = binding.leftSideGrid.getTypeContainedIn();
        String typesInRight  = binding.rightSideGrid.getTypeContainedIn();
        if (animateObjects(userAnswer, typesInLeft, typesInRight)) {
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
