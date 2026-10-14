package com.fittrack.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "water_log")
public class WaterLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int amountMl;
    public int hour; // 0-23

    @NonNull
    public String date; // yyyy-MM-dd

    public WaterLog(int amountMl, int hour, @NonNull String date) {
        this.amountMl = amountMl;
        this.hour = hour;
        this.date = date;
    }
}
