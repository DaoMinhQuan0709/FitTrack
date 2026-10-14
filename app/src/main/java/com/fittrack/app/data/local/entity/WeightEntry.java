package com.fittrack.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weight_entry")
public class WeightEntry {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public float weightKg;

    @NonNull
    public String date; // yyyy-MM-dd

    public WeightEntry(float weightKg, @NonNull String date) {
        this.weightKg = weightKg;
        this.date = date;
    }
}
