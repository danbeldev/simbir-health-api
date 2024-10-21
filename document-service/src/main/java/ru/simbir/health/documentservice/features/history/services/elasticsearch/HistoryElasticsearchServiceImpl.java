package ru.simbir.health.documentservice.features.history.services.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.simbir.health.documentservice.features.history.documents.elasticsearch.HistoryDocument;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;
import ru.simbir.health.documentservice.features.history.repositories.HistoryElasticsearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryElasticsearchServiceImpl implements HistoryElasticsearchService {

    private final HistoryElasticsearchRepository historyElasticsearchRepository;

    @Override
    public void save(HistoryEntity entity) {
        historyElasticsearchRepository.save(fromEntityToDocument(entity));
    }

    @Override
    public List<HistoryDocument> searchByQuery(String query) {
        return historyElasticsearchRepository.searchByQuery(query);
    }

    private HistoryDocument fromEntityToDocument(HistoryEntity entity) {
        var document = new HistoryDocument();

        document.setId(entity.getId());
        document.setDate(entity.getDate());
        document.setData(entity.getData());
        document.setDoctorId(entity.getDoctorId());
        document.setPacientId(entity.getPacientId());
        document.setRoomName(entity.getRoomName());

        return document;
    }
}
