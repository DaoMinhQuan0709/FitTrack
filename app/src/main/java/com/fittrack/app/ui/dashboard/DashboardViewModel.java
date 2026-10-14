package com.fittrack.app.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.entity.UserProfile;
import com.fittrack.app.data.local.model.DailyTotal;
import com.fittrack.app.data.local.model.HourlyTotal;
import com.fittrack.app.data.repository.NutritionRepository;
import com.fittrack.app.data.repository.UserRepository;
import com.fittrack.app.data.repository.WorkoutRepository;
import com.fittrack.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final UserRepository userRepo;
    private final NutritionRepository nutritionRepo;
    private final WorkoutRepository workoutRepo;

    private final String today;

    private final LiveData<UserProfile> userProfile;
    private final LiveData<Double> totalCaloriesToday;
    private final LiveData<Double> totalProteinToday;
    private final LiveData<Double> totalCarbToday;
    private final LiveData<Double> totalFatToday;
    private final LiveData<Double> totalBurnedToday;
    private final LiveData<Integer> totalWaterToday;
    private final LiveData<List<HourlyTotal>> hourlyWaterToday;
    private final LiveData<List<DailyTotal>> burnedCalories7Days;
    private final LiveData<Integer> workoutCountToday;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
        nutritionRepo = new NutritionRepository(application);
        workoutRepo = new WorkoutRepository(application);

        today = DateUtils.today();

        userProfile = userRepo.getUserProfile();
        totalCaloriesToday = nutritionRepo.getTotalCaloriesByDate(today);
        totalProteinToday = nutritionRepo.getTotalProteinByDate(today);
        totalCarbToday = nutritionRepo.getTotalCarbByDate(today);
        totalFatToday = nutritionRepo.getTotalFatByDate(today);
        
        totalBurnedToday = workoutRepo.getTotalCaloriesBurnedByDate(today);
        totalWaterToday = nutritionRepo.getTotalWaterByDate(today);
        hourlyWaterToday = nutritionRepo.getHourlyWaterByDate(today);
        
        workoutCountToday = workoutRepo.getWorkoutCountByDate(today);

        List<String> last7Days = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            last7Days.add(DateUtils.offsetDate(today, -i));
        }
        burnedCalories7Days = workoutRepo.getDailyBurnedByDates(last7Days);
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<Double> getTotalCaloriesToday() {
        return totalCaloriesToday;
    }

    public LiveData<Double> getTotalProteinToday() {
        return totalProteinToday;
    }

    public LiveData<Double> getTotalCarbToday() {
        return totalCarbToday;
    }

    public LiveData<Double> getTotalFatToday() {
        return totalFatToday;
    }

    public LiveData<Double> getTotalBurnedToday() {
        return totalBurnedToday;
    }

    public LiveData<Integer> getTotalWaterToday() {
        return totalWaterToday;
    }

    public LiveData<List<HourlyTotal>> getHourlyWaterToday() {
        return hourlyWaterToday;
    }

    public LiveData<List<DailyTotal>> getBurnedCalories7Days() {
        return burnedCalories7Days;
    }

    public LiveData<Integer> getWorkoutCountToday() {
        return workoutCountToday;
    }
}
