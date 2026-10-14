package com.fittrack.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.fittrack.app.auth.AuthActivity;
import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.databinding.ActivitySplashBinding;
import com.fittrack.app.onboarding.OnboardingActivity;
import com.fittrack.app.util.DatabaseSeeder;
import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        
        binding.llLogoContainer.startAnimation(fadeIn);
        binding.ivFlame.startAnimation(scaleUp);

        // Delay 2 seconds then route
        new Handler(Looper.getMainLooper()).postDelayed(this::route, 2000);
    }

    private void route() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            DatabaseSeeder.seedIfNeeded(getApplicationContext());
            boolean hasProfile = AppDatabase.getInstance(getApplicationContext())
                    .userProfileDao().getUserProfileOnce() != null;

            runOnUiThread(() -> {
                    Intent intent = hasProfile
                        ? new Intent(SplashActivity.this, MainActivity.class)
                        : new Intent(SplashActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        });
    }
}
