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
import com.freelance.jptalusan.linearops.Utilities.AudioPlayer;
import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Utilities.Equation;
import com.freelance.jptalusan.linearops.Utilities.EquationGeneration;
import com.freelance.jptalusan.linearops.Utilities.Utilities;
import com.freelance.jptalusan.linearops.Views.LinearOpsGridLayout;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityLevelFiveBinding;

import java.util.ArrayList;

public class LinearEqualityActivityLevel3 extends AppCompatActivity {
    private static String TAG = "Level3Activity";
    protected SharedPreferences prefs;
    private int currLevel = 0;
    private ActivityLinearEqualityLevelFiveBinding binding;
    private Equation eq;
    private boolean canUseButtons = true;
    private int userAnswer = 0;
    private boolean isDone = false;
    private int score = 0;
    private int numberOfGamesPlayed = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality_level_five);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (prefs.getBoolean(Constants.FIRST_TIME, true)) {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();
            prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).apply();
        }

        Utilities.popupDialog(this);

        //DEBUG
        prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_3).apply();
        currLevel = prefs.getInt(Constants.LINEAR_EQ_LEVEL, 0);

        startLinearOps();

        ArrayList<String> points = new ArrayList<>();
        for (int i = Constants.X_MIN; i <= Constants.X_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.increaseFractionButton.setVisibility(View.GONE);
        binding.decreaseFractionButton.setVisibility(View.GONE);
//        binding.seekbar.setSeekBarMax(Constants.X_MAX * 2 + 1);
//        binding.seekbar.setComboSeekBarAdapter(points);
//        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.setValues(points);
        binding.seekbar.getViewDimensions();
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
                binding.leftSideGrid.redrawLayout();
                binding.rightSideGrid.redrawLayout();
                setViewAbility(false);
                isAnswerCorrect(userAnswer);
                int temp = Utilities.determineResetPeriodInMillis(
                        binding.leftSideGrid,
                        binding.rightSideGrid,
                        userAnswer,
                        eq);
                Log.d(TAG, "Reset in: " + temp + " milliseconds.");
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.performCleanup(binding.leftSideGrid, binding.rightSideGrid, eq.getX());
                    }
                }, temp);

                Handler h2 = new Handler();
                h2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startLinearOps();
                    }
                }, temp + 2000);
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

    private void startLinearOps() {
        do {
            eq = EquationGeneration.generateEqualityEquation(currLevel);
        } while (eq.toString().equals("FAILED"));
        setupLayoutForEquation(eq);
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        isDone = false;
        binding.seekbar.setVisibility(View.GONE);
        binding.checkButton.setVisibility(View.GONE);
        setViewAbility(true);
    }

    private void setViewAbility(boolean enabled) {
        binding.seekbar.comboSeekBar.setEnabled(enabled);
        binding.whiteBoxButton.setEnabled(enabled);
        binding.blackBoxButton.setEnabled(enabled);
        binding.whiteCircleButton.setEnabled(enabled);
        binding.blackCircleButton.setEnabled(enabled);
        binding.checkButton.setEnabled(enabled);
    }

    private boolean areLayoutsReady() {
        return binding.leftSideGrid.isLayoutUniform()
                && binding.rightSideGrid.isLayoutUniform();
    }

    public void setupLayoutForEquation(Equation equation) {
        final double ax = equation.getAx();
        final double b  = equation.getB();
        final double cx = equation.getCx();
        final double d  = equation.getD();

        //Log.d(TAG, equation.toString());
        System.out.println(TAG + ", " + equation);

        binding.leftSideGrid.reset();
        binding.rightSideGrid.reset();

        binding.leftSideGrid.side = Constants.LEFT;
        binding.rightSideGrid.side = Constants.RIGHT;

        binding.leftSideGrid.setRows(9);
        binding.leftSideGrid.setCols(10);

        binding.rightSideGrid.setRows(9);
        binding.rightSideGrid.setCols(10);

        binding.leftSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.leftSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                switch(currLevel) {
                    case Constants.LEVEL_3:
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
                    case Constants.LEVEL_3:
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

    private void isAnswerCorrect(int userAnswer) {
        isDone = true;
        AudioPlayer ap = new AudioPlayer();
        Utilities u = new Utilities(binding.leftSideGrid, binding.rightSideGrid);
        if (u.animateObjects(eq, userAnswer, true)) {
            binding.seekbar.updateScore(++score, ++numberOfGamesPlayed);
            ap.play(this, R.raw.correct);
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        } else {
            binding.seekbar.updateScore(score, ++numberOfGamesPlayed);
            ap.play(this, R.raw.wrong);
            if ((userAnswer * -1) == eq.getX()) {
                Toast.makeText(getApplicationContext(), "Wrong sign", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
