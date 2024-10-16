package ru.simbir.health.documentservice.features.history;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.documentservice.common.security.authenticate.Authenticate;
import ru.simbir.health.documentservice.common.security.user.UserSession;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;
import ru.simbir.health.documentservice.common.security.user.models.UserSessionDetails;
import ru.simbir.health.documentservice.common.validate.comparison.FieldComparison;
import ru.simbir.health.documentservice.common.validate.hospital.ValidHospitalAndRoom;
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
    @Authenticate(roles = UserRole.Doctor, parameterUserId = "id")
    public List<HistoryEntityDto> getAllByAccountId(@PathVariable Long id) {
        return historyService.getAllByPacientId(id).stream().map(historyEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/{id}")
    @FieldComparison(firstField = "#request.userSession.id", secondField = "#response.pacientId", excludedRoles = UserRole.Doctor)
    public HistoryEntityDto getById(@PathVariable Long id, @UserSession UserSessionDetails userSession) {
        return historyEntityMapper.toDto(historyService.getById(id));
    }

    @PostMapping
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager, UserRole.Doctor})
    @ValidHospitalAndRoom(hospitalIdFieldName = "#request.params.hospitalId", roomFieldName = "#request.params.room")
    public HistoryEntityDto create(@Valid @RequestBody CreateOrUpdateParams params) {
        return historyEntityMapper.toDto(historyService.create(params));
    }

    @PutMapping("/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager, UserRole.Doctor})
    public void update(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateParams params) {
        historyService.update(id, params);
    }
}
