package ru.simbir.health.hospitalservice.features.timetable.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "timetable-client", url = "${feign.clients.timetable.url}")
public interface TimetableServiceClient {

    @DeleteMapping("/api/Timetable/Hospital/{id}")
    void deleteByHospitalId(@PathVariable("id") long hospitalId, @RequestHeader("Authorization") String accessToken);
}
