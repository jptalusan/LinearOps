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
import com.freelance.jptalusan.linearops.Views.LinearOpsGridLayout;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityBinding;

import java.util.ArrayList;
import java.util.List;

public class LinearEqualityActivity extends AppCompatActivity {
    private static String TAG = "LinearEqualityActivity";
    protected SharedPreferences prefs;
    private int currLevel = 0;
    private Equation eq = new Equation();
    private ActivityLinearEqualityBinding binding;
    private int correctAnswer = 0;

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
        final int ax = eq.getAx();
        final int b  = eq.getB();
        final int cx = eq.getCx();
        final int d  = eq.getD();

        Log.d(TAG, eq.toString());

        binding.leftSideGrid.setRows(4);
        binding.leftSideGrid.setCols(5);
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


        binding.rightSideGrid.setRows(4);
        binding.rightSideGrid.setCols(5);
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

        //TODO: Make this easier to access? in view methods?
        List<String> points = new ArrayList<>();
        for (int i = -10; i <= 10; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setSeekBarMax(21);
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.setComboSeekBarProgress(10);
        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                correctAnswer = val;
                isAnswerCorrect(val);
                if (val > 0) {
//                    binding.rightSideGrid.reset();
                    for (int i = 0; i < val; ++i) {
//                        binding.rightSideGrid.addScaledImage(R.drawable.white_box);
                        Log.d(TAG, binding.rightSideGrid.toString());
                    }
                } else if (val < 0) {
//                    binding.leftSideGrid.reset();
                    for (int i = val; i < 0; ++i) {
//                        binding.leftSideGrid.addScaledImage(R.drawable.black_box);
                        Log.d(TAG, binding.leftSideGrid.toString());
                    }
                } else {
//                    binding.leftSideGrid.reset();
//                    binding.rightSideGrid.reset();
                }
            }
        });

        binding.seekbar.reset();
        binding.rightSideGrid.onLinearOpsGridLayoutListener(new LinearOpsGridLayout.LinearOpsGridLayoutListener() {
            @Override
            public void onAnimationEnd(int val) {

            }

            @Override
            public void onAnimationStart(int val) {
                Handler leftSide = new Handler();
                leftSide.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.leftSideGrid.setDividend(Math.abs(correctAnswer));
                        binding.leftSideGrid.moveLeftXViews();
                    }
                }, 0);
            }
        });
    }

    //TODO: Add boolean or prevent seekbar from being used.
    private boolean isAnswerCorrect(int value) {
        double answer = eq.getX();
        Log.d(TAG, "Answer:Value=" + answer + ":" + value);
        final int temp = value;
        if (answer == (double) value) {
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "count/val:" + binding.rightSideGrid.getChildCount() + "/" + Math.abs(temp));
            for (int i = 0; i < binding.rightSideGrid.getChildCount() / Math.abs(temp); ++i) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.rightSideGrid.setDividend(Math.abs(temp));
                        binding.rightSideGrid.moveViews(Math.abs(temp), "LEFT");
                    }
                }, 1000 * i);


            }
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
