package ru.simbir.health.documentservice.features.history.repositories;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.simbir.health.documentservice.features.history.documents.elasticsearch.HistoryDocument;

import java.util.List;

public interface HistoryElasticsearchRepository extends ElasticsearchRepository<HistoryDocument, Long> {

    @Query("""
            {
                "multi_match": {
                    "query": "?0",
                    "fields": ["data","root_name"],
                    "type": "best_fields",
                    "operator": "or"
                }
            }
            """)
    List<HistoryDocument> searchByQuery(String query);
}
