package ru.simbir.health.documentservice.features.hospital.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hospital-service", url = "http://localhost:8081/api/Hospitals")
public interface HospitalServiceClient {

    @GetMapping("/{hospitalId}/Room/{room}/Validation")
    boolean validationHospitalAndRoom(@PathVariable Long hospitalId, @PathVariable String room);
}
