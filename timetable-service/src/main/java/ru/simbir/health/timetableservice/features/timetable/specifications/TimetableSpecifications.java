package ru.simbir.health.timetableservice.features.timetable.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableEntity;

public class TimetableSpecifications {

    public static Specification<TimetableEntity> active() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), false);
    }

    public static Specification<TimetableEntity> byDoctorId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("doctorId"), id);
    }

    public static Specification<TimetableEntity> byHospitalId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("hospitalId"), id);
    }

    public static Specification<TimetableEntity> byRoom(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("roomName"), name);
    }
}
