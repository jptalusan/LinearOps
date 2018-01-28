package com.freelance.jptalusan.linearops.Activities;

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
    private final ArrayList<AppCompatTextView> textViewArrays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_inequality);

        long seed = Calendar.getInstance().getTimeInMillis();
        rnd = new Random((int) seed);

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

        binding.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLinearOps();
            }
        });

//        binding.relativeLayout.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                setViewAbility(false);
//                        if (!hasFirstBeenAnswered) {
//                            if (isFirstAnswerCorrect(userAnswer)) {
//                                hasFirstBeenAnswered = true;
//                                setUpInequality();
//                            } else {
//                                int temp = Constants.DEFAULT_RESET * 2;
//                                Log.d(TAG, "Reset in: " + temp + " milliseconds.");
//                                Handler h = new Handler();
//                                h.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        startLinearOps();
//                                    }
//                                }, temp);
//                            }
//                        } else {
//                            if (isSecondAnswerCorrect()) {
//                                Toast.makeText(LinearInequality.this, "Correct", Toast.LENGTH_LONG).show();
//                                //TODO: Adjust checkbox look
//                            } else {
//                                Toast.makeText(LinearInequality.this, "Incorrect", Toast.LENGTH_LONG).show();
//                                setupIncorrectText();
//                            }
//                            int temp = Constants.DEFAULT_RESET * (textViewArrays.size() + 2);
//                            Log.d(TAG, "Reset in: " + temp + " milliseconds.");
//                            Handler h = new Handler();
//                            h.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    for (AppCompatTextView v : textViewArrays) {
//                                        v.setVisibility(View.VISIBLE);
//                                    }
////                            startLinearOps();
//                                }
//                            }, temp);
//                            setAbilityOfViews(false);
//                            binding.checkButton.setVisibility(View.GONE);
//                            binding.proceed.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });

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
                        Toast.makeText(LinearInequality.this, "Correct", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LinearInequality.this, "Incorrect", Toast.LENGTH_LONG).show();
                        setupIncorrectText();
                    }
                    setupCorrectText();
                    int temp = Constants.DEFAULT_RESET * (textViewArrays.size() + 2);
                    Log.d(TAG, "Reset in: " + temp + " milliseconds.");
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (AppCompatTextView v : textViewArrays) {
                                v.setVisibility(View.VISIBLE);
                            }
//                            startLinearOps();
                        }
                    }, temp);
                    setAbilityOfViews(false);
                    binding.checkButton.setVisibility(View.GONE);
                    binding.proceed.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupCorrectText() {
        float scale = 0.7f;
        if ((lessThanSuppossedToBeChecked && isLessThanChecked)) {
            lessThanCB.setButtonDrawable(R.drawable.custom_checkbox_start);
            lessThanCB.setScaleX(scale);
            lessThanCB.setScaleY(scale);
        }

        if ((answerSuppossedToBeChecked && isAnswerChecked)) {
            answerCB.setButtonDrawable(R.drawable.custom_checkbox_start);
            answerCB.setScaleX(scale);
            answerCB.setScaleY(scale);
        }

        if ((greaterThanSuppossedToBeChecked && isGreaterThanChecked)) {
            greaterThanCB.setButtonDrawable(R.drawable.custom_checkbox_start);
            greaterThanCB.setScaleX(scale);
            greaterThanCB.setScaleY(scale);
        }
    }

    private void setupIncorrectText() {
        if ((lessThanSuppossedToBeChecked && !isLessThanChecked)) {
            lessThanCB.setChecked(true);
            lessThanCB.setButtonDrawable(R.drawable.custom_checkbox_start);
        } else if ((!lessThanSuppossedToBeChecked && isLessThanChecked)) {
            lessThanCB.setButtonDrawable(R.drawable.custom_checkbox);
        }

        if ((answerSuppossedToBeChecked && !isAnswerChecked)) {
            answerCB.setChecked(true);
            answerCB.setButtonDrawable(R.drawable.custom_checkbox_start);
        } else if ((!answerSuppossedToBeChecked && isAnswerChecked)) {
            answerCB.setButtonDrawable(R.drawable.custom_checkbox);
        }

        if ((greaterThanSuppossedToBeChecked && !isGreaterThanChecked)) {
            greaterThanCB.setChecked(true);
            greaterThanCB.setButtonDrawable(R.drawable.custom_checkbox_start);
        } else if ((!greaterThanSuppossedToBeChecked && isGreaterThanChecked)) {
            greaterThanCB.setButtonDrawable(R.drawable.custom_checkbox);
        }
    }

    private void drawBoundaryOnLayout(Rect r) {
        View verticalLine = new View(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                5, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = r.right + ((r.right - r.left) / 2);
        verticalLine.setLayoutParams(params);
        verticalLine.setBackgroundColor(Color.BLACK);
        verticalLine.setAlpha(0.5f);
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
//        lessThanCB.setSupportButtonTintList(new ColorStateList(states, colors));
        lessThanCB.setId(lessThanId);
        lessThanCB.setOnClickListener(new CheckboxListener());
        binding.checkBoxesLayout.addView(lessThanCB);

        int states2[][] = {{android.R.attr.state_checked}, {}};
        int colors2[] = {Color.BLUE, Color.BLUE};
        answerCB = new AppCompatCheckBox(this);
        answerCB.setLayoutParams(generateForSelected(r));
//        answerCB.setSupportButtonTintList(new ColorStateList(states2, colors2));
        answerCB.setId(answerId);
        answerCB.setOnClickListener(new CheckboxListener());
        binding.checkBoxesLayout.addView(answerCB);

        int states3[][] = {{android.R.attr.state_checked}, {}};
        int colors3[] = {Color.GREEN, Color.GREEN};
        greaterThanCB = new AppCompatCheckBox(this);
        greaterThanCB.setLayoutParams(generateParamsAtIndex(greaterThan));
//        greaterThanCB.setSupportButtonTintList(new ColorStateList(states3, colors3));
        greaterThanCB.setId(greaterThanId);
        greaterThanCB.setOnClickListener(new CheckboxListener());
        binding.checkBoxesLayout.addView(greaterThanCB);
    }

    private RelativeLayout.LayoutParams generateForSelected(Rect r) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                binding.checkBoxesLayout.getHeight());

        params.leftMargin = r.left + ((r.right - r.left) / 2);
