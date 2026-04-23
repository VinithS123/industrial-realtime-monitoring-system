package com.example.PRISM.service;

import com.example.PRISM.entity.MachineEntity;
import com.example.PRISM.entity.SensorReadingEntity;
import com.example.PRISM.exception.MachineNotFoundException;
import com.example.PRISM.repository.MachineRepository;
import com.example.PRISM.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final MachineRepository machineRepository;

    private final SensorReadingRepository sensorRepository;

    public String extractMachineCode(String userQuery){
        Pattern pattern = Pattern.compile("[A-Z]{1,2}-\\d{3}");
        Matcher matcher = pattern.matcher(userQuery);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String generatePrompt(String machineCode, String message) {

        MachineEntity machine = machineRepository.findByCode(machineCode).orElseThrow(
                ()-> new MachineNotFoundException("Invalid Machine Mode"));


        String promptTemplate =  """
                You are an Industrial Maintenance AI Assistant.
                Keep all answers extremely concise and direct.
                avoid unnecessary symbols.
                User Query:
                            %s
                            
                Machine Details:
                            %s
                
                Sensor Data:
                            %s
                
                            Instructions:
                            - Prioritize safety
                            - Use only given data
                            - Give step-by-step response
                            - Highlight risks clearly   """;

        return String.format(promptTemplate,message,machineDetailsPrompt(machine),sensorPrompt(machine.getId()));
    }

    private String sensorPrompt(Long machineId){
        List<SensorReadingEntity> sensorReading = sensorRepository.findTop20ByMachineIdOrderByTimestampDesc(machineId);
        Map<String,List<SensorReadingEntity>> listBySensorMap = sensorReading.stream()
                .collect(Collectors.groupingBy(SensorReadingEntity::getSensorType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                reading -> reading.stream()
                                        .sorted(Comparator.comparing((SensorReadingEntity::getTimestamp)))
                                        .toList())));

        return " Sensor Readings : "+ listBySensorMap;
    }

    private String machineDetailsPrompt(MachineEntity machine){
        return """ 
                name: %s,
                type: %s,
                code: %s
                status: %s
                location: %s
                """.formatted(machine.getName(),machine.getType(),machine.getCode(),
                              machine.getStatus(),machine.getLocation());
    }
}
