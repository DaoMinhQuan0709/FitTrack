package com.fittrack.app.util;

/**
 * Mifflin-St Jeor Formula for BMR and TDEE calculation.
 */
public final class TDEECalculator {

    private TDEECalculator() {
        // utility class
    }

    public static double calculateBMR(String gender, float weightKg, float heightCm, int age) {
        double base = 10 * weightKg + 6.25 * heightCm - 5 * age;
        return "Male".equals(gender) ? base + 5 : base - 161;
    }

    public static double activityMultiplier(String level) {
        if (level == null) return 1.2;
        switch (level) {
            case "Sedentary":
                return 1.2;
            case "Lightly Active":
                return 1.375;
            case "Moderately Active":
                return 1.55;
            case "Very Active":
                return 1.725;
            case "Extra Active":
                return 1.9;
            default:
                return 1.2;
        }
    }

    public static double calculateTDEE(double bmr, String activityLevel) {
        return bmr * activityMultiplier(activityLevel);
    }

    public static double calculateCalorieGoal(double tdee, String goal) {
        if ("Lose Weight".equals(goal)) {
            return tdee - 500;
        } else if ("Gain Weight".equals(goal)) {
            return tdee + 300;
        } else {
            return tdee; // Maintain
        }
    }

    public static MacroResult calculateMacros(double calorieGoal) {
        double proteinG = (calorieGoal * 0.30) / 4;
        double carbG = (calorieGoal * 0.40) / 4;
        double fatG = (calorieGoal * 0.30) / 9;
        return new MacroResult(proteinG, carbG, fatG);
    }

    public static final class MacroResult {
        public final double proteinG;
        public final double carbG;
        public final double fatG;

        public MacroResult(double proteinG, double carbG, double fatG) {
            this.proteinG = proteinG;
            this.carbG = carbG;
            this.fatG = fatG;
        }
    }
}
