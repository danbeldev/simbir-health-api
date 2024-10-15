package ru.simbir.health.timetableservice.features.timetable.mappers;

import org.mapstruct.*;
import ru.simbir.health.timetableservice.features.timetable.dto.TimetableEntityDto;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimetableEntityMapper {
    TimetableEntity toEntity(TimetableEntityDto timetableEntityDto);

    TimetableEntityDto toDto(TimetableEntity timetableEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TimetableEntity partialUpdate(TimetableEntityDto timetableEntityDto, @MappingTarget TimetableEntity timetableEntity);
}