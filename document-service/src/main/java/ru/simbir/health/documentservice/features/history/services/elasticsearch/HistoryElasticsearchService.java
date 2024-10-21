package ru.simbir.health.documentservice.features.history.services.elasticsearch;

import ru.simbir.health.documentservice.features.history.documents.elasticsearch.HistoryDocument;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;

import java.util.List;

public interface HistoryElasticsearchService {

    void save(HistoryEntity entity);

    List<HistoryDocument> searchByQuery(String query);
}
