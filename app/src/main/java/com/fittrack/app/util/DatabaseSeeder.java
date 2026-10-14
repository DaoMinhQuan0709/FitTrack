package com.fittrack.app.util;

import android.content.Context;

import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.data.local.entity.FoodLog;
import com.fittrack.app.data.local.entity.WaterLog;
import com.fittrack.app.data.local.entity.WeightEntry;
import com.fittrack.app.data.local.entity.WorkoutLog;

import java.util.Random;

public class DatabaseSeeder {

    public static void seedIfNeeded(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        if (db.foodLogDao().getCount() == 0) {
            seedData(db);
        }
    }

    private static void seedData(AppDatabase db) {
        Random random = new Random();
        String today = DateUtils.today();

        for (int i = 30; i >= 0; i--) {
            String date = DateUtils.offsetDate(today, -i);

            // Seed Food
            db.foodLogDao().insert(new FoodLog("Beef Pho", 450, 25, 60, 15, "Breakfast", date));
            db.foodLogDao().insert(new FoodLog("Chicken Rice", 600, 30, 80, 20, "Lunch", date));
            db.foodLogDao().insert(new FoodLog("Grilled Pork", 550, 20, 70, 25, "Dinner", date));
            db.foodLogDao().insert(new FoodLog("Red Apple", 80, 0.5, 20, 0.2, "Snack", date));

            if (random.nextBoolean()) {
                db.foodLogDao().insert(new FoodLog("Greek Yogurt", 120, 5, 15, 3, "Snack", date));
            }

            // Seed Water
            if (i == 0) {
                db.waterLogDao().insert(new WaterLog(250, 8, date));
                db.waterLogDao().insert(new WaterLog(500, 10, date));
                db.waterLogDao().insert(new WaterLog(250, 13, date));
                db.waterLogDao().insert(new WaterLog(500, 16, date));
                db.waterLogDao().insert(new WaterLog(250, 20, date));
            } else {
                int waterAmt = 1500 + random.nextInt(1000);
                db.waterLogDao().insert(new WaterLog(waterAmt, 12, date));
            }

            // Seed Workout
            double burnedBase = 300 + random.nextInt(300);
            db.workoutLogDao().insert(new WorkoutLog("Push Ups", 3, 15, 0, burnedBase * 0.4, date));
            db.workoutLogDao().insert(new WorkoutLog("Squats", 3, 20, 20, burnedBase * 0.6, date));

            // Seed Weight with realistic fluctuations (drops ~1.5-2kg then rises ~0.8-1kg)
            int dayOffset = 30 - i;
            float baseWeight = 76.5f - (dayOffset / 30.0f) * 2.0f; // Overall trend 76.5 -> 74.5 kg
            double wave = Math.sin(dayOffset * 0.45) * 1.2; // Realistically drops ~2kg & recovers ~1kg
            float noise = (random.nextFloat() - 0.5f) * 0.4f;

            float weight = Math.round((baseWeight + wave + noise) * 10.0f) / 10.0f;
            db.weightEntryDao().insert(new WeightEntry(weight, date));
        }
    }
}
