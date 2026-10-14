package com.fittrack.app.ui.workout;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.R;
import com.fittrack.app.databinding.DialogAddWorkoutBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AddWorkoutDialogFragment extends DialogFragment {

    private WorkoutViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        DialogAddWorkoutBinding binding = DialogAddWorkoutBinding.inflate(LayoutInflater.from(requireContext()));

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(binding.getRoot())
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = binding.etExerciseName.getText().toString().trim();
            Integer sets = parseIntOrNull(binding.etSets.getText().toString());
            Integer reps = parseIntOrNull(binding.etReps.getText().toString());
            Float weightNullable = parseFloatOrNull(binding.etWeight.getText().toString());
            float weight = weightNullable != null ? weightNullable : 0f;
            Double burnedNullable = parseDoubleOrNull(binding.etCaloriesBurned.getText().toString());
            double burned = burnedNullable != null ? burnedNullable : 0.0;

            if (name.isEmpty() || sets == null || sets <= 0 || reps == null || reps <= 0) {
                Toast.makeText(requireContext(), "Please enter valid exercise name, positive Sets and Reps", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weight < 0 || burned < 0) {
                Toast.makeText(requireContext(), "Weight and Calories Burned cannot be negative", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.addWorkout(name, sets, reps, weight, burned);
            dialog.dismiss();
        }));

        return dialog;
    }

    private static Double parseDoubleOrNull(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseIntOrNull(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Float parseFloatOrNull(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
