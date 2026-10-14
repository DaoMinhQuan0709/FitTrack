package com.fittrack.app.onboarding;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.databinding.FragmentOnboardingStepBasicBinding;

public class OnboardingStepBasicFragment extends Fragment {

    private FragmentOnboardingStepBasicBinding binding;
    private OnboardingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingStepBasicBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        binding.etName.setText(viewModel.name);
        binding.etAge.setText(String.valueOf(viewModel.age));
        if ("Male".equals(viewModel.gender)) {
            binding.rbMale.setChecked(true);
        } else {
            binding.rbFemale.setChecked(true);
        }

        binding.etName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.name = s.toString();
            }
        });

        binding.etAge.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    viewModel.age = Integer.parseInt(s.toString());
                } catch (NumberFormatException ignored) {}
            }
        });

        binding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            viewModel.gender = (checkedId == binding.rbMale.getId()) ? "Male" : "Female";
        });
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}
