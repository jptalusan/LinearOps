package com.freelance.jptalusan.linearops.Activities;

import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Rect;
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
import java.util.Random;

public class LinearInequality extends AppCompatActivity {
    private static final String TAG = "LinearInequality";
    private ActivityLinearInequalityBinding binding;
    private Equation eq = null;
    private int userAnswer = 0;
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

        ArrayList<String> points = new ArrayList<>();
        for (int i = Constants.X_MIN; i <= Constants.X_MAX; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setValues(points);
        binding.seekbar.getViewDimensions();
//        binding.seekbar.icons.setVisibility(View.INVISIBLE);
        binding.seekbar.reset();

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                userAnswer = val;
                binding.seekbar.drawIconOnRect(userAnswer);
            }
        });

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setViewAbility(false);
                if (!hasFirstBeenAnswered) {
                    if (isFirstAnswerCorrect(userAnswer)) {
                        hasFirstBeenAnswered = true;
                        setUpInequality();
                    } else {
                        int temp = Constants.DEFAULT_RESET * 2;
                        Log.d(TAG, "Reset in: " + temp + " milliseconds.");
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startLinearOps();
                            }
                        }, temp);
                    }
                } else {
                    if (isSecondAnswerCorrect()) {
                        Toast.makeText(LinearInequality.this, "2: Correct", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LinearInequality.this, "2: Incorrect", Toast.LENGTH_LONG).show();
                        setupIncorrectText();
                    }
                    int temp = Constants.DEFAULT_RESET * 2;
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

        int tempAnswer = answer;
        int tempLess = lessThan;
        int tempGreat = greaterThan;

        answer -= Constants.X_MAX;
        lessThan -= Constants.X_MAX;
        greaterThan -= Constants.X_MAX;
        Log.d(TAG, "log: " + lessThan + "," + answer + "," + greaterThan);

        //TODO: Refactor, since i can just change the string instead
        final ArrayList<AppCompatTextView> textViewArrays = new ArrayList<>();
        if (lessThanSuppossedToBeChecked && !isLessThanChecked) {
            AppCompatTextView tx1 = new AppCompatTextView(this);
            tx1.setLayoutParams(generateParamsForTextAtIndex(tempLess, 0));
            tx1.setTextColor(Color.BLACK);
            String temp = "(" + lessThan + ")";
            tx1.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✔");
            tx1.setPadding(0, 0, 0, 30);
//            binding.checkBoxesLayout.addView(tx1);
            textViewArrays.add(tx1);
        }

        if (!lessThanSuppossedToBeChecked && isLessThanChecked) {
            AppCompatTextView tx2 = new AppCompatTextView(this);
            tx2.setLayoutParams(generateParamsForTextAtIndex(tempLess, 0));
            tx2.setTextColor(Color.BLACK);
            String temp = "(" + lessThan + ")";
            tx2.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✖");
            tx2.setPadding(0, 0, 0, 30);
//            binding.checkBoxesLayout.addView(tx2);
            textViewArrays.add(tx2);
        }

        if (answerSuppossedToBeChecked && !isAnswerChecked) {
            AppCompatTextView tx3 = new AppCompatTextView(this);
            tx3.setLayoutParams(generateParamsForTextAtIndex(tempAnswer, 1));
            tx3.setTextColor(Color.BLACK);
            String temp = "(" + answer + ")";
            tx3.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✔");
            tx3.setPadding(0, 0, 0, 30);
//            binding.checkBoxesLayout.addView(tx3);
            textViewArrays.add(tx3);
        }

        if (!answerSuppossedToBeChecked && isAnswerChecked) {
            AppCompatTextView tx4 = new AppCompatTextView(this);
            tx4.setLayoutParams(generateParamsForTextAtIndex(tempAnswer, 1));
            tx4.setTextColor(Color.BLACK);
            String temp = "(" + answer + ")";
            tx4.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✖");
            tx4.setPadding(0, 0, 0, 30);
//            binding.checkBoxesLayout.addView(tx4);
            textViewArrays.add(tx4);
        }

        if (greaterThanSuppossedToBeChecked && !isGreaterThanChecked) {
            AppCompatTextView tx5 = new AppCompatTextView(this);
            tx5.setLayoutParams(generateParamsForTextAtIndex(tempGreat, 2));
            tx5.setTextColor(Color.BLACK);
            String temp = "(" + greaterThan + ")";
            tx5.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✔");
            tx5.setPadding(0, 0, 0, 30);
//            binding.checkBoxesLayout.addView(tx5);
            textViewArrays.add(tx5);
        }

        if (!greaterThanSuppossedToBeChecked && isGreaterThanChecked) {
            AppCompatTextView tx6 = new AppCompatTextView(this);
            tx6.setLayoutParams(generateParamsForTextAtIndex(tempGreat, 2));
            tx6.setTextColor(Color.BLACK);
            String temp = "(" + greaterThan + ")";
            tx6.setText(eq.printEquation()
                    .replace("x", temp)
                    .replace("=", symbols[inequalityIndex])
                    + " ✖");
            tx6.setPadding(0, 0, 0, 30);
//            binding.checkBoxesLayout.addView(tx6);
            textViewArrays.add(tx6);
        }

        Handler handler = new Handler();

        for(int i = 0; i < textViewArrays.size(); i++) {
            final int index = i;
            Runnable shuffle = new Runnable() {
                public void run() {
                    binding.checkBoxesLayout.addView(textViewArrays.get(index));
                }
            };
            handler.postDelayed(shuffle, (i+1)*1500);
        }

        //TODO:Add another runnable to remove previous ones?
    }

    private void drawBoundaryOnLayout(Rect r) {
        View verticalLine = new View(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                5, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = r.right - ((r.right - r.left) / 2);
        verticalLine.setLayoutParams(params);
        verticalLine.setBackgroundColor(Color.BLACK);
        binding.checkBoxesLayout.addView(verticalLine);
    }

    private void setUpInequality() {
        Rect r = binding.seekbar.comboSeekBar.getThumb().getBounds();
        Log.d(TAG, "thumb: " + r.toString());

        String text = binding.equationTextView.getText().toString();
        binding.equationTextView.setText(text.replace("=", getRandomInequality()));
        binding.equationTextView.invalidate();

        binding.seekbar.setResourceId(R.drawable.vertical_line);
        binding.seekbar.drawOnBoundaryLayout(r);
        drawBoundaryOnLayout(r);

        binding.seekbar.comboSeekBar.setEnabled(false);

        int answer = userAnswer + Constants.X_MAX;
        int lessThan = answer / 2;
        int greaterThan = answer + ((Constants.X_MAX * 2 + 1) - answer) / 2;

        lessThan = lessThan == Constants.X_MIN ? lessThan + 2 : lessThan;
        greaterThan = greaterThan == Constants.X_MAX ? greaterThan - 2 : greaterThan;

        answer = lessThan == answer ? answer + 1 : answer;
        answer = greaterThan == answer ? answer - 1 : answer;

        Log.d(TAG, "lt answer gt : " + lessThan + " " + answer + " " + greaterThan);

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
        answerCB.setLayoutParams(generateForSelected(r));
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

    private RelativeLayout.LayoutParams generateForSelected(Rect r) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                binding.checkBoxesLayout.getHeight());

        params.leftMargin = r.left - ((r.right - r.left) / 2);
        params.bottomMargin = 0;
        return params;
    }

    private RelativeLayout.LayoutParams generateParamsAtIndex(int index) {
//        Log.d(TAG, "Generate: " + index);
        int slice = binding.checkBoxesLayout.getWidth() / (Constants.X_MAX * 2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                binding.checkBoxesLayout.getHeight());

        Log.d(TAG, "index - params: " + index + " : " + slice + " x " + params.height);

        params.leftMargin = (slice * index) - (slice / 2);
        params.bottomMargin = 0;
        return params;
    }

    //TODO: what if the checkboxes are close to each other?
    private RelativeLayout.LayoutParams generateParamsForTextAtIndex(int index, int someValue) {
        Log.d(TAG, "Generate: " + index);
        int slice = binding.checkBoxesLayout.getWidth() / (Constants.X_MAX * 2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                binding.checkBoxesLayout.getHeight());

        Log.d(TAG, "params: " + slice + " x " + params.height);

        params.leftMargin = (slice * index) - (slice / 2);
        params.topMargin = 20;
//        params.bottomMargin = 100;
        return params;
    }

    private void startLinearOps() {
        Log.d(TAG, "startLinearOps()");
        do {
            eq = EquationGeneration.generateEqualityEquation(Constants.LEVEL_2);
//            eq = new Equation(-5, 4, -6, 0, 2);
            binding.equationTextView.setText(eq.printEquation());
        } while (eq.toString().equals("FAILED"));

//        binding.seekbar.icons.setVisibility(View.GONE);
        binding.seekbar.comboSeekBar.setEnabled(true);
        binding.seekbar.reset();
        binding.seekbar.numbers.removeAllViews();
        binding.seekbar.getViewDimensions();
        binding.seekbar.setResourceId(0);
        binding.seekbar.comboSeekBar.invalidate();

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
        boolean isLessAnswerCorrect;
        boolean isEqualAnswerCorrect;
        boolean isGreatAnswerCorrect;
        lessThanSuppossedToBeChecked = checker(userAnswer - 1);
        answerSuppossedToBeChecked = checker(userAnswer);
        greaterThanSuppossedToBeChecked = checker(userAnswer + 1);

        isLessAnswerCorrect = lessThanSuppossedToBeChecked == isLessThanChecked;
        isEqualAnswerCorrect = answerSuppossedToBeChecked == isAnswerChecked;
        isGreatAnswerCorrect = greaterThanSuppossedToBeChecked == isGreaterThanChecked;

        return isLessAnswerCorrect && isEqualAnswerCorrect && isGreatAnswerCorrect;
    }

    //ax + b > c
    public boolean checker(int userAnswer) {
        //String symbols[] = {"<", "≤", ">", "≥"};
        Log.d(TAG, "ax + b = c --> " + ((eq.getAx() * userAnswer) + eq.getB()) + " " + symbols[inequalityIndex] + " " + eq.getCx());
        switch(inequalityIndex) {
            case 0:
                return (((eq.getAx() * userAnswer) + eq.getB()) < eq.getCx());
            case 1:
                return (((eq.getAx() * userAnswer) + eq.getB()) <= eq.getCx());
            case 2:
                return (((eq.getAx() * userAnswer) + eq.getB()) > eq.getCx());
            case 3:
                return (((eq.getAx() * userAnswer) + eq.getB()) >= eq.getCx());
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
