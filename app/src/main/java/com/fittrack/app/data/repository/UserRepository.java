package com.fittrack.app.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.data.local.dao.UserProfileDao;
import com.fittrack.app.data.local.entity.UserProfile;

public class UserRepository {

    private final UserProfileDao dao;

    public UserRepository(Context context) {
        dao = AppDatabase.getInstance(context).userProfileDao();
    }

    public LiveData<UserProfile> getUserProfile() {
        return dao.getUserProfile();
    }

    /** Synchronous read - only call from background thread. */
    public UserProfile getUserProfileOnce() {
        return dao.getUserProfileOnce();
    }

    /** Asynchronous read, results via callback on main thread. */
    public void getUserProfileOnceAsync(Callback<UserProfile> callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserProfile result = dao.getUserProfileOnce();
            callback.onResult(result);
        });
    }

    public boolean hasProfile() {
        return dao.getUserProfileOnce() != null;
    }

    public void saveProfile(UserProfile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insertOrUpdate(profile));
    }

    public void updateAvatar(String uri) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserProfile profile = dao.getUserProfileOnce();
            if (profile != null) {
                profile.avatarUri = uri;
                dao.insertOrUpdate(profile);
            }
        });
    }

    public void clearProfile() {
        AppDatabase.databaseWriteExecutor.execute(dao::clear);
    }

    public interface Callback<T> {
        void onResult(T result);
    }
}
