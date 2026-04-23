package com.example.PRISM.mapper;

import com.example.PRISM.dto.MachineDto;
import com.example.PRISM.dto.SensorReadingDto;
import com.example.PRISM.entity.MachineEntity;
import com.example.PRISM.entity.SensorReadingEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MachineMapper {


    List<MachineDto> toMachineDtoList(List<MachineEntity> machineEntityList);

    MachineEntity toMachineEntity(MachineDto machineDto);

    MachineDto toMachineDto(MachineEntity machineEntity);

    List<SensorReadingDto> toSensorReadtingDtoList(List<SensorReadingEntity> sensorReading);

}
