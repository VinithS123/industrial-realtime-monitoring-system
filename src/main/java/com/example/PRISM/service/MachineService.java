package com.example.PRISM.service;

import com.example.PRISM.dto.MachineDto;
import com.example.PRISM.dto.MachineResponseDto;
import com.example.PRISM.entity.MachineEntity;
import com.example.PRISM.entity.SensorReadingEntity;
import com.example.PRISM.exception.MachineNotFoundException;
import com.example.PRISM.mapper.MachineMapper;
import com.example.PRISM.repository.MachineRepository;
import com.example.PRISM.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;

    private final SensorReadingRepository sensorRepo;

    private final MachineMapper machineMapper;

    public List<MachineDto> getAllMachines(int page, int size, String orderBy, String orderAs, boolean fetchAll) {

        if(fetchAll){
            return machineMapper.toMachineDtoList(machineRepository.findAll());
        }
        Sort sort = null;
        if(orderAs.equals("DESC")){
            sort = Sort.by(orderBy).descending();
        }
        sort = Sort.by(orderBy).ascending();
        return machineMapper.toMachineDtoList(machineRepository.findAll(PageRequest.of(page,size,sort)).getContent());

    }

    public MachineDto addMachine(MachineDto machineDto) {
        MachineEntity machine = machineMapper.toMachineEntity(machineDto);
        return machineMapper.toMachineDto(machineRepository.save(machine));
    }


    public MachineResponseDto getLatestInfo(Long id) {
        MachineEntity machine = machineRepository.findById(id).orElseThrow(
                ()-> new MachineNotFoundException("machine not found"));

        List<SensorReadingEntity> sensorReading = sensorRepo.findTop10ByMachineIdOrderByTimestampDesc(id);

        return MachineResponseDto.builder()
                .machineDto(machineMapper.toMachineDto(machine))
                .sensorReadingDto(machineMapper.toSensorReadtingDtoList(sensorReading))
                .build();
    }

    public void updateStatus(Long id, String status) {
        MachineEntity machine = machineRepository.findById(id).orElseThrow(
                ()-> new MachineNotFoundException("machine not found"));
        machine.setStatus(status);
        machineRepository.save(machine);
    }
}
