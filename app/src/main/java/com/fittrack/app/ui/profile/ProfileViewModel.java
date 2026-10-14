package com.fittrack.app.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fittrack.app.data.local.entity.UserProfile;
import com.fittrack.app.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileViewModel extends AndroidViewModel {

    private final UserRepository repo;
    private final LiveData<UserProfile> userProfile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
        userProfile = repo.getUserProfile();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public String currentEmail() {
        return FirebaseAuth.getInstance().getCurrentUser() != null
                && FirebaseAuth.getInstance().getCurrentUser().getEmail() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getEmail()
                : "";
    }

    public void updateAvatar(String uri) {
        repo.updateAvatar(uri);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
