package ru.simbir.health.documentservice.features.history;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.documentservice.common.security.authenticate.Authenticate;
import ru.simbir.health.documentservice.common.security.authorization.Authorization;
import ru.simbir.health.documentservice.common.security.user.UserSession;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;
import ru.simbir.health.documentservice.common.security.user.models.UserSessionDetails;
import ru.simbir.health.documentservice.common.validate.hospital.ValidHospitalAndRoom;
import ru.simbir.health.documentservice.features.history.documents.elasticsearch.HistoryDocument;
import ru.simbir.health.documentservice.features.history.dto.HistoryEntityDto;
import ru.simbir.health.documentservice.features.history.dto.params.CreateOrUpdateHistoryParams;
import ru.simbir.health.documentservice.features.history.mappers.HistoryEntityMapper;
import ru.simbir.health.documentservice.features.history.services.HistoryService;
import ru.simbir.health.documentservice.features.history.services.elasticsearch.HistoryElasticsearchService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/History")
@Tag(name = "История посещений", description = "Эндпоинты для управления историей посещений пациентов.")
public class HistoryController {

    private final HistoryService historyService;
    private final HistoryElasticsearchService historyElasticsearchService;

    private final HistoryEntityMapper historyEntityMapper;

    @Authenticate
    @GetMapping("/search")
    @Operation(summary = "Поиск истории посещений", description = "Ищет историю посещений по заданному запросу.")
    public List<HistoryDocument> search(
            @RequestParam String query
    ) {
        return historyElasticsearchService.searchByQuery(query);
    }

    @GetMapping("/Account/{id}")
    @Authenticate(roles = UserRole.Doctor, parameterUserId = "id")
    @Operation(summary = "Получить историю по ID пациента", description = "Возвращает всю историю посещений для указанного пациента.")
    public List<HistoryEntityDto> getAllByAccountId(@PathVariable Long id) {
        return historyService.getAllByPacientId(id).stream().map(historyEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/{id}")
    @Authorization(value = "historyAuthorization.accessReadHistory(#result,#userSession)", executeBefore = false)
    @Operation(summary = "Получить историю по ID", description = "Возвращает конкретную запись истории посещений по указанному ID.")
    public HistoryEntityDto getById(@PathVariable Long id, @UserSession UserSessionDetails userSession) {
        return historyEntityMapper.toDto(historyService.getById(id));
    }

    @PostMapping
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager, UserRole.Doctor})
    @ValidHospitalAndRoom(hospitalIdFieldName = "#params.hospitalId", roomFieldName = "#params.room")
    @Operation(summary = "Создать новую запись истории", description = "Создаёт новую запись истории посещений с указанными параметрами.")
    public HistoryEntityDto create(@Valid @RequestBody CreateOrUpdateHistoryParams params) {
        return historyEntityMapper.toDto(historyService.create(params));
    }

    @PutMapping("/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager, UserRole.Doctor})
    @Operation(summary = "Обновить запись истории", description = "Обновляет существующую запись истории посещений по указанному ID.")
    public void update(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateHistoryParams params) {
        historyService.update(id, params);
    }
}
