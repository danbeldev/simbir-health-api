package ru.simbir.health.documentservice.features.history.services.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;
import ru.simbir.health.documentservice.features.history.dto.HistoryEntityDto;
import ru.simbir.health.documentservice.features.user.client.models.UserModel;

@Service
@RequiredArgsConstructor
public class HistoryAuthorization {

    @Transactional(readOnly = true)
    public boolean accessReadHistory(HistoryEntityDto history, UserModel user) {
        if (user.getRoles().contains(UserRole.Doctor)) return true;
        return history.pacientId().equals(user.getId());
    }
}
