package com.example.PRISM.repository;

import com.example.PRISM.entity.SensorReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReadingEntity, Long> {

    List<SensorReadingEntity> findTop10ByMachineIdOrderByTimestampDesc(Long machineId);

    List<SensorReadingEntity> findTop20ByMachineIdOrderByTimestampDesc(Long machineId);

}

