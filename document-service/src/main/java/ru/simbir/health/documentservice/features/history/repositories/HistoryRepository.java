package ru.simbir.health.documentservice.features.history.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
    List<HistoryEntity> findAllByPacientId(Long pacientId);
}
