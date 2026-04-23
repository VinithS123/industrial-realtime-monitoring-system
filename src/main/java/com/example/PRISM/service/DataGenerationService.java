package com.example.PRISM.service;

import com.example.PRISM.entity.MachineEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DataGenerationService {

    private final Random random = new Random();

    // Logic for reading generation
     public double calculateValue(MachineEntity machine, String sensorType, double min, double max, Map<String, Double> lastValues) {
        String key = machine.getId() + "_" + sensorType;

        // if status sabotage
        if ("sabotage".equals(machine.getStatus())) {

            if (random.nextBoolean() && lastValues.containsKey(key)) {
                return lastValues.get(key);
            }
            return max * 1.5;
        }

        double safeMax = max * 0.85;
        return min + (random.nextDouble() * (safeMax - min));
    }
}
