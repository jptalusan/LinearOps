package com.freelance.jptalusan.linearops.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.databinding.ActivityLinearEqualityBinding;

public class LinearEqualityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityLinearEqualityBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_equality);
        binding.leftSideGrid.setRows(5);
        binding.leftSideGrid.setCols(5);
        binding.leftSideGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.leftSideGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                binding.leftSideGrid.getViewDimensions();

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
//                binding.rightSideGrid.getViewDimensions();
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
                binding.rightSideGrid.addScaledImage(R.mipmap.ic_launcher);
            }
        });
    }
}
