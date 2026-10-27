package com.fittrack.app.ui.dashboard;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.R;
import com.fittrack.app.data.local.model.DailyTotal;
import com.fittrack.app.databinding.FragmentDashboardBinding;
import com.fittrack.app.util.DateUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;

    private double calorieGoal = 2000.0;
    private double caloriesEaten = 0.0;
    private double caloriesBurned = 0.0;
    
    private double proteinGoal = 150.0;
    private double carbGoal = 250.0;
    private double fatGoal = 70.0;

    private double proteinEaten = 0.0;
    private double carbEaten = 0.0;
    private double fatEaten = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        
        binding.tvDate.setText(DateUtils.toHeaderFormat(DateUtils.today()));

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile == null) return;
            binding.tvGreeting.setText("Good Morning, " + profile.name + " 👋");
            
            if (profile.avatarUri != null && !profile.avatarUri.isEmpty()) {
                binding.ivAvatarDashboard.setImageURI(null);
                binding.ivAvatarDashboard.setImageURI(Uri.parse(profile.avatarUri));
            }
            
            calorieGoal = profile.calorieGoal;
            proteinGoal = profile.proteinGoalG;
            carbGoal = profile.carbGoalG;
            fatGoal = profile.fatGoalG;
            
            updateCalorieSummary();
            updateMacroProgress();
        });

        viewModel.getTotalCaloriesToday().observe(getViewLifecycleOwner(), total -> {
            caloriesEaten = total != null ? total : 0.0;
            updateCalorieSummary();
        });

        viewModel.getTotalBurnedToday().observe(getViewLifecycleOwner(), total -> {
            caloriesBurned = total != null ? total : 0.0;
            binding.tvWorkoutCalories.setText(String.format(Locale.US, "%.0f kcal", caloriesBurned));
            updateCalorieSummary();
        });

        viewModel.getTotalProteinToday().observe(getViewLifecycleOwner(), value -> {
            proteinEaten = value != null ? value : 0.0;
            updateMacroProgress();
        });

        viewModel.getTotalCarbToday().observe(getViewLifecycleOwner(), value -> {
            carbEaten = value != null ? value : 0.0;
            updateMacroProgress();
        });

        viewModel.getTotalFatToday().observe(getViewLifecycleOwner(), value -> {
            fatEaten = value != null ? value : 0.0;
            updateMacroProgress();
        });

        viewModel.getWorkoutCountToday().observe(getViewLifecycleOwner(), count -> {
            int c = count != null ? count : 0;
            binding.tvWorkoutSummary.setText(getString(R.string.workout_summary_format, c, c * 15));
        });

        viewModel.getBurnedCalories7Days().observe(getViewLifecycleOwner(), this::updateTrendChart);
    }

    private void updateCalorieSummary() {
        if (binding == null) return;

        binding.tvCaloriesEaten.setText(String.format(Locale.US, "%.0f kcal", caloriesEaten));
        binding.tvCaloriesBurned.setText(String.format(Locale.US, "%.0f kcal", caloriesBurned));
        
        long remain = Math.round(calorieGoal - caloriesEaten + caloriesBurned);
        binding.tvCaloriesRemaining.setText(String.valueOf(remain));
        
        double totalTarget = calorieGoal + caloriesBurned;
        int progress = totalTarget > 0 ? (int) ((caloriesEaten / totalTarget) * 100) : 0;
        binding.cpCalorieProgress.setProgress(Math.min(progress, 100));
        
        if (remain < 0) {
            binding.tvCaloriesRemaining.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_red));
        } else {
            binding.tvCaloriesRemaining.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
        }
    }

    private void updateMacroProgress() {
        if (binding == null) return;

        binding.tvProteinValue.setText(String.format(Locale.US, "%.0f/%.0fg", proteinEaten, proteinGoal));
        binding.tvCarbValue.setText(String.format(Locale.US, "%.0f/%.0fg", carbEaten, carbGoal));
        binding.tvFatValue.setText(String.format(Locale.US, "%.0f/%.0fg", fatEaten, fatGoal));

        binding.progressProtein.setProgress(proteinGoal > 0 ? (int) ((proteinEaten / proteinGoal) * 100) : 0);
        binding.progressCarb.setProgress(carbGoal > 0 ? (int) ((carbEaten / carbGoal) * 100) : 0);
        binding.progressFat.setProgress(fatGoal > 0 ? (int) ((fatEaten / fatGoal) * 100) : 0);
    }

    private void updateTrendChart(List<DailyTotal> trend) {
        if (binding == null || trend == null) return;
        BarChart barChart = binding.barChartBurned;
        
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < trend.size(); i++) {
            entries.add(new BarEntry(i, (float) trend.get(i).total));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Burned");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.accent_pink));
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
        dataSet.setValueTextSize(11f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value <= 0) return "";
                return String.valueOf((int) value);
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setFitBars(true);
        barChart.getAxisLeft().setSpaceTop(15f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < trend.size()) {
                    return DateUtils.toChartFormat(trend.get(index).date);
                }
                return "";
            }
        });

        barChart.getAxisLeft().setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
