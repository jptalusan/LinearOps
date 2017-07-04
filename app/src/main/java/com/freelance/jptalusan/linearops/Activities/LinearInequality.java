package com.freelance.jptalusan.linearops.Activities;

import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean lessThanSuppossedToBeChecked = false;
    private boolean answerSuppossedToBeChecked = false;
    private boolean greaterThanSuppossedToBeChecked = false;
    private String symbols[] = {"<", "≤", ">", "≥"};

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
                } else {
                    if (isSecondAnswerCorrect()) {
                        Toast.makeText(LinearInequality.this, "2: Correct", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LinearInequality.this, "2: Incorrect", Toast.LENGTH_LONG).show();
                        setupIncorrectText();
                    }
                    int temp = Constants.DEFAULT_RESET * 3;
                    Log.d(TAG, "Reset in: " + temp + " milliseconds.");
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startLinearOps();
                        }
                    }, temp);
                }
            }
        });
    }

    private void setupIncorrectText() {
        int answer = userAnswer + Constants.X_MAX;
        int lessThan = answer / 2;
        int greaterThan = answer + ((Constants.X_MAX * 2 + 1) - answer) / 2;
        answer -= Constants.X_MAX;
        lessThan -= Constants.X_MAX;
        greaterThan -= Constants.X_MAX;
        Log.d(TAG, "log: " + lessThan + "," + answer + "," + greaterThan);

        if (lessThanSuppossedToBeChecked && !isLessThanChecked) {
            AppCompatTextView tx1 = new AppCompatTextView(this);
            tx1.setLayoutParams(generateParamsForTextAtIndex(lessThan));
            String temp = "(" + lessThan + ")";
            tx1.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✔");
            tx1.setPadding(0, 30, 0, 0);
            binding.checkBoxesLayout.addView(tx1);
        }

        if (!lessThanSuppossedToBeChecked && isLessThanChecked) {
            AppCompatTextView tx2 = new AppCompatTextView(this);
            tx2.setLayoutParams(generateParamsForTextAtIndex(lessThan));
            String temp = "(" + lessThan + ")";
            tx2.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✖");
            tx2.setPadding(0, 30, 0, 0);
            binding.checkBoxesLayout.addView(tx2);
        }

        if (answerSuppossedToBeChecked && !isAnswerChecked) {
            AppCompatTextView tx3 = new AppCompatTextView(this);
            tx3.setLayoutParams(generateParamsForTextAtIndex(answer));
            String temp = "(" + answer + ")";
            tx3.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✔");
            tx3.setPadding(0, 30, 0, 0);
            binding.checkBoxesLayout.addView(tx3);
        }

        if (!answerSuppossedToBeChecked && isAnswerChecked) {
            AppCompatTextView tx4 = new AppCompatTextView(this);
            tx4.setLayoutParams(generateParamsForTextAtIndex(answer));
            String temp = "(" + answer + ")";
            tx4.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✖");
            tx4.setPadding(0, 30, 0, 0);
            binding.checkBoxesLayout.addView(tx4);
        }

        if (greaterThanSuppossedToBeChecked && !isGreaterThanChecked) {
            AppCompatTextView tx5 = new AppCompatTextView(this);
            tx5.setLayoutParams(generateParamsForTextAtIndex(greaterThan));
            String temp = "(" + greaterThan + ")";
            tx5.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✔");
            tx5.setPadding(0, 30, 0, 0);
            binding.checkBoxesLayout.addView(tx5);
        }

        if (!greaterThanSuppossedToBeChecked && isGreaterThanChecked) {
            AppCompatTextView tx6 = new AppCompatTextView(this);
            tx6.setLayoutParams(generateParamsForTextAtIndex(greaterThan));
            String temp = "(" + greaterThan + ")";
            tx6.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✖");
            tx6.setPadding(0, 30, 0, 0);
            binding.checkBoxesLayout.addView(tx6);
        }
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

    private RelativeLayout.LayoutParams generateParamsForTextAtIndex(int index) {
        Log.d(TAG, "Generate: " + index);
        int slice = (int) binding.checkBoxesLayout.getWidth() / (Constants.X_MAX * 2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) binding.checkBoxesLayout.getHeight());

        Log.d(TAG, "params: " + slice + " x " + params.height);

        params.leftMargin = (slice * index) - (slice / 2);
        params.topMargin = 50;
        params.bottomMargin = 100;
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

        lessThanSuppossedToBeChecked = false;
        answerSuppossedToBeChecked = false;
        greaterThanSuppossedToBeChecked = false;
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

    private boolean isSecondAnswerCorrect() {
        //String symbols[] = {"<", "≤", ">", "≥"};
        switch (inequalityIndex) {
            case 0:
                lessThanSuppossedToBeChecked = true;
                if (isLessThanChecked
                        && !isAnswerChecked
                        && !isGreaterThanChecked) {
                    return true;
                }
                break;
            case 1:
                lessThanSuppossedToBeChecked = true;
                answerSuppossedToBeChecked = true;
                if (isLessThanChecked
                        && isAnswerChecked
                        && !isGreaterThanChecked) {
                    return true;
                }
                break;
            case 2:
                greaterThanSuppossedToBeChecked = true;
                if (!isLessThanChecked
                        && !isAnswerChecked
                        && isGreaterThanChecked) {
                    return true;
                }
                break;
            case 3:
                answerSuppossedToBeChecked = true;
                greaterThanSuppossedToBeChecked = true;
                if (!isLessThanChecked
                        && isAnswerChecked
                        && isGreaterThanChecked) {
                    return true;
                }
                break;
        }
        return false;
    }

    private String getRandomInequality() {
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