package com.fittrack.app.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fittrack.app.auth.AuthActivity;
import com.fittrack.app.data.local.AppDatabase;
import com.fittrack.app.databinding.FragmentProfileBinding;
import com.fittrack.app.onboarding.OnboardingActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    saveAvatarToInternalStorage(uri);
                }
            });

    private void saveAvatarToInternalStorage(Uri sourceUri) {
        if (getContext() == null) return;
        Context appContext = getContext().getApplicationContext();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try (InputStream inputStream = appContext.getContentResolver().openInputStream(sourceUri)) {
                if (inputStream == null) return;

                File avatarFile = new File(appContext.getFilesDir(), "user_avatar.jpg");
                try (FileOutputStream outputStream = new FileOutputStream(avatarFile)) {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                }

                Uri internalUri = Uri.fromFile(avatarFile);
                viewModel.updateAvatar(internalUri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.tvEmail.setText(viewModel.currentEmail());

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile == null) return;
            binding.tvName.setText(profile.name);
            binding.tvAgeGender.setText(String.format(Locale.US, "%d years · %s", profile.age, profile.gender));
            binding.tvHeightWeight.setText(String.format(Locale.US, "%.0f cm · %.1f kg", profile.heightCm, profile.weightKg));
            binding.tvActivityLevel.setText(profile.activityLevel);
            binding.tvGoal.setText(profile.goal);
            binding.tvBmrValue.setText(String.format(Locale.US, "%.0f kcal", profile.bmr));
            binding.tvTdeeValue.setText(String.format(Locale.US, "%.0f kcal", profile.tdee));

            if (profile.avatarUri != null && !profile.avatarUri.isEmpty()) {
                binding.ivAvatar.setImageURI(null);
                binding.ivAvatar.setImageURI(Uri.parse(profile.avatarUri));
            }
        });

        binding.ivAvatar.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.btnEditProfile.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), OnboardingActivity.class)));

        binding.btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout from FitTrack?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    viewModel.logout();
                    Intent intent = new Intent(requireContext(), AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
