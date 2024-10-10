package ru.simbir.health.accountservice.features.user.entities.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserRoleEntityId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1482489882385162241L;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public enum Role implements GrantedAuthority {
        User,
        Doctor,
        Manager,
        Admin;

        @Override
        public String getAuthority() {
            return "ROLE_" + name();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserRoleEntityId entity = (UserRoleEntityId) o;
        return Objects.equals(this.role, entity.role) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, userId);
    }
}