package com.fittrack.app.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Shared ViewModel for AuthActivity + LoginFragment + RegisterFragment.
 */
public class AuthViewModel extends ViewModel {

    /** Result of login/register: success (with user) or failure (with message). */
    public static final class AuthOutcome {
        public final boolean success;
        public final FirebaseUser user;
        public final String errorMessage;

        private AuthOutcome(boolean success, FirebaseUser user, String errorMessage) {
            this.success = success;
            this.user = user;
            this.errorMessage = errorMessage;
        }

        static AuthOutcome success(FirebaseUser user) {
            return new AuthOutcome(true, user, null);
        }

        static AuthOutcome failure(String message) {
            return new AuthOutcome(false, null, message);
        }
    }

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final MutableLiveData<AuthOutcome> authResult = new MutableLiveData<>();
    public LiveData<AuthOutcome> getAuthResult() {
        return authResult;
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public FirebaseUser currentUser() {
        return auth.getCurrentUser();
    }

    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            authResult.setValue(AuthOutcome.failure("Please enter both Email and Password"));
            return;
        }
        isLoading.setValue(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    isLoading.setValue(false);
                    authResult.setValue(AuthOutcome.success(result.getUser()));
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authResult.setValue(AuthOutcome.failure(e.getMessage()));
                });
    }

    public void register(String email, String password, String confirmPassword) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            authResult.setValue(AuthOutcome.failure("Please enter both Email and Password"));
            return;
        }
        if (password.length() < 6) {
            authResult.setValue(AuthOutcome.failure("Password must be at least 6 characters"));
            return;
        }
        if (!password.equals(confirmPassword)) {
            authResult.setValue(AuthOutcome.failure("Passwords do not match"));
            return;
        }
        isLoading.setValue(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    isLoading.setValue(false);
                    authResult.setValue(AuthOutcome.success(result.getUser()));
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authResult.setValue(AuthOutcome.failure(e.getMessage()));
                });
    }

    public void logout() {
        auth.signOut();
    }
}
