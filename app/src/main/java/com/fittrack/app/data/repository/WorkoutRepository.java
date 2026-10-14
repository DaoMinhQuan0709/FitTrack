package com.fittrack.app.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.data.local.dao.WorkoutLogDao;
import com.fittrack.app.data.local.entity.WorkoutLog;
import com.fittrack.app.data.local.model.DailyTotal;

import java.util.List;

public class WorkoutRepository {

    private final WorkoutLogDao dao;

    public WorkoutRepository(Context context) {
        dao = AppDatabase.getInstance(context).workoutLogDao();
    }

    public LiveData<List<WorkoutLog>> getWorkoutLogsByDate(String date) {
        return dao.getWorkoutLogsByDate(date);
    }

    public LiveData<Integer> getWorkoutCountByDate(String date) {
        return dao.getWorkoutCountByDate(date);
    }

    public LiveData<Double> getTotalCaloriesBurnedByDate(String date) {
        return dao.getTotalCaloriesBurnedByDate(date);
    }

    public LiveData<List<DailyTotal>> getDailyBurnedByDates(List<String> dates) {
        return dao.getDailyBurnedByDates(dates);
    }

    public void insert(WorkoutLog workoutLog) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insert(workoutLog));
    }

    public void delete(WorkoutLog workoutLog) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.delete(workoutLog));
    }
}
