package com.fittrack.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.fittrack.app.data.local.entity.WorkoutLog;
import com.fittrack.app.data.local.model.DailyTotal;

import java.util.List;

@Dao
public interface WorkoutLogDao {

    @Query("SELECT * FROM workout_log WHERE date = :date ORDER BY id DESC")
    LiveData<List<WorkoutLog>> getWorkoutLogsByDate(String date);

    @Query("SELECT COUNT(*) FROM workout_log WHERE date = :date")
    LiveData<Integer> getWorkoutCountByDate(String date);

    @Query("SELECT COALESCE(SUM(caloriesBurned), 0) FROM workout_log WHERE date = :date")
    LiveData<Double> getTotalCaloriesBurnedByDate(String date);

    @Query("SELECT date, SUM(caloriesBurned) as total FROM workout_log WHERE date IN (:dates) GROUP BY date")
    LiveData<List<DailyTotal>> getDailyBurnedByDates(List<String> dates);

    @Insert
    void insert(WorkoutLog workoutLog);

    @Delete
    void delete(WorkoutLog workoutLog);
}
