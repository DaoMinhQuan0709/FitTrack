# Implementation Plan - Convert Dashboard 7-Day Calories Burned Chart to BarChart

Convert the 7-day calories burned chart on the Dashboard screen from `LineChart` to `BarChart` to provide a clearer, discrete daily activity visual representation.

## Proposed Changes

### 1. Dashboard Layout
#### [MODIFY] [fragment_dashboard.xml](file:///C:/Users/laptop/Downloads/FitTrack_Java/app/src/main/res/layout/fragment_dashboard.xml)
- Replace `<com.github.mikephil.charting.charts.LineChart>` with `<com.github.mikephil.charting.charts.BarChart>` and change the ID to `@+id/barChartBurned`.

### 2. Dashboard View Logic
#### [MODIFY] [DashboardFragment.java](file:///C:/Users/laptop/Downloads/FitTrack_Java/app/src/main/java/com/fittrack/app/ui/dashboard/DashboardFragment.java)
- Update `updateTrendChart(List<DailyTotal> trend)` to configure `BarChart`:
  - Use `BarEntry`, `BarDataSet`, and `BarData`.
  - Set bar color to `@color/accent_green`.
  - Set `setFitBars(true)` to ensure bars fit nicely within the chart viewport.
  - Retain the X-axis date formatter (`dd/MM`).
  - Set left axis minimum to `0f` for consistent bar baselines.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify compilation and ViewBinding code generation.

### Manual Verification
- Launch the app and check the **Dashboard** screen.
- Verify that the 7-day calories burned section displays a clean **Bar Chart (biểu đồ cột)** with daily date labels (`dd/MM`) on the X-axis and green bars.
