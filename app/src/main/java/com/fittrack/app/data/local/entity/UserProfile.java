package com.fittrack.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * User Profile. Single-user local app, always uses id = 1.
 */
@Entity(tableName = "user_profile")
public class UserProfile {

    @PrimaryKey
    public int id = 1;

    @NonNull
    public String firebaseUid;

    @NonNull
    public String name;

    public int age;

    @NonNull
    public String gender; // "Male" | "Female"

    public float heightCm;
    public float weightKg;

    @NonNull
    public String activityLevel; 

    @NonNull
    public String goal; 

    public double bmr;
    public double tdee;
    public double calorieGoal;
    public double proteinGoalG;
    public double carbGoalG;
    public double fatGoalG;

    public String avatarUri; // Local URI of profile picture

    public UserProfile() {
        this.firebaseUid = "";
        this.name = "";
        this.gender = "";
        this.activityLevel = "";
        this.goal = "";
    }

    public UserProfile(@NonNull String firebaseUid, @NonNull String name, int age, @NonNull String gender,
                        float heightCm, float weightKg, @NonNull String activityLevel, @NonNull String goal,
                        double bmr, double tdee, double calorieGoal,
                        double proteinGoalG, double carbGoalG, double fatGoalG) {
        this.id = 1;
        this.firebaseUid = firebaseUid;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.bmr = bmr;
        this.tdee = tdee;
        this.calorieGoal = calorieGoal;
        this.proteinGoalG = proteinGoalG;
        this.carbGoalG = carbGoalG;
        this.fatGoalG = fatGoalG;
    }
}
