package ru.simbir.health.documentservice.features.history.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "histories")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "pacient_id", nullable = false)
    private Long pacientId;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @NotNull
    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @NotNull
    @Column(name = "room_name", nullable = false)
    private String roomName;

    @NotNull
    @Column(name = "data", length = 2048, nullable = false)
    private String data;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        HistoryEntity that = (HistoryEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "pacientId = " + pacientId + ", " +
                "doctorId = " + doctorId + ", " +
                "hospitalId = " + hospitalId + ", " +
                "roomName = " + roomName + ", " +
                "date = " + date + ")";
    }
}