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
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityBinding;

import java.util.ArrayList;

//TODO: Use this: https://stackoverflow.com/questions/34327745/create-vertical-lines-in-seekbar
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
    private int score = 0;
    private int numberOfGamesPlayed = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (prefs.getBoolean(Constants.FIRST_TIME, true)) {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();
            prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).apply();
        }

        Utilities.popupDialog(this);

        //DEBUG
        prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).apply();
        currLevel = prefs.getInt(Constants.LINEAR_EQ_LEVEL, 0);

        ArrayList<String> points = new ArrayList<>();
        for (int i = Constants.ONE_MIN; i <= Constants.ONE_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setValues(points);
        binding.seekbar.getViewDimensions();
        startLinearOps();

        binding.seekbar.reset();
        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
            //Should only be called when layout is in the for ax = b only
//                Log.d(TAG, "inLeveL: " + val);
                //TO GET The thumb location
                userAnswer = val;
            }
        });

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.leftSideGrid.redrawLayout();
                binding.rightSideGrid.redrawLayout();
                setViewAbility(false);
                isAnswerCorrect(userAnswer);
//                newAnimation(userAnswer);
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
            }
        });
    }

    private void startLinearOps() {
        Log.d(TAG, "startLinearOps()");
        do {
            eq = EquationGeneration.generateEqualityEquation(currLevel);
//            eq = new Equation(-2, 4, 0 ,0, 1);
        } while (eq.toString().equals("FAILED"));
        setupLayoutForEquation(eq);
        binding.seekbar.setComboSeekBarProgress(Constants.ONE_MAX);
        numberOfAnimatedX = 0;
        isDone = false;
        setViewAbility(true);
    }

    private void setViewAbility(boolean enabled) {
        binding.seekbar.comboSeekBar.setEnabled(enabled);
        binding.checkButton.setEnabled(enabled);
    }

    public void setupLayoutForEquation(Equation equation) {
        final double ax = equation.getAx();
        final double b  = equation.getB();
        final double cx = equation.getCx();
        final double d  = equation.getD();

        Log.d(TAG, equation.toString());

        binding.leftSideGrid.reset();
        binding.rightSideGrid.reset();

        binding.leftSideGrid.side = Constants.LEFT;
        binding.rightSideGrid.side = Constants.RIGHT;

        binding.leftSideGrid.setRows(4);
        binding.leftSideGrid.setCols(5);

        binding.rightSideGrid.setRows(9);
        binding.rightSideGrid.setCols(10);

        Log.d(TAG, "Test");

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

    private void isAnswerCorrect(int userAnswer) {
        isDone = true;
        Utilities u = new Utilities(binding.leftSideGrid, binding.rightSideGrid);
        AudioPlayer ap = new AudioPlayer();
        if (u.animateObjects(eq, userAnswer, true)) {
            binding.seekbar.updateScore(++score, ++numberOfGamesPlayed);
            ap.play(this, R.raw.correct);
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        } else {
            binding.seekbar.updateScore(score, ++numberOfGamesPlayed);
            ap.play(this, R.raw.wrong);
            if ((userAnswer * -1) == eq.getX()) {
                Toast.makeText(getApplicationContext(), "Incorrect sign", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
