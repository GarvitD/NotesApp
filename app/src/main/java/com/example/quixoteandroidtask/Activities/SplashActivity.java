package com.example.quixoteandroidtask.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.quixoteandroidtask.R;
import com.example.quixoteandroidtask.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isLoggedIn = getSharedPreferences("usersSp",MODE_PRIVATE).getBoolean("isLoggedIn",false);
        binding.bgView.animate().translationY(-3000).setDuration(1000).setStartDelay(3000);
        binding.heading.animate().translationY(2000).setDuration(1000).setStartDelay(3000);
        binding.splashImage.animate().translationY(2000).setDuration(1000).setStartDelay(3000);
        binding.splashAnimation.animate().translationY(2000).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLoggedIn) {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
                finish();
            }
        },4000);
    }
}