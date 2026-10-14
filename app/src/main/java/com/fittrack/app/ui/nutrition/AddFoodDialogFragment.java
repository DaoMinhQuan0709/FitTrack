package com.fittrack.app.ui.nutrition;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.R;
import com.fittrack.app.databinding.DialogAddFoodBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.List;

public class AddFoodDialogFragment extends DialogFragment {

    private NutritionViewModel viewModel;
    private final List<String> mealTypes = Arrays.asList("Breakfast", "Lunch", "Dinner", "Snack");

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(NutritionViewModel.class);

        DialogAddFoodBinding binding = DialogAddFoodBinding.inflate(LayoutInflater.from(requireContext()));

        ArrayAdapter<String> mealAdapter = new ArrayAdapter<>(
                requireContext(), R.layout.custom_spinner_item, mealTypes
        );
        mealAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerMealType.setAdapter(mealAdapter);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add Food")
                .setView(binding.getRoot())
                .setPositiveButton("Save", null) 
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = binding.etFoodName.getText().toString().trim();
            Double calories = parseDoubleOrNull(binding.etCalories.getText().toString());
            Double proteinNullable = parseDoubleOrNull(binding.etProtein.getText().toString());
            Double carbNullable = parseDoubleOrNull(binding.etCarb.getText().toString());
            Double fatNullable = parseDoubleOrNull(binding.etFat.getText().toString());
            double protein = proteinNullable != null ? proteinNullable : 0.0;
            double carb = carbNullable != null ? carbNullable : 0.0;
            double fat = fatNullable != null ? fatNullable : 0.0;
            String mealType = mealTypes.get(binding.spinnerMealType.getSelectedItemPosition());

            if (name.isEmpty() || calories == null || calories <= 0) {
                Toast.makeText(requireContext(), "Please enter valid food name and positive calories", Toast.LENGTH_SHORT).show();
                return;
            }

            if (protein < 0 || carb < 0 || fat < 0) {
                Toast.makeText(requireContext(), "Nutrient values cannot be negative", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.addFood(name, calories, protein, carb, fat, mealType);
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
}
