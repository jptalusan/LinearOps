package com.freelance.jptalusan.linearops.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.Views.SeekBarLayout;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityBinding;

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
        binding.seekbar.setSeekBarMin(-10);
        binding.seekbar.setSeekBarMax(10);
        binding.seekbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ViewTreeObserver.OnGlobalLayoutListener listener = this;
                binding.seekbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                binding.seekbar.addNumbers();
                binding.seekbar.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.seekbar.getViewTreeObserver().addOnGlobalLayoutListener(listener);
                    }
                });
            }
        });

        binding.seekbar.setSeekBarChangeValueListener(new SeekBarLayout.SeekbarChangeValueListener() {
            @Override
            public void onSeekBarValueChanged(int val) {
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
