package com.fittrack.app.ui.workout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.entity.WorkoutLog;
import com.fittrack.app.data.repository.WorkoutRepository;
import com.fittrack.app.util.DateUtils;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private final WorkoutRepository repo;
    private final String today;

    private final LiveData<List<WorkoutLog>> workoutLogsToday;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repo = new WorkoutRepository(application);
        today = DateUtils.today();
        workoutLogsToday = repo.getWorkoutLogsByDate(today);
    }

    public LiveData<List<WorkoutLog>> getWorkoutLogsToday() {
        return workoutLogsToday;
    }

    public void addWorkout(String exerciseName, int sets, int reps, float weightKg, double burned) {
        repo.insert(new WorkoutLog(exerciseName, sets, reps, weightKg, burned, today));
    }

    public void deleteWorkout(WorkoutLog workoutLog) {
        repo.delete(workoutLog);
    }
}
