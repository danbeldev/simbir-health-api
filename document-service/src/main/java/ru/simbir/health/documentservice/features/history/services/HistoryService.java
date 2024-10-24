package ru.simbir.health.documentservice.features.history.services;

import ru.simbir.health.documentservice.features.history.dto.params.CreateOrUpdateHistoryParams;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;

import java.util.List;

public interface HistoryService {

    HistoryEntity getById(Long id);

    List<HistoryEntity> getAllByPacientId(Long pacientId);

    HistoryEntity create(CreateOrUpdateHistoryParams params);

    void update(Long id, CreateOrUpdateHistoryParams params);
}
