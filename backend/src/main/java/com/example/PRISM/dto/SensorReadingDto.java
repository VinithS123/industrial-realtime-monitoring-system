package com.example.PRISM.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SensorReadingDto {

    private Long id;
    private Long machineId;
    private String sensorType;
    private Double value;
    private String unit;
    private boolean anomaly;
    private LocalDateTime timestamp;
}
