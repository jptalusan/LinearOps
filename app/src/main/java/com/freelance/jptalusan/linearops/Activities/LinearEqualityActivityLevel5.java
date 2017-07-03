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
import com.freelance.jptalusan.linearops.Utilities.Utilities;
import com.freelance.jptalusan.linearops.Views.LinearOpsGridLayout;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityLevelFiveBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JPTalusan on 21/05/2017.
 */

public class LinearEqualityActivityLevel5 extends AppCompatActivity {
    private static String TAG = "Level5Activity";
    protected SharedPreferences prefs;
    private int currLevel = 0;
    private ActivityLinearEqualityLevelFiveBinding binding;
    private Equation eq;
    private boolean canUseButtons = true;
    private int userAnswer = 0;
    private int numberOfAnimatedX = 0;
    private boolean isDone = false;
    private int[] fractionValues = {1, 2, 3, 4, 5, 6};
    int fractionCounter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality_level_five);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (prefs.getBoolean(Constants.FIRST_TIME, true)) {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();
            prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).commit();
        }

        //DEBUG
        prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_5).commit();
        currLevel = prefs.getInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_5);

        startLinearOps();
        setupSeekbarValues(0);
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);
        binding.seekbar.setVisibility(View.GONE);
        binding.checkButton.setVisibility(View.GONE);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                userAnswer = val;
            }
        });

        binding.seekbar.reset();

        binding.whiteBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canUseButtons)
                    addImagesToBothSides(R.drawable.white_box);
            }
        });

        binding.blackBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canUseButtons)
                    addImagesToBothSides(R.drawable.black_box);
            }
        });

        binding.whiteCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canUseButtons)
                    addImagesToBothSides(R.drawable.white_circle);
            }
        });

        binding.blackCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canUseButtons)
                    addImagesToBothSides(R.drawable.black_circle);
            }
        });

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewAbility(false);
                isAnswerCorrect(userAnswer);
