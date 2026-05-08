package com.example.PRISM.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MachineResponseDto {

    MachineDto machineDto;

    List<SensorReadingDto> sensorReadingDto;

}