//        params.bottomMargin = 0;
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
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
//        params.topMargin = 0;
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        return params;
    }

    //TODO: what if the checkboxes are close to each other?
    //Somevalue: 0=lessthan, 1=index, 2=greaterthan
    private RelativeLayout.LayoutParams generateParamsForTextAtIndex(int index, int someValue) {
        Log.d(TAG, "Generate: " + index);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                binding.checkBoxesLayout.getHeight());

        Log.d(TAG, "somevalue: " + someValue);
        switch (someValue) {
            case 0: //less than
                params.addRule(RelativeLayout.ALIGN_LEFT, lessThanId);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case 1: //equals
                params.addRule(RelativeLayout.ALIGN_RIGHT, answerId);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.ALIGN_BOTTOM);
                params.topMargin = params.height - 50;
                params.leftMargin = 35;
                break;
            case 2: //greater than
                params.addRule(RelativeLayout.ALIGN_RIGHT, greaterThanId);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
        }
        return params;
    }

    private String simplifyEquation(Equation eq) {
        StringBuilder s = new StringBuilder();
        s.append(eq.getAx() - eq.getCx());
        s.append("x + ");
        s.append(eq.getB());
        s.append(" = ");
        s.append(eq.getD());
        return s.toString().replace(".0", "").replace(" + -", " - ");
    }

    private void setAbilityOfViews(boolean enabled) {
        binding.checkButton.setEnabled(enabled);
        if (lessThanCB != null)
            lessThanCB.setEnabled(enabled);
        if (answerCB != null)
            answerCB.setEnabled(enabled);
        if (greaterThanCB != null)
            greaterThanCB.setEnabled(enabled);
    }

    private void startLinearOps() {
        Log.d(TAG, "startLinearOps()");
        binding.checkButton.setVisibility(View.VISIBLE);
        binding.proceed.setVisibility(View.GONE);
        do {
            eq = EquationGeneration.generateEqualityEquation(Constants.LEVEL_4);
//            eq = new Equation(-5, 4, -6, 0, 2);
            binding.equationTextView.setText(simplifyEquation(eq));
        } while (eq.toString().equals("FAILED"));

        binding.seekbar.comboSeekBar.setEnabled(true);
        binding.seekbar.reset();
        binding.seekbar.numbers.removeAllViews();
        binding.seekbar.getViewDimensions();
        binding.seekbar.setResourceId(0);
        binding.seekbar.comboSeekBar.invalidate();

        binding.checkBoxesLayout.removeAllViews();

        setAbilityOfViews(true);

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
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
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

        Log.d(TAG, "less than is " + isLessAnswerCorrect);
        Log.d(TAG, "equals is " + isEqualAnswerCorrect);
        Log.d(TAG, "greater than is " + isGreatAnswerCorrect);

        return isLessAnswerCorrect && isEqualAnswerCorrect && isGreatAnswerCorrect;
    }

    //ax + b > c
    public boolean checker(int userAnswer) {
        //String symbols[] = {"<", "≤", ">", "≥"};
        boolean answer = false;
        switch(inequalityIndex) {
            case 0:
                answer = (((eq.getAx() - eq.getCx()) * userAnswer) < (eq.getD() - eq.getB()));
                break;
            case 1:
                answer = (((eq.getAx() - eq.getCx()) * userAnswer) <= (eq.getD() - eq.getB()));
                break;
            case 2:
                answer = (((eq.getAx() - eq.getCx()) * userAnswer) > (eq.getD() - eq.getB()));
                break;
            case 3:
                answer = (((eq.getAx() - eq.getCx()) * userAnswer) >= (eq.getD() - eq.getB()));
                break;
        }

        Log.d(TAG, userAnswer + ", " + simplifyEquation(eq) + ": ax + b = c --> " + ((eq.getAx() - eq.getCx()) * userAnswer) + " " + symbols[inequalityIndex] + " " + (eq.getD() - eq.getB()) + " is " + answer);
        return answer;
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
