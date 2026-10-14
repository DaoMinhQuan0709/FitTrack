package com.fittrack.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_log")
public class WorkoutLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String exerciseName;

    public int sets;
    public int reps;
    public float weightKg;
    public double caloriesBurned;

    @NonNull
    public String date; // yyyy-MM-dd

    public WorkoutLog(@NonNull String exerciseName, int sets, int reps, float weightKg, double caloriesBurned, @NonNull String date) {
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.weightKg = weightKg;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }
}
