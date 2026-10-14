package com.fittrack.app.ui.nutrition;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fittrack.app.R;
import com.fittrack.app.data.local.entity.FoodLog;
import com.fittrack.app.databinding.ItemFoodLogBinding;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FoodLogAdapter extends RecyclerView.Adapter<FoodLogAdapter.FoodLogViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(FoodLog foodLog);
    }

    private final OnDeleteClickListener onDeleteClick;
    private List<FoodLog> items = Collections.emptyList();

    public FoodLogAdapter(OnDeleteClickListener onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public void submitList(List<FoodLog> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodLogBinding binding = ItemFoodLogBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodLogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodLogViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class FoodLogViewHolder extends RecyclerView.ViewHolder {
        private final ItemFoodLogBinding binding;

        FoodLogViewHolder(ItemFoodLogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FoodLog item) {
            binding.tvFoodName.setText(item.name);
            binding.tvCalories.setText(String.format(Locale.US, "%.0f kcal", item.calories));
            binding.tvMacroDetail.setText(String.format(Locale.US, "Protein %.0fg  Carb %.0fg  Fat %.0fg", 
                    item.proteinG, item.carbG, item.fatG));

            int badgeColor;
            int mealIcon;
            String mealDisplay;

            switch (item.mealType) {
                case "Breakfast":
                    badgeColor = R.color.meal_morning;
                    mealIcon = R.drawable.ic_meal_morning;
                    mealDisplay = "Breakfast";
                    break;
                case "Lunch":
                    badgeColor = R.color.meal_lunch;
                    mealIcon = R.drawable.ic_meal_lunch;
                    mealDisplay = "Lunch";
                    break;
                case "Dinner":
                    badgeColor = R.color.meal_dinner;
                    mealIcon = R.drawable.ic_meal_dinner;
                    mealDisplay = "Dinner";
                    break;
                default:
                    badgeColor = R.color.meal_snack;
                    mealIcon = R.drawable.ic_meal_snack;
                    mealDisplay = "Snack";
                    break;
            }

            binding.viewBadgeBg.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(binding.getRoot().getContext(), badgeColor)));
            binding.ivMealIcon.setImageResource(mealIcon);
            binding.tvMealType.setText(mealDisplay);

            binding.btnDelete.setOnClickListener(v -> onDeleteClick.onDeleteClick(item));
        }
    }
}
