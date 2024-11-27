package ru.simbir.health.accountservice.features.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    public final KafkaProducer kafkaProducer;

    @GetMapping("/publish")
    public String publishMessage(@RequestParam("message") String message) {
        kafkaProducer.sendMessage(message);
        return "Message published: " + message;
    }
}
