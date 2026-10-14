package com.fittrack.app.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.data.local.dao.FoodLogDao;
import com.fittrack.app.data.local.dao.WaterLogDao;
import com.fittrack.app.data.local.entity.FoodLog;
import com.fittrack.app.data.local.entity.WaterLog;
import com.fittrack.app.data.local.model.DailyTotal;
import com.fittrack.app.data.local.model.HourlyTotal;

import java.util.List;

public class NutritionRepository {

    private final FoodLogDao dao;
    private final WaterLogDao waterDao;

    public NutritionRepository(Context context) {
        dao = AppDatabase.getInstance(context).foodLogDao();
        waterDao = AppDatabase.getInstance(context).waterLogDao();
    }

    public LiveData<List<FoodLog>> getFoodLogsByDate(String date) {
        return dao.getFoodLogsByDate(date);
    }

    public LiveData<Double> getTotalCaloriesByDate(String date) {
        return dao.getTotalCaloriesByDate(date);
    }

    public LiveData<Double> getTotalProteinByDate(String date) {
        return dao.getTotalProteinByDate(date);
    }

    public LiveData<Double> getTotalCarbByDate(String date) {
        return dao.getTotalCarbByDate(date);
    }

    public LiveData<Double> getTotalFatByDate(String date) {
        return dao.getTotalFatByDate(date);
    }

    public LiveData<List<DailyTotal>> getDailyCaloriesByDates(List<String> dates) {
        return dao.getDailyCaloriesByDates(dates);
    }

    // Water tracking
    public LiveData<Integer> getTotalWaterByDate(String date) {
        return waterDao.getTotalWaterByDate(date);
    }

    public LiveData<List<HourlyTotal>> getHourlyWaterByDate(String date) {
        return waterDao.getHourlyWaterByDate(date);
    }

    public void insertWater(WaterLog waterLog) {
        AppDatabase.databaseWriteExecutor.execute(() -> waterDao.insert(waterLog));
    }

    public void clearWater(String date) {
        AppDatabase.databaseWriteExecutor.execute(() -> waterDao.clearDay(date));
    }

    public void insert(FoodLog foodLog) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insert(foodLog));
    }

    public void delete(FoodLog foodLog) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.delete(foodLog));
    }
}
