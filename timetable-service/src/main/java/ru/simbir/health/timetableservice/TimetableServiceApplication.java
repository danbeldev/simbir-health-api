package ru.simbir.health.timetableservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TimetableServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimetableServiceApplication.class, args);
    }

}
