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
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityLevelFiveBinding;

import java.util.ArrayList;
import java.util.List;

public class LinearEqualityActivityLevel5 extends AppCompatActivity {
    private static String TAG = "Level5Activity";
    protected SharedPreferences prefs;
    private int currLevel = 0;
    private ActivityLinearEqualityLevelFiveBinding binding;
    private Equation eq;
    private boolean canUseButtons = true;
    private double userAnswer = 0;
    private double fractionCounter = 1;
    private double mCenterValue = 0;
    private List<String> points = null;
    private List<Double> pointsVal = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality_level_five);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (prefs.getBoolean(Constants.FIRST_TIME, true)) {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();
            prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).apply();
        }

        //DEBUG
        prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_5).apply();
        currLevel = prefs.getInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_5);

        startLinearOps();
        setupSeekbarValues();
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);
        binding.seekbar.setVisibility(View.GONE);
        binding.checkButton.setVisibility(View.GONE);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                Log.d(TAG, "Val: " + val);
                Log.d(TAG, "Size: " + pointsVal.size());
                Log.d(TAG, "Size: " + points.size());
                if (val + Constants.SEEKBAR_CUSTOM_WIDTH > pointsVal.size()) {
                    Log.d(TAG, "Point if: " + pointsVal.get(pointsVal.size() - 1));
                    mCenterValue = pointsVal.get(pointsVal.size() - 1);
                } else {
                    Log.d(TAG, "Point else: " + pointsVal.get(val + Constants.SEEKBAR_CUSTOM_WIDTH));
                    mCenterValue = pointsVal.get(val + Constants.SEEKBAR_CUSTOM_WIDTH);
                }
                userAnswer = mCenterValue;
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
                binding.leftSideGrid.redrawLayout();
                binding.rightSideGrid.redrawLayout();
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
//                if (mCenterValue != 0) {
                    if (fractionCounter == 6) {
                        fractionCounter = 1;
                    } else {
                        fractionCounter++;
                    }
                    setupSeekbarValues();

                    Log.d(TAG, "fraction: " + fractionCounter);
//                }
            }
        });

        binding.decreaseFractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fractionCounter == 1) {
                    fractionCounter = 6;
                } else {
                    fractionCounter--;
                }
                setupSeekbarValues();
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
                if (areLayoutsReady()) {
                    Log.d(TAG, "can use seekbar.");
                    binding.seekbar.getViewDimensions();
                    binding.seekbar.setVisibility(View.VISIBLE);
                    binding.checkButton.setVisibility(View.VISIBLE);
                    binding.decreaseFractionButton.setVisibility(View.VISIBLE);
                    binding.increaseFractionButton.setVisibility(View.VISIBLE);
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
                if (areLayoutsReady()) {
                    Log.d(TAG, "can use seekbar.");
                    binding.seekbar.getViewDimensions();
                    binding.seekbar.setVisibility(View.VISIBLE);
                    binding.checkButton.setVisibility(View.VISIBLE);
                    binding.decreaseFractionButton.setVisibility(View.VISIBLE);
                    binding.increaseFractionButton.setVisibility(View.VISIBLE);
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

    private void setupSeekbarValues() {
        Log.d(TAG, "setupseekbar center: " + mCenterValue + ", fraction: " + fractionCounter);
        int min;
        int max;
        points = new ArrayList<>();
        pointsVal = new ArrayList<>();
        int tempCenter = (int)mCenterValue;
        Log.d(TAG, "mCenter: " + (tempCenter));
        min = tempCenter - Constants.SEEKBAR_CUSTOM_WIDTH;
        max = tempCenter + Constants.SEEKBAR_CUSTOM_WIDTH;

        for (double i = min; i <= max; ++i) {
            double hatch = ((i - tempCenter) * (1.0/fractionCounter));
            double currNode = hatch + tempCenter;

            if (i == tempCenter) {
                points.add(Integer.toString((int)i));
            } else if (currNode == (int)currNode) {
                points.add(Integer.toString((int)(tempCenter + hatch)));
            } else {
                points.add("|");
            }
            pointsVal.add((double) Math.round(currNode * 100000) / 100000);
        }

        //IF custom wwidth is small, remove +1
        binding.seekbar.setSeekBarMax((Constants.SEEKBAR_CUSTOM_WIDTH * 2) + 1);
        Log.d(TAG, "X setupSeekbarValues: " + max + "/" + min + "/" + ((Constants.SEEKBAR_CUSTOM_WIDTH * 2) + 1));
        binding.seekbar.setComboSeekBarProgress(Constants.SEEKBAR_CUSTOM_WIDTH);
        pointsVal.add(pointsVal.get(pointsVal.size() - 1)); //HACK

        //debug
        String temp = "";
        for (double d:
                pointsVal) {
            temp += "[" + d + "]";
        }
        Log.d(TAG, "pointsValArr: " + temp);

        String temp2 = "";
        for (double d:
                pointsVal) {
            temp2 += "[" + d + "]";
        }
        Log.d(TAG, "pointsArr: " + temp2);

        binding.seekbar.getViewDimensions();
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.invalidate();
    }

    private void startLinearOps() {
        Log.d(TAG, "startLinearOps: " + currLevel);
        do {
            eq = EquationGeneration.generateEqualityEquation(currLevel);
//            eq = new Equation(1, 2, -7, 0, 5);
        } while (eq.toString().equals("FAILED"));
        setupLayoutForEquation(eq);
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.setVisibility(View.GONE);
        binding.checkButton.setVisibility(View.GONE);
        binding.decreaseFractionButton.setVisibility(View.GONE);
        binding.increaseFractionButton.setVisibility(View.GONE);
        setViewAbility(true);
        fractionCounter = 1;
    }

    private void setViewAbility(boolean enabled) {
        binding.seekbar.comboSeekBar.setEnabled(enabled);
        binding.whiteBoxButton.setEnabled(enabled);
        binding.blackBoxButton.setEnabled(enabled);
        binding.whiteCircleButton.setEnabled(enabled);
        binding.blackCircleButton.setEnabled(enabled);
        binding.checkButton.setEnabled(enabled);
        binding.decreaseFractionButton.setEnabled(enabled);
        binding.increaseFractionButton.setEnabled(enabled);
    }

    private boolean areLayoutsReady() {
        return binding.rightSideGrid.isLayoutUniform() && binding.leftSideGrid.isLayoutUniform();
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
        final double ax = equation.getAx();
        final double b  = equation.getB();
        final double cx = equation.getCx();
        final double d  = equation.getD();

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

    private void isAnswerCorrect(double userAnswer) {
        Log.d(TAG, "isAnswerCorrect x==ans:" + eq.getX() + "==" + userAnswer);
        if (eq.getX() == userAnswer) {
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
