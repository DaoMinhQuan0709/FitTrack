package com.fittrack.app.ui.workout;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fittrack.app.data.local.entity.WorkoutLog;
import com.fittrack.app.databinding.ItemWorkoutLogBinding;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.WorkoutViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(WorkoutLog workoutLog);
    }

    private final OnDeleteClickListener onDeleteClick;
    private List<WorkoutLog> items = Collections.emptyList();

    public WorkoutLogAdapter(OnDeleteClickListener onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public void submitList(List<WorkoutLog> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWorkoutLogBinding binding = ItemWorkoutLogBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final ItemWorkoutLogBinding binding;

        WorkoutViewHolder(ItemWorkoutLogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(WorkoutLog item) {
            binding.tvExerciseName.setText(item.exerciseName);
            binding.tvSetsReps.setText(String.format(Locale.getDefault(), "%d sets x %d reps · %.1f kg", 
                    item.sets, item.reps, item.weightKg));
            binding.tvCaloriesBurned.setText(String.format(Locale.getDefault(), "%.0f kcal", item.caloriesBurned));
            binding.btnDelete.setOnClickListener(v -> onDeleteClick.onDeleteClick(item));
        }
    }
}
