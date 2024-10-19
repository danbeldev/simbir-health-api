package ru.simbir.health.hospitalservice.features.hospital.dto.params;

import java.util.List;

public record CreateOrUpdateHospitalParams(
   String name,
   String address,
   String contactPhone,
   List<String> rooms
) {}
