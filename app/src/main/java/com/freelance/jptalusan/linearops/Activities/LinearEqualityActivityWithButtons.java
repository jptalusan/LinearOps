package com.freelance.jptalusan.linearops.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityWithButtonsBinding;

import java.util.ArrayList;
import java.util.List;

public class LinearEqualityActivityWithButtons extends AppCompatActivity {
    private static String TAG = "LinearEqualityActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityLinearEqualityWithButtonsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality_with_buttons);
        binding.leftSideGrid.setRows(3);
        binding.leftSideGrid.setCols(2);
        binding.leftSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.leftSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
//                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
//                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
//                binding.leftSideGrid.addScaledImage(R.mipmap.ic_launcher_round);
            }
        });


        binding.rightSideGrid.setRows(5);
        binding.rightSideGrid.setCols(5);
        binding.rightSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.rightSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
//                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
//                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
//                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
//                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
            }
        });

        binding.seekbar.setResourceId(R.mipmap.ic_launcher_round);
        List<String> points = new ArrayList<>();
        for (int i = -10; i <= 10; ++i) {
            points.add(Integer.toString(i));
        }

        binding.seekbar.setSeekBarMax(21);
        binding.seekbar.setComboSeekBarAdapter(points);
        binding.seekbar.setComboSeekBarProgress(10);

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
                if (val > 0) {
                    binding.rightSideGrid.reset();
                    for (int i = 0; i < val; ++i) {
                        binding.rightSideGrid.addScaledImage(R.drawable.white_box);
                    }
                } else if (val < 0){
                    binding.leftSideGrid.reset();
                    for (int i = val; i < 0; ++i) {
                        binding.leftSideGrid.addScaledImage(R.drawable.black_box);
                    }
                } else {
                    binding.leftSideGrid.reset();
                    binding.rightSideGrid.reset();
                }
            }
        });

        binding.seekbar.reset();

        binding.whiteBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rightSideGrid.addScaledImage(R.drawable.white_box);
                Log.d(TAG, binding.rightSideGrid.toString());
            }
        });

        binding.blackBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.leftSideGrid.addScaledImage(R.drawable.black_box);
                Log.d(TAG, binding.leftSideGrid.toString());
            }
        });

        binding.whiteCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rightSideGrid.addScaledImage(R.drawable.white_circle);
                Log.d(TAG, binding.rightSideGrid.toString());
            }
        });

        binding.blackCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.leftSideGrid.addScaledImage(R.drawable.black_circle);
                Log.d(TAG, binding.leftSideGrid.toString());
            }
        });
    }
}
