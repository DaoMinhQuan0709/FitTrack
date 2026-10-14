package com.fittrack.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_log")
public class FoodLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    public double calories;
    public double proteinG;
    public double carbG;
    public double fatG;

    @NonNull
    public String mealType; // "Breakfast" | "Lunch" | "Dinner" | "Snack"

    @NonNull
    public String date; // yyyy-MM-dd

    public FoodLog(@NonNull String name, double calories, double proteinG, double carbG,
                    double fatG, @NonNull String mealType, @NonNull String date) {
        this.name = name;
        this.calories = calories;
        this.proteinG = proteinG;
        this.carbG = carbG;
        this.fatG = fatG;
        this.mealType = mealType;
        this.date = date;
    }
}
