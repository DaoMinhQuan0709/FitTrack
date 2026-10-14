package com.fittrack.app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.MainActivity;
import com.fittrack.app.R;
import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.databinding.ActivityAuthBinding;
import com.fittrack.app.onboarding.OnboardingActivity;

/**
 * Root Activity (launcher). Contains 2 Fragments: Login / Register.
 * After successful login/registration -> check if Room already has a profile
 * (UserProfile) to decide whether to go to Onboarding or straight to MainActivity.
 */
public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // If Firebase already has a login session -> route straight, no need to login again
        if (authViewModel.currentUser() != null) {
            routeAfterAuth();
            return;
        }

        showLoginFragment();

        authViewModel.getAuthResult().observe(this, outcome -> {
            if (outcome.success) {
                routeAfterAuth();
            } else {
                Toast.makeText(this, getString(R.string.error) + outcome.errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(binding.authContainer.getId(), new LoginFragment())
                .commit();
    }

    public void showRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(binding.authContainer.getId(), new RegisterFragment())
                .commit();
    }

    private void routeAfterAuth() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            boolean hasProfile = AppDatabase.getInstance(getApplicationContext())
                    .userProfileDao().getUserProfileOnce() != null;

            runOnUiThread(() -> {
                Intent intent = hasProfile
                        ? new Intent(AuthActivity.this, MainActivity.class)
                        : new Intent(AuthActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }
}
