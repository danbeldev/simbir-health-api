package ru.simbir.health.documentservice.features.history.mappers;

import org.mapstruct.*;
import ru.simbir.health.documentservice.features.history.dto.HistoryEntityDto;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HistoryEntityMapper {
    HistoryEntity toEntity(HistoryEntityDto historyEntityDto);

    @Mapping(source = "roomName", target = "room")
    HistoryEntityDto toDto(HistoryEntity historyEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    HistoryEntity partialUpdate(HistoryEntityDto historyEntityDto, @MappingTarget HistoryEntity historyEntity);
}