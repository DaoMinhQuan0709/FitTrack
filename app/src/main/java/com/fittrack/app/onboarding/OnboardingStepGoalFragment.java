package com.fittrack.app.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.R;
import com.fittrack.app.databinding.FragmentOnboardingStepGoalBinding;

import java.util.Arrays;
import java.util.List;

public class OnboardingStepGoalFragment extends Fragment {

    private FragmentOnboardingStepGoalBinding binding;
    private OnboardingViewModel viewModel;

    private final List<String> activityLevels = Arrays.asList(
            "Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active"
    );
    private final List<String> goals = Arrays.asList("Lose Weight", "Maintain", "Gain Weight");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingStepGoalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(requireContext(), R.layout.custom_spinner_item, activityLevels);
        activityAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerActivity.setAdapter(activityAdapter);
        binding.spinnerActivity.setSelection(Math.max(activityLevels.indexOf(viewModel.activityLevel), 0));

        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(requireContext(), R.layout.custom_spinner_item, goals);
        goalAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerGoal.setAdapter(goalAdapter);
        binding.spinnerGoal.setSelection(Math.max(goals.indexOf(viewModel.goal), 0));

        binding.spinnerActivity.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.activityLevel = activityLevels.get(position);
            }
        });

        binding.spinnerGoal.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.goal = goals.get(position);
            }
        });
    }

    private abstract static class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }
}
