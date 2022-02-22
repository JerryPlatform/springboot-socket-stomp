package com.example.projectj.controller;


import com.example.projectj.dto.KafkaDto;
import com.example.projectj.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaService kafkaService;

    @PostMapping("/test")
    public String sendKafkaMessage(@RequestBody KafkaDto dto) {
        kafkaService.sendKafkaMessage(dto.getMessage());
        return "success";
    }
}
