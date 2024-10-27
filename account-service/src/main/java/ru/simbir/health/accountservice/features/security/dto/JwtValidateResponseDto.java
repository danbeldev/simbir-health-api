package ru.simbir.health.accountservice.features.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Data
@AllArgsConstructor
public final class JwtValidateResponseDto {

    private Boolean active = true;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId = null;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Collection<UserRoleEntityId.Role> roles = new ArrayList<>();


    public JwtValidateResponseDto(Boolean active) {
        this.active = active;
    }

    public JwtValidateResponseDto(Long userId, Collection<UserRoleEntityId.Role> roles) {
        this.userId = userId;
        this.roles = roles;
    }
}
