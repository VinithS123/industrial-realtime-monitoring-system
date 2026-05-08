package com.example.PRISM.service;

import com.example.PRISM.entity.MachineEntity;
import com.example.PRISM.entity.SensorReadingEntity;
import com.example.PRISM.repository.MachineRepository;
import com.example.PRISM.repository.SensorReadingRepository;
import com.example.PRISM.util.RegressionUtil;
import com.example.PRISM.util.ZScoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final MachineRepository machineRepo;
    private final SensorReadingRepository sensorRepo;
    private final AlertService alertService;
    private final DataGenerationService dataGenerationService;

    private final Map<String, LinkedList<Double>> historyBuffer = new HashMap<>();

    private final Map<String, Double> lastValues = new HashMap<>();
    private final Map<String, Integer> stickyCounter = new HashMap<>();


    @Scheduled(fixedRate = 3000)
    public void generateData() {
        List<MachineEntity> machines = machineRepo.findAll();
        for (MachineEntity machine : machines) {
            if (shouldSkipSimulation(machine)) continue;
            startSimulation(machine);
        }
    }

    private boolean shouldSkipSimulation(MachineEntity machine) {
        return "shutdown".equals(machine.getStatus()) || "maintenance".equals(machine.getStatus());
    }

    private void startSimulation(MachineEntity machine) {
        switch (machine.getType()) {
            case "Reactor" -> {
                startSensor(machine, "Temperature", 20, 250, "°C");
                startSensor(machine, "Pressure", 1, 50, "bar");
                startSensor(machine, "Flow Rate", 0, 100, "m³/h");
                startSensor(machine, "Level", 0, 100, "%");
            }
            case "Fermenter" -> {
                startSensor(machine, "Temperature", 30, 40, "°C");
                startSensor(machine, "pH", 4.0, 9.0, "pH");
                startSensor(machine, "Dissolved Oxygen", 20, 100, "%");
            }
            case "Distillation" -> {
                startSensor(machine, "Top Temperature", 60, 110, "°C");
                startSensor(machine, "Bottom Temperature", 100, 180, "°C");
                startSensor(machine, "Pressure", 0.5, 5.0, "atm");
            }
            case "Exchanger" -> {
                startSensor(machine, "Inlet Temperature", 80, 150, "°C");
                startSensor(machine, "Outlet Temperature", 40, 90, "°C");
                startSensor(machine, "Pressure Drop", 0.1, 2.0, "bar");
            }
            case "Centrifuge" -> {
                startSensor(machine, "Rotation Speed", 1000, 10000, "RPM");
                startSensor(machine, "Vibration", 0, 5, "mm/s");
            }
            case "Pump" -> {
                startSensor(machine, "Output Pressure", 1, 10, "bar");
                startSensor(machine, "Motor Temp", 40, 90, "°C");
            }
            default -> {
                System.out.println("Machine Not Supported");
            }
        }
    }

    // Sensor
    private void startSensor(MachineEntity machine, String sensorType, double min, double max, String unit) {

        double val = dataGenerationService.calculateValue(machine, sensorType, min, max,lastValues);

        boolean isSabotage = "sabotage".equals(machine.getStatus());
        boolean isAnomaly = val > max || isSabotage;

        saveReading(machine, sensorType, val, unit, isAnomaly);

        // Algorithm
        runAlgorithms(machine, sensorType, val, max, isSabotage, isAnomaly);

        // Sensor Health Check
        checkSensorHealth(machine, sensorType, val);

        // Trigger Alarm
        if (isAnomaly && isSabotage && val > max) {
            System.out.println(" In Critical state");
            alertService.triggerEmergency(machine, "CRITICAL: " + sensorType + " " + String.format("%.2f", val) + unit);
        }
    }


    public void runAlgorithms(MachineEntity machine, String sensorType,
                              double val, double max, boolean isSabotage,
                              boolean isAnomaly) {
        String key = machine.getId() + "_" + sensorType;
        historyBuffer.putIfAbsent(key, new LinkedList<>());
        LinkedList<Double> history = historyBuffer.get(key);

        history.add(val);
        if (history.size() > 5) {
            history.removeFirst();
        }

        if (history.size() == 5) {
            // Linear Regression
            double slope = RegressionUtil.calculateSlope(history);
            if (slope > 0 && (val > max * 0.85 || isSabotage)) {
                if (slope > 0.1) {
                    double distanceToMax = max - val;
                    double seconds = (distanceToMax / slope) * 3.0;
                    if (seconds > 0 && seconds < 45) {
                        alertService.triggerEmergency(machine, "AI PREDICTION: " + sensorType + " Failure in " + (int)seconds + "s!");
                    }
                }
            }
            // Z-Score
            double zScore = ZScoreUtil.calculateZScore(history, val);
            if (Math.abs(zScore) > 3.0 && !isAnomaly) {
                System.out.println("Z-Score Alert: " + sensorType + " Value=" + val + " Z=" + zScore);
            }
        }
    }

    private void checkSensorHealth(MachineEntity machine, String sensorType, double currentVal) {
        String key = machine.getId() + "_" + sensorType;

        Double lastVal = lastValues.get(key);

        if (lastVal != null && Double.compare(lastVal, currentVal) == 0) {
            int count = stickyCounter.getOrDefault(key, 0) + 1;
            stickyCounter.put(key, count);

            if (count == 5) {
                System.out.println("SENSOR FAILURE: " + sensorType + " is frozen.");
                alertService.triggerEmergency(machine, "SENSOR FAILURE: " + sensorType + " Signal Lost (Frozen Value)");
            }
        } else {
            // Value changed, reset counter
            stickyCounter.put(key, 0);
        }

        // Update memory
        lastValues.put(key, currentVal);
    }

    private void saveReading(MachineEntity machine, String sensorType, double val, String unit, boolean isAnomaly) {
        SensorReadingEntity reading = new SensorReadingEntity();
        reading.setMachineId(machine.getId());
        reading.setSensorType(sensorType);
        reading.setValue(val);
        reading.setUnit(unit);
        reading.setTimestamp(LocalDateTime.now());
        reading.setAnomaly(isAnomaly);
        sensorRepo.save(reading);
    }

}