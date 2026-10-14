package com.fittrack.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.fittrack.app.data.local.entity.WaterLog;
import com.fittrack.app.data.local.model.HourlyTotal;

import java.util.List;

@Dao
public interface WaterLogDao {

    @Query("SELECT * FROM water_log WHERE date = :date")
    LiveData<List<WaterLog>> getWaterLogsByDate(String date);

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_log WHERE date = :date")
    LiveData<Integer> getTotalWaterByDate(String date);

    @Query("SELECT hour, SUM(amountMl) as total FROM water_log WHERE date = :date GROUP BY hour")
    LiveData<List<HourlyTotal>> getHourlyWaterByDate(String date);

    @Insert
    void insert(WaterLog waterLog);

    @Delete
    void delete(WaterLog waterLog);

    @Query("DELETE FROM water_log WHERE date = :date")
    void clearDay(String date);
}
