package com.freelance.jptalusan.linearops.Activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class LinearEqualityActivity extends AppCompatActivity {
    private static String TAG = "Level1Activity";
    protected SharedPreferences prefs;
    private int currLevel = 0;
    private Equation eq;
    private ActivityLinearEqualityBinding binding;
    private int userAnswer = 0;
    private boolean isAnimationDone = false;
    private int numberOfAnimatedX = 0;
    private boolean isDone = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (prefs.getBoolean(Constants.FIRST_TIME, true)) {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();
            prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).commit();
        }

        //DEBUG
        prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).commit();
        currLevel = prefs.getInt(Constants.LINEAR_EQ_LEVEL, 0);

        startLinearOps();

        List<String> points = new ArrayList<>();
        for (int i = Constants.ONE_MIN; i <= Constants.ONE_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setSeekBarMax((Constants.ONE_MAX * 2) + 1);
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.setComboSeekBarProgress(Constants.ONE_MAX);
//        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);
        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                //Should only be called when layout is in the for ax = b only
                userAnswer = val;
            }
        });

        binding.seekbar.reset();

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnswerCorrect(userAnswer);
            }
        });

        binding.rightSideGrid.onLinearOpsGridLayoutListener(new LinearOpsGridLayout.LinearOpsGridLayoutListener() {
            @Override
            public void onAnimationEnd(int val) {
                isAnimationDone = true;
            }

            @Override
            public void onAnimationStart(int val) {
                isAnimationDone = false;
            }

            @Override
            public void onCancelOutEnd() {
            }

            @Override
            public void onAllAnimationsEnd() {
                if (binding.rightSideGrid.isLayoutUniform() &&
                        binding.rightSideGrid.getValuesInside().equals(Constants.X)) {
                    numberOfAnimatedX++;
                    Log.d(TAG, "R: numberOfAnimatedX:" + numberOfAnimatedX);
                    if (numberOfAnimatedX == Math.abs(eq.getAx()) ||
                            eq.getAx() == 1 ||
                            (isDone && areAllXViewsDoneAnimating())
                            ) {
                        isDone = false;
                        //reset
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startLinearOps();
                            }
                        }, 2500);
                        Log.d(TAG, "R:Reset.");
                    }
                }
            }
        });

        binding.leftSideGrid.onLinearOpsGridLayoutListener(new LinearOpsGridLayout.LinearOpsGridLayoutListener() {
            @Override
            public void onAnimationEnd(int val) {
                isAnimationDone = true;
            }

            @Override
            public void onAnimationStart(int val) {
                isAnimationDone = false;
            }

            @Override
            public void onCancelOutEnd() {
            }

            @Override
            public void onAllAnimationsEnd() {
                if (binding.leftSideGrid.isLayoutUniform() &&
                        binding.leftSideGrid.getValuesInside().equals(Constants.X)) {
                    numberOfAnimatedX++;
                    Log.d(TAG, "L: numberOfAnimatedX:" + numberOfAnimatedX);
                    if (numberOfAnimatedX == Math.abs(eq.getAx()) ||
                            eq.getAx() == 1 ||
                            (isDone && areAllXViewsDoneAnimating())
                            ) {
                        isDone = false;
                        //reset
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startLinearOps();
                            }
                        }, 2500);
                        Log.d(TAG, "L:Reset.");
                    }
                }
            }
        });
    }

    private boolean areAllXViewsDoneAnimating() {
        int solution = eq.getB() / userAnswer;
        int remaindr = eq.getB() % userAnswer;

        Log.d(TAG, "what; " + Math.abs(solution) + ", " + Math.abs(remaindr));
        if (remaindr > 0)
            return (Math.abs(solution) + 1) == numberOfAnimatedX;
        else
            return Math.abs(solution) == numberOfAnimatedX;
    }

    private void startLinearOps() {
        eq = EquationGeneration.generateEqualityEquation(currLevel);
        setupLayoutForEquation(eq);
        binding.seekbar.setComboSeekBarProgress(Constants.ONE_MAX);
        numberOfAnimatedX = 0;
        isDone = false;
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

    public void setupLayoutForEquation(Equation equation) {
        final int ax = equation.getAx();
        final int b  = equation.getB();
        final int cx = equation.getCx();
        final int d  = equation.getD();

        Log.d(TAG, equation.toString());

        binding.leftSideGrid.reset();
        binding.rightSideGrid.reset();

        binding.leftSideGrid.side = Constants.LEFT;
        binding.rightSideGrid.side = Constants.RIGHT;

//        setupGrid(binding.leftSideGrid, ax);
//        setupGrid(binding.rightSideGrid, b);
        binding.leftSideGrid.setRows(5);
        binding.leftSideGrid.setCols(4);

        binding.rightSideGrid.setRows(5);
        binding.rightSideGrid.setCols(4);

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
                            if (b > 0) {
                                binding.rightSideGrid.addScaledImage(R.drawable.white_circle);
                                binding.seekbar.setResourceId(R.drawable.white_circle);
                            } else {
                                binding.rightSideGrid.addScaledImage(R.drawable.black_circle);
                                binding.seekbar.setResourceId(R.drawable.black_circle);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //TODO: Add boolean or prevent seekbar from being used after answer?
    private boolean isAnswerCorrect(int userAnswer) {
        isDone = true;
        if (Utilities.animateObjects(eq, binding.leftSideGrid, binding.rightSideGrid, userAnswer, this)) {
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
