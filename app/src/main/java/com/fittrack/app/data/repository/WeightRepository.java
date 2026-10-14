package com.fittrack.app.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.data.local.dao.WeightEntryDao;
import com.fittrack.app.data.local.entity.WeightEntry;

import java.util.List;

public class WeightRepository {

    private final WeightEntryDao dao;

    public WeightRepository(Context context) {
        dao = AppDatabase.getInstance(context).weightEntryDao();
    }

    public LiveData<List<WeightEntry>> getAllWeights() {
        return dao.getAllWeights();
    }

    public LiveData<WeightEntry> getLatestWeight() {
        return dao.getLatestWeight();
    }

    public void insert(WeightEntry weightEntry) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insert(weightEntry));
    }

    public void delete(WeightEntry weightEntry) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.delete(weightEntry));
    }
}
