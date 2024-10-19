package ru.simbir.health.hospitalservice.features.hospital.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class HospitalRoomEntityId implements Serializable {
    private static final long serialVersionUID = -3058261271896172937L;

    @Size(max = 48)
    @NotNull
    @Column(name = "name", nullable = false, length = 48)
    private String name;

    @NotNull
    @Column(name = "hospital_id", nullable = false, insertable = false, updatable = false)
    private Long hospitalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospital;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HospitalRoomEntityId entity = (HospitalRoomEntityId) o;
        return Objects.equals(this.hospitalId, entity.hospitalId) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hospitalId, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "serialVersionUID = " + serialVersionUID + ", " +
                "name = " + name + ", " +
                "hospitalId = " + hospitalId + ")";
    }
}