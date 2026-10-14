package com.fittrack.app.ui.nutrition;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fittrack.app.R;
import com.fittrack.app.databinding.ItemCalendarDayBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    public interface OnDateClickListener {
        void onDateClick(String date);
    }

    private final OnDateClickListener listener;
    private List<String> days = Collections.emptyList();
    private String selectedDate = "";

    public CalendarAdapter(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<String> newDays, String selected) {
        this.days = newDays;
        this.selectedDate = selected;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCalendarDayBinding binding = ItemCalendarDayBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CalendarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.bind(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        private final ItemCalendarDayBinding binding;
        private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.ENGLISH);
        private final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        CalendarViewHolder(ItemCalendarDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String dateStr) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(isoFormat.parse(dateStr));
                
                binding.tvDayOfWeek.setText(dayFormat.format(cal.getTime()).toUpperCase());
                binding.tvDayOfMonth.setText(dateFormat.format(cal.getTime()));

                boolean isSelected = dateStr.equals(selectedDate);
                
                if (isSelected) {
                    binding.cardDay.setCardBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.accent_green));
                    binding.cardDay.setStrokeWidth(0);
                    binding.tvDayOfWeek.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
                    binding.tvDayOfMonth.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
                } else {
                    binding.cardDay.setCardBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bg_surface_variant));
                    binding.cardDay.setStrokeWidth(0);
                    binding.tvDayOfWeek.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.text_secondary));
                    binding.tvDayOfMonth.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.text_primary));
                }

                binding.getRoot().setOnClickListener(v -> listener.onDateClick(dateStr));

            } catch (Exception ignored) {}
        }
    }
}
