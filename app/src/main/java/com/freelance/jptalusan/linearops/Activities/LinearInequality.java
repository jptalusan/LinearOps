package com.freelance.jptalusan.linearops.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
                if (isFirstAnswerCorrect(userAnswer)) {
                    Log.d(TAG, "value: " + (userAnswer + Constants.X_MAX));
                    setUpInequality();
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

        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);
        binding.seekbar.drawResourceOn(userAnswer);

        binding.seekbar.comboSeekBar.setEnabled(false);
        binding.seekbar.comboSeekBar.setLinearInequalityDrawable();
        binding.seekbar.comboSeekBar.invalidate();
    }

    private void startLinearOps() {
        Log.d(TAG, "startLinearOps()");
        do {
            eq = EquationGeneration.generateEqualityEquation(Constants.LEVEL_2);
            binding.equationTextView.setText(eq.printEquation());
        } while (eq.toString().equals("FAILED"));
        binding.seekbar.setComboSeekBarProgress(Constants.X_MAX);
        binding.seekbar.comboSeekBar.setEnabled(true);

        binding.seekbar.setResourceId(0);
    }

    private boolean isFirstAnswerCorrect(int userAnswer) {
        if (userAnswer == eq.getX()) {
            Toast.makeText(this, "Correct.", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Incorrect.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isSecondAnswerCorrect(int userAnswer) {
        return false;
    }

    private String getRandomInequality() {
        String symbols[] = {"<", ">", "≤", "≥"};
        int x = EquationGeneration.pickRandom(rnd, 0, 3);
        Log.d(TAG, "x:" + x);
        return symbols[x];
    }
}
