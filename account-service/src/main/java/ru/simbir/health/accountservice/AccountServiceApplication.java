package ru.simbir.health.accountservice;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@EnableKafka
@EnableFeignClients
@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@Bean
	DefaultKafkaProducerFactory<String, String> stringProducerFactory(KafkaProperties properties) {
		Map<String, Object> producerProperties = properties.buildProducerProperties(null);
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(producerProperties);
	}

	@Bean
	KafkaTemplate<String, String> stringKafkaTemplate(DefaultKafkaProducerFactory<String, String> stringProducerFactory) {
		return new KafkaTemplate<>(stringProducerFactory);
	}
}
