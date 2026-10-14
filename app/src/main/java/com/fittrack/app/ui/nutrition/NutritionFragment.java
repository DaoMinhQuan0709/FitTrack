package com.fittrack.app.ui.nutrition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fittrack.app.R;
import com.fittrack.app.databinding.FragmentNutritionBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

public class NutritionFragment extends Fragment {

    private FragmentNutritionBinding binding;
    private NutritionViewModel viewModel;

    private FoodLogAdapter adapter;
    private CalendarAdapter calendarAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentNutritionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NutritionViewModel.class);

        // Food List
        adapter = new FoodLogAdapter(food -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.confirm_delete_title)
                    .setMessage(R.string.confirm_delete_message)
                    .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteFood(food))
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
        binding.rvFoodLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFoodLogs.setAdapter(adapter);

        // Weekly Calendar
        calendarAdapter = new CalendarAdapter(date -> viewModel.selectDate(date));
        binding.rvCalendar.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCalendar.setAdapter(calendarAdapter);

        // Observers
        viewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            updateCalendarSelection();
        });

        viewModel.getWeekDays().observe(getViewLifecycleOwner(), days -> {
            calendarAdapter.submitList(days, viewModel.getSelectedDate().getValue());
        });

        viewModel.getFoodLogs().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            binding.emptyState.llEmptyState.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);

            double p = 0, c = 0, f = 0;
            for (com.fittrack.app.data.local.entity.FoodLog food : list) {
                p += food.proteinG;
                c += food.carbG;
                f += food.fatG;
            }
            binding.tvMacroSummary.setText(String.format(Locale.US, "P:%d C:%d F:%d", (int)p, (int)c, (int)f));
        });

        viewModel.getTotalCalories().observe(getViewLifecycleOwner(), total ->
                binding.tvTotalCalories.setText(String.format(Locale.US, "%.0f kcal", total != null ? total : 0.0)));

        viewModel.getTotalWater().observe(getViewLifecycleOwner(), total -> {
            int water = total != null ? total : 0;
            double liters = water / 1000.0;
            binding.tvWaterTotal.setText(String.format(Locale.US, "%.1f / 2.0 L", liters));
            binding.progressWater.setProgress(water);
        });

        // Water Actions
        binding.btnAddWater250.setOnClickListener(v -> viewModel.addWater(250));
        binding.btnAddWater500.setOnClickListener(v -> viewModel.addWater(500));
        binding.btnAddWater750.setOnClickListener(v -> viewModel.addWater(750));
        binding.btnAddWater1000.setOnClickListener(v -> viewModel.addWater(1000));
        binding.btnClearWater.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.confirm_delete_title)
                    .setMessage(R.string.confirm_clear_water_message)
                    .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.clearWater())
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });

        binding.fabAddFood.setOnClickListener(v ->
                new AddFoodDialogFragment().show(getChildFragmentManager(), "AddFoodDialog"));
    }

    private void updateCalendarSelection() {
        if (calendarAdapter != null) {
            calendarAdapter.submitList(viewModel.getWeekDays().getValue(), viewModel.getSelectedDate().getValue());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
