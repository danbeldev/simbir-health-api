package ru.simbir.health.accountservice.features.security.jwt;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

import java.util.Collection;
import java.util.UUID;

@Data
public class JwtUserDetails implements UserDetails {

    private final Long userId;
    private final UUID tokenId;
    private final String username;
    private final Collection<UserRoleEntityId.Role> authorities;

    public static JwtUserDetails create(UUID tokenId, UserEntity user) {
        return new JwtUserDetails(
                user.getId(),
                tokenId,
                user.getUsername(),
                user.getRoles().stream().map(r -> r.getId().getRole()).toList()
        );
    }

    @Override
    public String getPassword() {
        return null;
    }
}
