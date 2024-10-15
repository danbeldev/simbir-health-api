package ru.simbir.health.documentservice.features.history;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.documentservice.features.history.dto.HistoryEntityDto;
import ru.simbir.health.documentservice.features.history.dto.params.CreateOrUpdateParams;
import ru.simbir.health.documentservice.features.history.mappers.HistoryEntityMapper;
import ru.simbir.health.documentservice.features.history.services.HistoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/History")
public class HistoryController {

    private final HistoryService historyService;

    private final HistoryEntityMapper historyEntityMapper;

    @GetMapping("/Account/{id}")
    public List<HistoryEntityDto> getAllByAccountId(@PathVariable Long id) {
        return historyService.getAllByPacientId(id).stream().map(historyEntityMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public HistoryEntityDto getById(@PathVariable Long id) {
        return historyEntityMapper.toDto(historyService.getById(id));
    }

    @PostMapping
    public HistoryEntityDto create(@RequestBody CreateOrUpdateParams params) {
        return historyEntityMapper.toDto(historyService.create(params));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody CreateOrUpdateParams params) {
        historyService.update(id, params);
    }
}
