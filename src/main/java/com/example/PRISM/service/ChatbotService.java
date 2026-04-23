package com.example.PRISM.service;

import com.example.PRISM.dto.ChatDto;
import com.example.PRISM.repository.MachineRepository;
import com.example.PRISM.repository.SensorReadingRepository;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final MachineRepository machineRepo;

    private final SensorReadingRepository sensorRepository;

    private final PromptService promptService;

    private final Client client;

    public ChatDto getResponse(ChatDto chatDto) {

        String machineCode = promptService.extractMachineCode(chatDto.getMessage());

        if(machineCode==null){
            return new ChatDto("Please enter machine code in prompt");
        }

        String MachineQueryPrompt = promptService.generatePrompt(machineCode,chatDto.getMessage());
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        MachineQueryPrompt,
                        null);

        return new ChatDto(response.text());

    }

}
