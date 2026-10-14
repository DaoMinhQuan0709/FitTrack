package com.fittrack.app.ui.nutrition;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.fittrack.app.data.local.entity.FoodLog;
import com.fittrack.app.data.local.entity.WaterLog;
import com.fittrack.app.data.repository.NutritionRepository;
import com.fittrack.app.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NutritionViewModel extends AndroidViewModel {

    private final NutritionRepository repo;

    private final MutableLiveData<String> selectedDate = new MutableLiveData<>(DateUtils.today());
    private final MutableLiveData<List<String>> weekDays = new MutableLiveData<>();

    private final LiveData<List<FoodLog>> foodLogs;
    private final LiveData<Double> totalCalories;
    private final LiveData<Integer> totalWater;

    public NutritionViewModel(@NonNull Application application) {
        super(application);
        repo = new NutritionRepository(application);

        foodLogs = Transformations.switchMap(selectedDate, repo::getFoodLogsByDate);
        totalCalories = Transformations.switchMap(selectedDate, repo::getTotalCaloriesByDate);
        totalWater = Transformations.switchMap(selectedDate, repo::getTotalWaterByDate);

        updateWeekList(DateUtils.today());
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<List<String>> getWeekDays() {
        return weekDays;
    }

    public LiveData<List<FoodLog>> getFoodLogs() {
        return foodLogs;
    }

    public LiveData<Double> getTotalCalories() {
        return totalCalories;
    }

    public LiveData<Integer> getTotalWater() {
        return totalWater;
    }

    public void selectDate(String date) {
        selectedDate.setValue(date);
        updateWeekList(date);
    }

    private void updateWeekList(String pivotDate) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(pivotDate));
            
            // Find start of week (Monday)
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            
            List<String> days = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                days.add(DateUtils.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            weekDays.setValue(days);
        } catch (Exception ignored) {}
    }

    public void addFood(String name, double calories, double protein, double carb, double fat, String mealType) {
        String date = selectedDate.getValue();
        if (date == null) date = DateUtils.today();
        repo.insert(new FoodLog(name, calories, protein, carb, fat, mealType, date));
    }

    public void deleteFood(FoodLog foodLog) {
        repo.delete(foodLog);
    }

    public void addWater(int ml) {
        String date = selectedDate.getValue();
        if (date == null) date = DateUtils.today();
        
        int hour = 12; // default
        if (date.equals(DateUtils.today())) {
            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        }
        
        repo.insertWater(new WaterLog(ml, hour, date));
    }

    public void clearWater() {
        String date = selectedDate.getValue();
        if (date == null) date = DateUtils.today();
        repo.clearWater(date);
    }
}
