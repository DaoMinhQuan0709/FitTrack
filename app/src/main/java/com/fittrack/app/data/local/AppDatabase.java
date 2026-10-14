package com.fittrack.app.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fittrack.app.data.local.dao.FoodLogDao;
import com.fittrack.app.data.local.dao.UserProfileDao;
import com.fittrack.app.data.local.dao.WaterLogDao;
import com.fittrack.app.data.local.dao.WeightEntryDao;
import com.fittrack.app.data.local.dao.WorkoutLogDao;
import com.fittrack.app.data.local.entity.FoodLog;
import com.fittrack.app.data.local.entity.UserProfile;
import com.fittrack.app.data.local.entity.WaterLog;
import com.fittrack.app.data.local.entity.WeightEntry;
import com.fittrack.app.data.local.entity.WorkoutLog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {UserProfile.class, FoodLog.class, WorkoutLog.class, WeightEntry.class, WaterLog.class},
        version = 12,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserProfileDao userProfileDao();

    public abstract FoodLogDao foodLogDao();

    public abstract WorkoutLogDao workoutLogDao();

    public abstract WeightEntryDao weightEntryDao();

    public abstract WaterLogDao waterLogDao();

    private static volatile AppDatabase INSTANCE;

    /** Shared executor to run Room read/write operations off the main thread (replacing coroutines). */
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fittrack_database"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
