package ru.simbir.health.hospitalservice.features.hospital.mappers;

import org.mapstruct.*;
import ru.simbir.health.hospitalservice.features.hospital.dto.room.HospitalRoomEntityDto;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalRoomEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HospitalRoomEntityMapper {

    @Mapping(source = "id.name", target = "name")
    HospitalRoomEntityDto toDto(HospitalRoomEntity entity);
}