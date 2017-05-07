package com.freelance.jptalusan.linearops.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityBinding;

import java.util.ArrayList;
import java.util.List;

public class LinearEqualityActivity extends AppCompatActivity {
    private static String TAG = "LinearEqualityActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityLinearEqualityBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality);
        binding.leftSideGrid.setRows(3);
        binding.leftSideGrid.setCols(2);
        binding.leftSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.leftSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
            }
        });


        binding.rightSideGrid.setRows(5);
        binding.rightSideGrid.setCols(5);
        binding.rightSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.rightSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
            }
        });

        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);
        List<String> points = new ArrayList<>();
        points.add("-10");
        points.add("-9");
        points.add("-8");
        points.add("-7");
        points.add("-6");
        points.add("-5");
        points.add("-4");
        points.add("-3");
        points.add("-2");
        points.add("-1");
        points.add("0");
        points.add("1");
        points.add("2");
        points.add("3");
        points.add("4");
        points.add("5");
        points.add("6");
        points.add("7");
        points.add("8");
        points.add("9");
        points.add("10");

        binding.seekbar.setSeekBarMax(21);
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.setComboSeekBarProgress(10);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                Log.d(TAG, "val: " + val);
                if (val > 0) {
                    binding.rightSideGrid.removeAllViews();
                    for (int i = 0; i < val; ++i) {
                        binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                    }
                } else if (val < 0){
                    binding.leftSideGrid.removeAllViews();
                    for (int i = val; i < 0; ++i) {
                        binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
                    }
                } else {
                    binding.leftSideGrid.removeAllViews();
                    binding.rightSideGrid.removeAllViews();
                }
            }
        });

        binding.seekbar.reset();
    }
}
