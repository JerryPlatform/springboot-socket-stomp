package com.example.projectj.service.Impl;

import com.example.projectj.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log
@Service
@Transactional
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendKafkaMessage(String message) {
        kafkaTemplate.send("test", message);
    }

    @KafkaListener(topics = "test", groupId = "koo")
    public void getKafkaMessage(String message) {
        log.info("Kafka message : " + message);
    }
}
