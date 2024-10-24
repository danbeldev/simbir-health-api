package ru.simbir.health.documentservice.features.history.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.documentservice.common.message.LocalizedMessageService;
import ru.simbir.health.documentservice.features.history.dto.params.CreateOrUpdateHistoryParams;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;
import ru.simbir.health.documentservice.features.history.repositories.HistoryRepository;
import ru.simbir.health.documentservice.features.history.services.elasticsearch.HistoryElasticsearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    public final HistoryRepository historyRepository;

    private final HistoryElasticsearchService historyElasticsearchService;
    private final LocalizedMessageService localizedMessageService;

    @Override
    @Transactional(readOnly = true)
    public HistoryEntity getById(Long id) {
        return historyRepository.findById(id)
                .orElseThrow(() -> {
                    var message = localizedMessageService.getMessage("error.history.notfound", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryEntity> getAllByPacientId(Long pacientId) {
        return historyRepository.findAllByPacientId(pacientId);
    }

    @Override
    @Transactional
    public HistoryEntity create(CreateOrUpdateHistoryParams params) {
        var history = new HistoryEntity();
        historySetPrams(history, params);

        var savedHistory = historyRepository.save(history);

        historyElasticsearchService.save(savedHistory);

        return savedHistory;
    }

    @Override
    @Transactional
    public void update(Long id, CreateOrUpdateHistoryParams params) {
        var history = getById(id);
        historySetPrams(history, params);

        historyElasticsearchService.save(history);

        historyRepository.save(history);
    }

    private void historySetPrams(HistoryEntity history, CreateOrUpdateHistoryParams params) {
        history.setData(params.data());
        history.setPacientId(params.pacientId());
        history.setHospitalId(params.hospitalId());
        history.setDoctorId(params.doctorId());
        history.setRoomName(params.room());
        history.setDate(params.date());
    }
}
