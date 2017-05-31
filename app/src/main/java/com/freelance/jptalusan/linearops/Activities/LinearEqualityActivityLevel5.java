package com.freelance.jptalusan.linearops.Activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

/**
 * Created by JPTalusan on 21/05/2017.
 */

public class LinearEqualityActivityLevel5 extends AppCompatActivity {
    private static String TAG = "Level3Activity";
    protected SharedPreferences prefs;
    private int currLevel = 0;
    private ActivityLinearEqualityLevelFiveBinding binding;
    private Equation eq;
    private boolean canUseSeekbar = false;
    private boolean canUseButton = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality_level_five);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        //DEBUG
        prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_5).apply();

        if (prefs.getBoolean(Constants.FIRST_TIME, true)) {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();
            prefs.edit().putInt(Constants.LINEAR_EQ_LEVEL, Constants.LEVEL_1).apply();
        }
        currLevel = prefs.getInt(Constants.LINEAR_EQ_LEVEL, 0);

        eq = EquationGeneration.generateEqualityEquation(currLevel);
        setupLayoutForEquation(eq);

        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);

        List<String> points = new ArrayList<>();
        for (int i = Constants.X_MIN; i <= Constants.X_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setSeekBarMax(Constants.X_MAX * 2 + 1);
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.setVisibility(View.GONE);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                if (canUseSeekbar) {
                    isAnswerCorrect(val);
                }
            }
        });

        binding.seekbar.reset();

        binding.whiteBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "can use button: " + canUseButton);
                if (canUseButton)
                    addImagesToBothSides(R.drawable.white_box);
            }
        });

        binding.blackBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "can use button: " + canUseButton);
                if (canUseButton)
                    addImagesToBothSides(R.drawable.black_box);
            }
        });

        binding.whiteCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "can use button: " + canUseButton);
                if (canUseButton)
                    addImagesToBothSides(R.drawable.white_circle);
            }
        });

        binding.blackCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "can use button: " + canUseButton);
                if (canUseButton)
                    addImagesToBothSides(R.drawable.black_circle);
            }
        });

        binding.rightSideGrid.onLinearOpsGridLayoutListener(new LinearOpsGridLayout.LinearOpsGridLayoutListener() {
            @Override
            public void onAnimationEnd(int val) {
                canUseButton = true;
            }

            @Override
            public void onAnimationStart(int val) {
                canUseButton = false;
            }

            @Override
            public void onCancelOutEnd() {
                Log.d(TAG, binding.rightSideGrid.toString());
                if (areLayoutsReady()) {
                    Log.d(TAG, "can use seekbar.");
                    canUseSeekbar = true;
                    binding.seekbar.setVisibility(View.VISIBLE);
                }
                canUseButton = true;
            }
        });

        binding.leftSideGrid.onLinearOpsGridLayoutListener(new LinearOpsGridLayout.LinearOpsGridLayoutListener() {
            @Override
            public void onAnimationEnd(int val) {

            }

            @Override
            public void onAnimationStart(int val) {

            }

            @Override
            public void onCancelOutEnd() {
                Log.d(TAG, binding.leftSideGrid.toString());
                if (areLayoutsReady()) {
                    Log.d(TAG, "can use seekbar.");
                    canUseSeekbar = true;
                    binding.seekbar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean areLayoutsReady() {
        Log.d(TAG, "areLayoutsReady");
        Log.d(TAG, binding.leftSideGrid.toString());
        Log.d(TAG, binding.rightSideGrid.toString());
        if (binding.leftSideGrid.isLayoutUniform()) {
            if (binding.rightSideGrid.isLayoutUniform()) {
                String leftSideType = binding.leftSideGrid.getTypeContainedIn();
                String rightSideType = binding.rightSideGrid.getTypeContainedIn();
                Log.d(TAG, leftSideType + "/" + rightSideType);
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
                    case Constants.LEVEL_4:
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
                    case Constants.LEVEL_4:
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
        //TODO: when answer is incorrect, drawables should be inverted too.
        if (prefs.getInt(Constants.LINEAR_EQ_LEVEL, 1) >= Constants.LEVEL_4) {
            if (eq.getX() == userAnswer) {
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