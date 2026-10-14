package com.fittrack.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fittrack.app.databinding.ActivityMainBinding;
import com.fittrack.app.ui.dashboard.DashboardFragment;
import com.fittrack.app.ui.nutrition.NutritionFragment;
import com.fittrack.app.ui.profile.ProfileFragment;
import com.fittrack.app.ui.progress.ProgressFragment;
import com.fittrack.app.ui.workout.WorkoutFragment;

/**
 * Main Container holding BottomNavigationView + 5 Fragments.
 * Uses show/hide (instead of replace) to preserve state of each tab.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final DashboardFragment dashboardFragment = new DashboardFragment();
    private final NutritionFragment nutritionFragment = new NutritionFragment();
    private final WorkoutFragment workoutFragment = new WorkoutFragment();
    private final ProgressFragment progressFragment = new ProgressFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();

    private Fragment activeFragment = dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(binding.fragmentContainer.getId(), profileFragment, "profile").hide(profileFragment);
        transaction.add(binding.fragmentContainer.getId(), progressFragment, "progress").hide(progressFragment);
        transaction.add(binding.fragmentContainer.getId(), workoutFragment, "workout").hide(workoutFragment);
        transaction.add(binding.fragmentContainer.getId(), nutritionFragment, "nutrition").hide(nutritionFragment);
        transaction.add(binding.fragmentContainer.getId(), dashboardFragment, "dashboard");
        transaction.commit();

        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                selected = dashboardFragment;
            } else if (itemId == R.id.nav_nutrition) {
                selected = nutritionFragment;
            } else if (itemId == R.id.nav_workout) {
                selected = workoutFragment;
            } else if (itemId == R.id.nav_progress) {
                selected = progressFragment;
            } else if (itemId == R.id.nav_profile) {
                selected = profileFragment;
            } else {
                selected = dashboardFragment;
            }
            switchTo(selected);
            return true;
        });
    }

    private void switchTo(@NonNull Fragment fragment) {
        if (fragment == activeFragment) return;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .hide(activeFragment)
                .show(fragment)
                .commit();
        activeFragment = fragment;
    }
}
