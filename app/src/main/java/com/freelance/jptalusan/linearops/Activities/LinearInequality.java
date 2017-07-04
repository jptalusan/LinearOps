package com.freelance.jptalusan.linearops.Activities;

import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Utilities.Constants;
import com.freelance.jptalusan.linearops.Utilities.Equation;
import com.freelance.jptalusan.linearops.Utilities.EquationGeneration;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearInequalityBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class LinearInequality extends AppCompatActivity {
    private static final String TAG = "LinearInequality";
    private ActivityLinearInequalityBinding binding;
    private Equation eq = null;
    private int userAnswer = 0;
    private List<String> points = null;
    private Random rnd = null;
    private AppCompatCheckBox lessThanCB = null;
    private AppCompatCheckBox answerCB = null;
    private AppCompatCheckBox greaterThanCB = null;
    private static final int lessThanId = 100;
    private static final int answerId = 200;
    private static final int greaterThanId = 300;
    private boolean isLessThanChecked = false;
    private boolean isAnswerChecked = false;
    private boolean isGreaterThanChecked = false;
    private int inequalityIndex = -1;
    private boolean hasFirstBeenAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_inequality);

        long seed = Calendar.getInstance().getTimeInMillis();
        rnd = new Random((int)seed);

        startLinearOps();

        points = new ArrayList<>();
        for (int i = Constants.X_MIN; i <= Constants.X_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setSeekBarMax(Constants.X_MAX * 2 + 1);
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.setResourceId(0);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                userAnswer = val;
            }
        });

        binding.seekbar.reset();

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setViewAbility(false);
                if (!hasFirstBeenAnswered) {
                    if (isFirstAnswerCorrect(userAnswer)) {
                        hasFirstBeenAnswered = true;
                        setUpInequality();
                    }
                }else {
                    if (isSecondAnswerCorrect()) {
                        Toast.makeText(LinearInequality.this, "2: Correct", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LinearInequality.this, "2: Incorrect", Toast.LENGTH_LONG).show();
                    }
                }
