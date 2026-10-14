# Implementation Plan - Project Upgrade & Fix ViewModel Errors

Upgrade the project to latest stable versions of Gradle, AGP, and dependencies, while fixing the `ViewModelProvider` compatibility issue in Java.

## User Review Required

> [!WARNING]
> Upgrading to the latest AGP (9.3.1) and Gradle (8.13) might require significant changes to the build scripts. I will proceed carefully.
> To fix the `ViewModelProvider` private access error in Java, I will use the `-android` specific artifacts for Lifecycle 2.8+, which are designed to maintain Java compatibility.

## Proposed Changes

### Build Configuration

#### [MODIFY] [build.gradle (root)](file:///C:/Users/laptop/Downloads/FitTrack_Java/build.gradle)
- Set AGP version to `8.7.3` (stable) or `9.3.1` as requested. I'll stick to a high stable version like `8.7.3` unless `9.3.1` is confirmed compatible with the current project structure.

#### [MODIFY] [build.gradle (app)](file:///C:/Users/laptop/Downloads/FitTrack_Java/app/build.gradle)
- Upgrade dependencies:
    - `androidx.core:core:1.13.1` -> `1.15.0`
    - `androidx.appcompat:appcompat:1.7.0` -> `1.7.0` (stable)
    - `com.google.android.material:material:1.12.0` -> `1.12.0` (stable)
    - `androidx.lifecycle:lifecycle-viewmodel:2.8.2` -> `androidx.lifecycle:lifecycle-viewmodel-android:2.8.7`
    - `androidx.lifecycle:lifecycle-livedata:2.8.2` -> `androidx.lifecycle:lifecycle-livedata-android:2.8.7`
    - `androidx.lifecycle:lifecycle-runtime:2.8.2` -> `androidx.lifecycle:lifecycle-runtime-android:2.8.7`
    - `androidx.room:room-runtime:2.6.1` -> `2.6.1`

### Auth Authentication

#### [MODIFY] [AuthActivity.java](file:///C:/Users/laptop/Downloads/FitTrack_Java/app/src/main/java/com/fittrack/app/auth/AuthActivity.java)
- Convert `authViewModel` field to a local variable.

## Verification Plan

### Automated Tests
- Run `gradle_sync`.
- Run `analyze_file` on `AuthActivity.java`.
- Run `gradle_build` to ensure the project still compiles.
