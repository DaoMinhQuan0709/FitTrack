package com.fittrack.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.fittrack.app.data.local.entity.FoodLog;
import com.fittrack.app.data.local.model.DailyTotal;

import java.util.List;

@Dao
public interface FoodLogDao {

    @Query("SELECT * FROM food_log WHERE date = :date ORDER BY id DESC")
    LiveData<List<FoodLog>> getFoodLogsByDate(String date);

    @Insert
    void insert(FoodLog foodLog);

    @Delete
    void delete(FoodLog foodLog);

    @Query("SELECT COALESCE(SUM(calories), 0) FROM food_log WHERE date = :date")
    LiveData<Double> getTotalCaloriesByDate(String date);

    @Query("SELECT COALESCE(SUM(proteinG), 0) FROM food_log WHERE date = :date")
    LiveData<Double> getTotalProteinByDate(String date);

    @Query("SELECT COALESCE(SUM(carbG), 0) FROM food_log WHERE date = :date")
    LiveData<Double> getTotalCarbByDate(String date);

    @Query("SELECT COALESCE(SUM(fatG), 0) FROM food_log WHERE date = :date")
    LiveData<Double> getTotalFatByDate(String date);

    @Query("SELECT COUNT(*) FROM food_log")
    int getCount();

    @Query("SELECT date, SUM(calories) as total FROM food_log WHERE date IN (:dates) GROUP BY date")
    LiveData<List<DailyTotal>> getDailyCaloriesByDates(List<String> dates);
}
