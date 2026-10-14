package com.fittrack.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fittrack.app.data.local.entity.UserProfile;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    LiveData<UserProfile> getUserProfile();

    @Query("SELECT * FROM user_profile WHERE id = 1")
    UserProfile getUserProfileOnce();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(UserProfile profile);

    @Query("DELETE FROM user_profile")
    void clear();
}
