package ru.simbir.health.documentservice.features.history.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.documentservice.features.history.dto.params.CreateOrUpdateParams;
import ru.simbir.health.documentservice.features.history.entities.HistoryEntity;
import ru.simbir.health.documentservice.features.history.repositories.HistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    public final HistoryRepository historyRepository;

    @Override
    @Transactional(readOnly = true)
    public HistoryEntity getById(Long id) {
        return historyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryEntity> getAllByPacientId(Long pacientId) {
        return historyRepository.findAllByPacientId(pacientId);
    }

    @Override
    @Transactional
    public HistoryEntity create(CreateOrUpdateParams params) {
        var history = new HistoryEntity();
        historySetPrams(history, params);
        return historyRepository.save(history);
    }

    @Override
    @Transactional
    public void update(Long id, CreateOrUpdateParams params) {
        var history = getById(id);
        historySetPrams(history, params);
        historyRepository.save(history);
    }

    private void historySetPrams(HistoryEntity history, CreateOrUpdateParams params) {
        history.setData(params.data());
        history.setPacientId(params.pacientId());
        history.setHospitalId(params.hospitalId());
        history.setDoctorId(params.doctorId());
        history.setRoomName(params.room());
        history.setDate(params.date());
    }
}
