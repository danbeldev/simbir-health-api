package ru.simbir.health.documentservice.features.history;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.documentservice.common.security.authenticate.Authenticate;
import ru.simbir.health.documentservice.common.security.user.UserSession;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;
import ru.simbir.health.documentservice.common.security.user.models.UserSessionDetails;
import ru.simbir.health.documentservice.features.history.documents.elasticsearch.HistoryDocument;
import ru.simbir.health.documentservice.features.history.dto.HistoryEntityDto;
import ru.simbir.health.documentservice.features.history.dto.params.CreateOrUpdateParams;
import ru.simbir.health.documentservice.features.history.mappers.HistoryEntityMapper;
import ru.simbir.health.documentservice.features.history.services.HistoryService;
import ru.simbir.health.documentservice.features.history.services.elasticsearch.HistoryElasticsearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/History")
public class HistoryController {

    private final HistoryService historyService;
    private final HistoryElasticsearchService historyElasticsearchService;

    private final HistoryEntityMapper historyEntityMapper;

    @GetMapping("/search")
    public List<HistoryDocument> search(
            @RequestParam String query
    ) {
        return historyElasticsearchService.searchByQuery(query);
    }

    @GetMapping("/Account/{id}")
    @Authenticate(roles = UserRole.Doctor, parameterUserId = "id")
    public List<HistoryEntityDto> getAllByAccountId(@PathVariable Long id) {
        return historyService.getAllByPacientId(id).stream().map(historyEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/{id}")
    // TODO: FieldComparison - заменить
//    @FieldComparison(firstField = "#request.userSession.id", secondField = "#response.pacientId", excludedRoles = UserRole.Doctor)
    public HistoryEntityDto getById(@PathVariable Long id, @UserSession UserSessionDetails userSession) {
        return historyEntityMapper.toDto(historyService.getById(id));
    }

    @PostMapping
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager, UserRole.Doctor})
    // TODO: ValidHospitalAndRoom - заменить
//    @ValidHospitalAndRoom(hospitalIdFieldName = "#request.params.hospitalId", roomFieldName = "#request.params.room")
    public HistoryEntityDto create(@Valid @RequestBody CreateOrUpdateParams params) {
        return historyEntityMapper.toDto(historyService.create(params));
    }

    @PutMapping("/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager, UserRole.Doctor})
    public void update(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateParams params) {
        historyService.update(id, params);
    }
}
