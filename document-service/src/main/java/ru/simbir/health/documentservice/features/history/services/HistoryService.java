package ru.simbir.health.documentservice.features.history.services;

import ru.simbir.health.documentservice.features.history.dto.params.CreateOrUpdateParams;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;

import java.util.List;

public interface HistoryService {

    HistoryEntity getById(Long id);

    List<HistoryEntity> getAllByPacientId(Long pacientId);

    HistoryEntity create(CreateOrUpdateParams params);

    void update(Long id, CreateOrUpdateParams params);
}
