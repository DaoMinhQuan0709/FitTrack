package com.fittrack.app.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.fittrack.app.MainActivity;
import com.fittrack.app.databinding.ActivityOnboardingBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        List<Fragment> steps = new ArrayList<>();
        steps.add(new OnboardingStepBasicFragment());
        steps.add(new OnboardingStepPhysicalFragment());
        steps.add(new OnboardingStepGoalFragment());

        binding.viewPager.setAdapter(new OnboardingPagerAdapter(this, steps));
        binding.viewPager.setUserInputEnabled(false); 

        new TabLayoutMediator(binding.tabDots, binding.viewPager, (tab, position) -> {}).attach();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.btnBack.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
                binding.btnNext.setText(position == steps.size() - 1 ? "Finish" : "Next");
            }
        });

        binding.btnNext.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current < steps.size() - 1) {
                if (validateCurrentStep(current)) {
                    binding.viewPager.setCurrentItem(current + 1);
                }
            } else {
                if (validateCurrentStep(current)) {
                    viewModel.saveProfile();
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current > 0) {
                binding.viewPager.setCurrentItem(current - 1);
            }
        });

        viewModel.getSaveComplete().observe(this, done -> {
            if (done) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    private boolean validateCurrentStep(int step) {
        if (step == 0) {
            if (viewModel.name.isEmpty()) {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (step == 1) {
            if (viewModel.height <= 0 || viewModel.weight <= 0) {
                Toast.makeText(this, "Please enter valid height and weight", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private static class OnboardingPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragments;

        public OnboardingPagerAdapter(@NonNull AppCompatActivity activity, List<Fragment> fragments) {
            super(activity);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}
