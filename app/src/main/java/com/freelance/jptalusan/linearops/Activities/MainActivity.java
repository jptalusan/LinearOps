package com.freelance.jptalusan.linearops.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.freelance.jptalusan.linearops.R;
import com.freelance.jptalusan.linearops.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.linearEquality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LinearEqualityActivity.class);
                startActivity(i);
            }
        });

        binding.linearInequality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LinearEqualityActivityWithButtons.class);
                startActivity(i);
            }
        });

        binding.level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LinearEqualityActivityLevel3.class);
                startActivity(i);
            }
        });

        binding.level4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LinearEqualityActivityLevel4.class);
                startActivity(i);
            }
        });

        binding.level5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LinearEqualityActivityLevel5.class);
                startActivity(i);
            }
        });
    }
}
