package com.fittrack.app.ui.workout;

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
import com.fittrack.app.databinding.FragmentWorkoutBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;
    private WorkoutViewModel viewModel;

    private WorkoutLogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        adapter = new WorkoutLogAdapter(workout -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.confirm_delete_title)
                    .setMessage(R.string.confirm_delete_message)
                    .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteWorkout(workout))
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
        binding.rvWorkoutLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvWorkoutLogs.setAdapter(adapter);

        viewModel.getWorkoutLogsToday().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            binding.emptyState.llEmptyState.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            binding.emptyState.tvEmptyTitle.setText("No workouts yet");
            binding.emptyState.tvEmptySubtitle.setText("Start your day with some exercise!");
            binding.tvWorkoutSummary.setText(list.size() + " exercises today");
        });

        binding.fabAddWorkout.setOnClickListener(v ->
                new AddWorkoutDialogFragment().show(getChildFragmentManager(), "AddWorkoutDialog"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