//                isSecondAnswerCorrect(userAnswer);
//                int temp = Constants.DEFAULT_RESET;
//                Log.d(TAG, "Reset in: " + temp + " milliseconds.");
//                Handler h = new Handler();
//                h.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startLinearOps();
//                    }
//                }, temp);
            }
        });
    }

    private void setUpInequality() {
        String text = binding.equationTextView.getText().toString();
        binding.equationTextView.setText(text.replace("=", getRandomInequality()));
        binding.equationTextView.invalidate();

        binding.seekbar.comboSeekBar.setEnabled(false);
        binding.seekbar.comboSeekBar.setLinearInequalityDrawable();
        binding.seekbar.comboSeekBar.invalidate();

        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);
        binding.seekbar.drawResourceOn(userAnswer);

        int answer = userAnswer + Constants.X_MAX;
        int lessThan = answer / 2;
        int greaterThan = answer + ((Constants.X_MAX * 2 + 1) - answer) / 2;
        lessThan = lessThan == Constants.X_MIN ? lessThan + 2 : lessThan;
        greaterThan = greaterThan == Constants.X_MAX ? greaterThan - 2 : greaterThan;

        answer = lessThan == answer ? answer + 1 : answer;
        answer = greaterThan == answer ? answer - 1 : answer;

        Log.d(TAG, "answer: " + answer);

        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {Color.RED, Color.RED};
        lessThanCB = new AppCompatCheckBox(this);
        lessThanCB.setLayoutParams(generateParamsAtIndex(lessThan));
        lessThanCB.setSupportButtonTintList(new ColorStateList(states, colors));
        lessThanCB.setId(lessThanId);
        lessThanCB.setOnClickListener(new CheckboxListener());
        binding.checkBoxesLayout.addView(lessThanCB);

        int states2[][] = {{android.R.attr.state_checked}, {}};
        int colors2[] = {Color.BLUE, Color.BLUE};
        answerCB = new AppCompatCheckBox(this);
        answerCB.setLayoutParams(generateParamsAtIndex(answer));
        answerCB.setSupportButtonTintList(new ColorStateList(states2, colors2));
        answerCB.setId(answerId);
        answerCB.setOnClickListener(new CheckboxListener());
        binding.checkBoxesLayout.addView(answerCB);

        int states3[][] = {{android.R.attr.state_checked}, {}};
        int colors3[] = {Color.GREEN, Color.GREEN};
        greaterThanCB = new AppCompatCheckBox(this);
        greaterThanCB.setLayoutParams(generateParamsAtIndex(greaterThan));
        greaterThanCB.setSupportButtonTintList(new ColorStateList(states3, colors3));
        greaterThanCB.setId(greaterThanId);
        greaterThanCB.setOnClickListener(new CheckboxListener());
        binding.checkBoxesLayout.addView(greaterThanCB);
    }

    private boolean isSecondAnswerCorrect() {
        //String symbols[] = {"<", "≤", ">", "≥"};
        switch (inequalityIndex) {
            case 0:
                if (isLessThanChecked
                        && !isAnswerChecked
                        && !isGreaterThanChecked) {
                    return true;
                }
                break;
            case 1:
                if (isLessThanChecked
                        && isAnswerChecked
                        && !isGreaterThanChecked) {
                    return true;
                }
                break;
            case 2:
                if (!isLessThanChecked
                        && !isAnswerChecked
                        && isGreaterThanChecked) {
                    return true;
                }
                break;
            case 3:
                if (!isLessThanChecked
                        && isAnswerChecked
                        && isGreaterThanChecked) {
                    return true;
                }
                break;
        }
        return false;
    }

    //TODO: Add checking if index is > width
    private RelativeLayout.LayoutParams generateParamsAtIndex(int index) {
        Log.d(TAG, "Generate: " + index);
        int slice = (int) binding.checkBoxesLayout.getWidth() / (Constants.X_MAX * 2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) binding.checkBoxesLayout.getHeight());

        Log.d(TAG, "params: " + slice + " x " + params.height);

        params.leftMargin = (slice * index) - (slice / 2);
        params.bottomMargin = 140;
        return params;
    }

    private void startLinearOps() {
        Log.d(TAG, "startLinearOps()");
        do {
            eq = EquationGeneration.generateEqualityEquation(Constants.LEVEL_2);
            binding.equationTextView.setText(eq.printEquation());
        } while (eq.toString().equals("FAILED"));
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.comboSeekBar.setEnabled(true);

        binding.seekbar.comboSeekBar.setCustomDrawable();
        binding.seekbar.comboSeekBar.invalidate();
        binding.seekbar.setResourceId(0);

        binding.checkBoxesLayout.removeAllViews();

        isLessThanChecked = false;
        isAnswerChecked = false;
        isGreaterThanChecked = false;
        inequalityIndex = -1;
        hasFirstBeenAnswered = false;
    }

    private boolean isFirstAnswerCorrect(int userAnswer) {
        if (userAnswer == eq.getX()) {
            Toast.makeText(this, "1: Correct", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "1: Incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isSecondAnswerCorrect(int userAnswer) {
        return false;
    }

    private String getRandomInequality() {
        String symbols[] = {"<", "≤", ">", "≥"};
        int x = EquationGeneration.pickRandom(rnd, 0, 3);
        inequalityIndex = x;
        return symbols[x];
    }

    class CheckboxListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AppCompatCheckBox cb = (AppCompatCheckBox) view;
            switch (view.getId()) {
                case lessThanId:
                    isLessThanChecked = cb.isChecked();
                    Log.d(TAG, "isLessThanChecked: " + cb.isChecked());
                    break;
                case answerId:
                    isAnswerChecked = cb.isChecked();
                    Log.d(TAG, "isAnswerChecked: " + cb.isChecked());
                    break;
                case greaterThanId:
                    isGreaterThanChecked = cb.isChecked();
                    Log.d(TAG, "isGreaterThanChecked: " + cb.isChecked());
                    break;
            }
        }
    }
}