//                int temp = Utilities.determineResetPeriodInMillis(
//                        binding.leftSideGrid,
//                        binding.rightSideGrid,
//                        userAnswer,
//                        eq);
                int temp = Constants.DEFAULT_RESET;
                Log.d(TAG, "Reset in: " + temp + " milliseconds.");
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startLinearOps();
                    }
                }, temp);
            }
        });

        binding.increaseFractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fractionCounter == 5)
                    fractionCounter = 0;
                else
                    fractionCounter++;
                Log.d(TAG, "Fraction: " + fractionCounter);
            }
        });

        binding.decreaseFractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fractionCounter == 0)
                    fractionCounter = 5;
                else
                    fractionCounter--;
                Log.d(TAG, "Fraction: " + fractionCounter);
            }
        });

        binding.rightSideGrid.onLinearOpsGridLayoutListener(new LinearOpsGridLayout.LinearOpsGridLayoutListener() {
            @Override
            public void onAnimationEnd(int val) {
                canUseButtons = true;
            }

            @Override
            public void onAnimationStart(int val) {
                canUseButtons = false;
            }

            @Override
            public void onCancelOutEnd() {
                Log.d(TAG, binding.rightSideGrid.toString());
                if (areLayoutsReady()) {
                    Log.d(TAG, "can use seekbar.");
                    binding.seekbar.getViewDimensions();
                    binding.seekbar.setVisibility(View.VISIBLE);
                    binding.checkButton.setVisibility(View.VISIBLE);
                    if (binding.rightSideGrid.getValuesInside().equals(Constants.ONE)) {
                        binding.seekbar.setResourceId(binding.rightSideGrid.getImageViewTypeWhenUniform());
                    } else {
                        binding.seekbar.setResourceId(binding.leftSideGrid.getImageViewTypeWhenUniform());
                    }
                }
                canUseButtons = true;
            }

            @Override
            public void onAllAnimationsEnd() {
            }
        });

        binding.leftSideGrid.onLinearOpsGridLayoutListener(new LinearOpsGridLayout.LinearOpsGridLayoutListener() {
            @Override
            public void onAnimationEnd(int val) {
                canUseButtons = true;
            }

            @Override
            public void onAnimationStart(int val) {
                canUseButtons = false;
            }

            @Override
            public void onCancelOutEnd() {
                Log.d(TAG, binding.leftSideGrid.toString());
                if (areLayoutsReady()) {
                    Log.d(TAG, "can use seekbar.");
                    binding.seekbar.getViewDimensions();
                    binding.seekbar.setVisibility(View.VISIBLE);
                    binding.checkButton.setVisibility(View.VISIBLE);
                    if (binding.rightSideGrid.getValuesInside().equals(Constants.ONE)) {
                        binding.seekbar.setResourceId(binding.rightSideGrid.getImageViewTypeWhenUniform());
                    } else {
                        binding.seekbar.setResourceId(binding.leftSideGrid.getImageViewTypeWhenUniform());
                    }
                }
                canUseButtons = true;
            }

            @Override
            public void onAllAnimationsEnd() {
            }
        });
    }

    //TODO: Bind correctly to buttons
    private void setupSeekbarValues(int centerValue) {
        List<String> points = new ArrayList<>();
        for (int i = Constants.X_MIN; i <= Constants.X_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setSeekBarMax(Constants.X_MAX * 2 + 1);
        binding.seekbar.setComboSeekBarAdapter(points);
    }
    private void startLinearOps() {
        Log.d(TAG, "startLinearOps: " + currLevel);
        do {
//            eq = EquationGeneration.generateEqualityEquation(currLevel);
            eq = new Equation(3, 2, -7, 0, 5);
        } while (eq.toString().equals("FAILED"));
        setupLayoutForEquation(eq);
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        numberOfAnimatedX = 0;
        isDone = false;
        binding.seekbar.setVisibility(View.GONE);
        binding.checkButton.setVisibility(View.GONE);
        setViewAbility(true);
        fractionCounter = 0;
    }

    private void setViewAbility(boolean enabled) {
        binding.seekbar.comboSeekBar.setEnabled(enabled);
        binding.whiteBoxButton.setEnabled(enabled);
        binding.blackBoxButton.setEnabled(enabled);
        binding.whiteCircleButton.setEnabled(enabled);
        binding.blackCircleButton.setEnabled(enabled);
        binding.checkButton.setEnabled(enabled);
//        binding.decreaseFractionButton.setEnabled(enabled);
//        binding.increaseFractionButton.setEnabled(enabled);
    }

    private boolean areLayoutsReady() {
        if (binding.leftSideGrid.isLayoutUniform()) {
            if (binding.rightSideGrid.isLayoutUniform()) {
                String leftSideType = binding.leftSideGrid.getTypeContainedIn();
                String rightSideType = binding.rightSideGrid.getTypeContainedIn();
                return true;
            }
        }
        return false;
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

        //Log.d(TAG, equation.toString());
        System.out.println(TAG + ", " + equation);
        System.out.println(TAG + ", " + equation.getX());

        binding.leftSideGrid.reset();
        binding.rightSideGrid.reset();

        binding.leftSideGrid.side = Constants.LEFT;
        binding.rightSideGrid.side = Constants.RIGHT;

//        setupGrid(binding.leftSideGrid, 25);
        binding.leftSideGrid.setRows(6);
        binding.leftSideGrid.setCols(5);

        binding.rightSideGrid.setRows(6);
        binding.rightSideGrid.setCols(5);
//        setupGrid(binding.rightSideGrid, 25);

        binding.leftSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.leftSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                switch(currLevel) {
                    case Constants.LEVEL_5:
                        for (int i = 0; i < Math.abs(ax); ++i) {
                            if (ax > 0)
                                binding.leftSideGrid.addScaledImage(R.drawable.white_box);
                            else
                                binding.leftSideGrid.addScaledImage(R.drawable.black_box);
                        }
                        for (int i = 0; i < Math.abs(b); ++i) {
                            if (b > 0)
                                binding.leftSideGrid.addScaledImage(R.drawable.white_circle);
                            else
                                binding.leftSideGrid.addScaledImage(R.drawable.black_circle);
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
                    case Constants.LEVEL_5:
                        for (int i = 0; i < Math.abs(cx); ++i) {
                            if (cx > 0)
                                binding.rightSideGrid.addScaledImage(R.drawable.white_box);
                            else
                                binding.rightSideGrid.addScaledImage(R.drawable.black_box);
                        }
                        for (int i = 0; i < Math.abs(d); ++i) {
                            if (d > 0)
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

        if (areLayoutsReady()) {
            Log.d(TAG, "Layout is ready by default.");
        }
    }

    private void addImagesToBothSides(int imageResource) {
        binding.leftSideGrid.addScaledImage(imageResource);
        binding.rightSideGrid.addScaledImage(imageResource);

        binding.leftSideGrid.cancelOutOppositeViewTypes();
        binding.rightSideGrid.cancelOutOppositeViewTypes();
    }

    private boolean isAnswerCorrect(int userAnswer) {
        isDone = true;
        //TODO: If needed really without any animations, override the animations in LinearOpsGridLayout to 0 delay
        if (prefs.getInt(Constants.LINEAR_EQ_LEVEL, 1) >= Constants.LEVEL_4) {
            if (Utilities.dontAnimateObjects(eq, binding.leftSideGrid, binding.rightSideGrid, userAnswer, this)) {
                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (Utilities.animateObjects(eq, binding.leftSideGrid, binding.rightSideGrid, userAnswer, this)) {
                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}
