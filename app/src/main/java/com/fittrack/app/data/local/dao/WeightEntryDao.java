package com.fittrack.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.fittrack.app.data.local.entity.WeightEntry;

import java.util.List;

@Dao
public interface WeightEntryDao {

    @Query("SELECT * FROM weight_entry ORDER BY date ASC, id ASC")
    LiveData<List<WeightEntry>> getAllWeights();

    @Query("SELECT * FROM weight_entry ORDER BY date DESC, id DESC LIMIT 1")
    LiveData<WeightEntry> getLatestWeight();

    @Insert
    void insert(WeightEntry weightEntry);

    @Delete
    void delete(WeightEntry weightEntry);
}
