package com.example.PRISM.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorReadingEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long machineId;
    private String sensorType;
    @Column(name = "reading_value")
    private Double value;
    private String unit;
    private boolean anomaly;
    private LocalDateTime timestamp;
}