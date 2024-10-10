package ru.simbir.health.hospitalservice.mappers;

import org.mapstruct.*;
import ru.simbir.health.hospitalservice.dto.room.HospitalRoomEntityDto;
import ru.simbir.health.hospitalservice.entities.HospitalRoomEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HospitalRoomEntityMapper {
    HospitalRoomEntity toEntity(HospitalRoomEntityDto hospitalRoomEntityDto);

    HospitalRoomEntityDto toDto(HospitalRoomEntity hospitalRoomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    HospitalRoomEntity partialUpdate(HospitalRoomEntityDto hospitalRoomEntityDto, @MappingTarget HospitalRoomEntity hospitalRoomEntity);
}