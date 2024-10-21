package ru.simbir.health.accountservice.features.user.entities.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
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
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "role = " + role + ", " +
                "user = " + user + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserRoleEntityId that = (UserRoleEntityId) o;
        return getRole() != null && Objects.equals(getRole(), that.getRole())
                && getUser() != null && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(role, user);
    }
}