package ru.simbir.health.accountservice.features.test;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private static final String TOPIC = "my-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("my-topic", message);

        // Асинхронная обработка результата
        future.thenAccept(result -> {
            // Обработка успешной отправки
            System.out.println("Message sent successfully: " + result.getProducerRecord().value());
        }).exceptionally(ex -> {
            // Обработка ошибки отправки
            System.err.println("Failed to send message: " + ex.getMessage());
            return null;
        });
    }
}
