package ru.simbir.health.accountservice.features.security.services.activeToken;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.accountservice.common.message.LocalizedMessageService;
import ru.simbir.health.accountservice.features.security.entities.ActiveTokenEntity;
import ru.simbir.health.accountservice.features.security.repositories.ActiveTokenRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActiveTokenServiceImpl implements ActiveTokenService {

    private final ActiveTokenRepository activeTokenRepository;

    private final LocalizedMessageService localizedMessageService;

    @Override
    @Transactional(readOnly = true)
    public ActiveTokenEntity getById(UUID id) {
        return activeTokenRepository.findById(id)
                .orElseThrow(() -> {
                    var message = localizedMessageService.getMessage("token.notfound.id", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existById(UUID id) {
        return activeTokenRepository.findById(id).isPresent();
    }

    @Override
    @Transactional
    public UUID create() {
        var token = new ActiveTokenEntity();
        return activeTokenRepository.save(token).getId();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        activeTokenRepository.deleteById(id);
    }
}
