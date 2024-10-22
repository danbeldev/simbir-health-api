package ru.simbir.health.timetableservice.features.hospital.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hospital-service", url = "${feign.clients.hospital.url}")
public interface HospitalServiceClient {

    @GetMapping("/{hospitalId}/Room/Validation")
    boolean validationHospitalAndRoom(@PathVariable Long hospitalId, @RequestParam String room);
}
