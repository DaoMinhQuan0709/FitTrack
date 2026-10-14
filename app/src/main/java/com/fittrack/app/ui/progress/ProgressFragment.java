package com.fittrack.app.ui.progress;

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
import com.fittrack.app.data.local.entity.WeightEntry;
import com.fittrack.app.databinding.FragmentProgressBinding;
import com.fittrack.app.util.DateUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProgressFragment extends Fragment {

    private FragmentProgressBinding binding;
    private ProgressViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        viewModel.getWeightHistory().observe(getViewLifecycleOwner(), list -> {
            if (list == null || list.isEmpty()) {
                binding.tvCurrentWeight.setText("--");
                binding.tvWeightChange.setText("No data available");
            } else {
                WeightEntry latest = list.get(list.size() - 1);
                binding.tvCurrentWeight.setText(String.format(Locale.US, "%.1f kg", latest.weightKg));
                
                if (list.size() >= 2) {
                    float first = list.get(0).weightKg;
                    float diff = latest.weightKg - first;
                    String sign = diff > 0 ? "+" : "";
                    binding.tvWeightChange.setText(String.format(Locale.US, "Change: %s%.1f kg since started", sign, diff));
                } else {
                    binding.tvWeightChange.setText("First entry recorded");
                }
            }
            updateChart(list);
        });

        binding.fabAddWeight.setOnClickListener(v ->
                new AddWeightDialogFragment().show(getChildFragmentManager(), "AddWeightDialog"));
    }

    private void updateChart(List<WeightEntry> list) {
        if (list == null || list.isEmpty()) return;

        LineChart lineChart = binding.lineChart;
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i, list.get(i).weightKg));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.accent_green));
        dataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.accent_green));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_card_gradient));

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < list.size()) {
                    return DateUtils.toChartFormat(list.get(index).date);
                }
                return "";
            }
        });
        xAxis.setLabelCount(5, false);

        lineChart.getAxisLeft().setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
