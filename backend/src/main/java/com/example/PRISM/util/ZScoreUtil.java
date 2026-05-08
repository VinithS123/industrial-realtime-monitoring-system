package com.example.PRISM.util;

import java.util.List;

public class ZScoreUtil {

    public static double calculateZScore(List<Double> history, double currentValue) {
        if (history.isEmpty()) return 0.0;

        double sum = 0;
        for (double val : history) {
            sum += val;
        }
        double mean = sum / history.size();

        double standardDeviation = 0;
        for (double val : history) {
            standardDeviation += Math.pow(val - mean, 2);
        }
        standardDeviation = Math.sqrt(standardDeviation / history.size());

        if (standardDeviation == 0) return 0.0;

        return (currentValue - mean) / standardDeviation;
    }
}
