package com.example.PRISM.util;

import java.util.List;

public class RegressionUtil {

    public static double calculateSlope(List<Double> yValues) {
        int n = yValues.size();
        if (n < 2) return 0.0;

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = i + 1;
            double y = yValues.get(i);

            sumX += x;
            sumY += y;
            sumXY += (x * y);
            sumX2 += (x * x);
        }

        // calculation slope
        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    }
}
