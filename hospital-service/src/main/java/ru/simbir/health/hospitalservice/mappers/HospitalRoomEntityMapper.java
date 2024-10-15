package ru.simbir.health.hospitalservice.mappers;

import org.mapstruct.*;
import ru.simbir.health.hospitalservice.dto.room.HospitalRoomEntityDto;
import ru.simbir.health.hospitalservice.entities.HospitalRoomEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HospitalRoomEntityMapper {

    @Mapping(source = "id.name", target = "name")
    HospitalRoomEntityDto toDto(HospitalRoomEntity entity);
}