package ru.simbir.health.hospitalservice.dto.params;

import java.util.List;

public record CreateOrUpdateHospitalParams(
   String name,
   String address,
   String contactPhone,
   List<String> rooms
) {}
