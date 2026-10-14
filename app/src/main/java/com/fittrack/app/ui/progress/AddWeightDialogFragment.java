package com.fittrack.app.ui.progress;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.databinding.DialogAddWeightBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AddWeightDialogFragment extends DialogFragment {

    private ProgressViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);

        DialogAddWeightBinding binding = DialogAddWeightBinding.inflate(LayoutInflater.from(requireContext()));

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Log Weight")
                .setView(binding.getRoot())
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            Float weight = parseFloatOrNull(binding.etWeightValue.getText().toString());
            if (weight == null || weight <= 0) {
                Toast.makeText(requireContext(), "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.addWeight(weight);
            dialog.dismiss();
        }));

        return dialog;
    }

    private static Float parseFloatOrNull(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
