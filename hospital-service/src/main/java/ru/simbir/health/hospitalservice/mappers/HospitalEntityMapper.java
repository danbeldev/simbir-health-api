package ru.simbir.health.hospitalservice.mappers;

import org.mapstruct.*;
import ru.simbir.health.hospitalservice.dto.HospitalEntityDto;
import ru.simbir.health.hospitalservice.entities.HospitalEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HospitalEntityMapper {
    HospitalEntity toEntity(HospitalEntityDto hospitalEntityDto);

    HospitalEntityDto toDto(HospitalEntity hospitalEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    HospitalEntity partialUpdate(HospitalEntityDto hospitalEntityDto, @MappingTarget HospitalEntity hospitalEntity);
}