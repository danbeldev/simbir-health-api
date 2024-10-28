package ru.simbir.health.timetableservice.features.timetable.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableEntity;

import java.time.Instant;

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

    public static Specification<TimetableEntity> hasDateRange(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction(); // Нет фильтра, возвращаем все записи
            }
            if (startDate != null && endDate != null) {
                // Если заданы обе даты, ищем в указанном диапазоне
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("from"), startDate),
                        criteriaBuilder.lessThanOrEqualTo(root.get("to"), endDate)
                );
            }
            if (startDate != null) {
                // Если только startDate, выбираем записи, которые начинаются после этой даты
                return criteriaBuilder.greaterThanOrEqualTo(root.get("from"), startDate);
            }
            // Если только endDate, выбираем записи, которые заканчиваются до этой даты
            return criteriaBuilder.lessThanOrEqualTo(root.get("to"), endDate);
        };
    }
}
