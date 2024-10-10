package ru.simbir.health.accountservice.features.user.mappers;

import org.mapstruct.*;
import ru.simbir.health.accountservice.features.user.dto.UserEntityDto;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {
    UserEntity toEntity(UserEntityDto userEntityDto);

    UserEntityDto toDto(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(UserEntityDto userEntityDto, @MappingTarget UserEntity userEntity);
}