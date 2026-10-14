package com.fittrack.app.onboarding;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.data.local.entity.UserProfile;
import com.fittrack.app.data.repository.UserRepository;
import com.fittrack.app.util.TDEECalculator;
import com.google.firebase.auth.FirebaseAuth;

public class OnboardingViewModel extends AndroidViewModel {

    private final UserRepository repo;

    private final MutableLiveData<UserProfile> existingProfile = new MutableLiveData<>();
    public LiveData<UserProfile> getExistingProfile() {
        return existingProfile;
    }

    private final MutableLiveData<Boolean> saveComplete = new MutableLiveData<>(false);
    public LiveData<Boolean> getSaveComplete() {
        return saveComplete;
    }

    // Step data
    public String name = "";
    public int age = 25;
    public String gender = "Male";
    public float height = 170f;
    public float weight = 65f;
    public String activityLevel = "Sedentary";
    public String goal = "Maintain";

    public OnboardingViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserProfile profile = repo.getUserProfileOnce();
            if (profile != null) {
                existingProfile.postValue(profile);
                name = profile.name;
                age = profile.age;
                gender = profile.gender;
                height = profile.heightCm;
                weight = profile.weightKg;
                activityLevel = profile.activityLevel;
                goal = profile.goal;
            }
        });
    }

    public void saveProfile() {
        double bmr = TDEECalculator.calculateBMR(gender, weight, height, age);
        double tdee = TDEECalculator.calculateTDEE(bmr, activityLevel);
        double calorieGoal = TDEECalculator.calculateCalorieGoal(tdee, goal);
        TDEECalculator.MacroResult macros = TDEECalculator.calculateMacros(calorieGoal);

        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "";

        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserProfile existing = repo.getUserProfileOnce();
            String existingAvatar = existing != null ? existing.avatarUri : null;

            UserProfile profile = new UserProfile(
                    uid, name, age, gender, height, weight, activityLevel, goal,
                    bmr, tdee, calorieGoal, macros.proteinG, macros.carbG, macros.fatG
            );
            profile.avatarUri = existingAvatar;

            repo.saveProfile(profile);
            saveComplete.postValue(true);
        });
    }

    @Deprecated
    public void saveProfile(String name, int age, String gender, float heightCm, float weightKg,
                             String activityLevel, String goal) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = heightCm;
        this.weight = weightKg;
        this.activityLevel = activityLevel;
        this.goal = goal;
        saveProfile();
    }
}
