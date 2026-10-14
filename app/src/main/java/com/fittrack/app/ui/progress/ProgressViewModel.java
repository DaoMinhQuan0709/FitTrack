package com.fittrack.app.ui.progress;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.entity.WeightEntry;
import com.fittrack.app.data.repository.WeightRepository;
import com.fittrack.app.util.DateUtils;

import java.util.List;

public class ProgressViewModel extends AndroidViewModel {

    private final WeightRepository repo;
    private final LiveData<List<WeightEntry>> weightHistory;

    public ProgressViewModel(@NonNull Application application) {
        super(application);
        repo = new WeightRepository(application);
        weightHistory = repo.getAllWeights();
    }

    public LiveData<List<WeightEntry>> getWeightHistory() {
        return weightHistory;
    }

    public void addWeight(float weightKg) {
        repo.insert(new WeightEntry(weightKg, DateUtils.today()));
    }
}
