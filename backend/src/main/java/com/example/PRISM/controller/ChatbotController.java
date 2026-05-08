package com.example.PRISM.controller;

import com.example.PRISM.dto.ChatDto;
import com.example.PRISM.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatDto> chat(@RequestBody ChatDto chatDto) {
        return ResponseEntity.ok(chatbotService.getResponse(chatDto));
    }
}
