package ru.simbir.health.timetableservice.features.timetable.services;

import ru.simbir.health.timetableservice.features.timetable.dto.params.CreateOrUpdateTimetableParams;
import ru.simbir.health.timetableservice.features.timetable.dto.params.GetAllTimetablesParams;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableEntity;

import java.util.List;

public interface TimetableService {

    List<TimetableEntity> getAll(GetAllTimetablesParams params);

    TimetableEntity getById(long id);

    TimetableEntity create(CreateOrUpdateTimetableParams params);

    void update(long id, CreateOrUpdateTimetableParams params);

    void softDeleteById(long id);

    void softDeleteByHospitalId(long id);

    void softDeleteByDoctorId(long id);
}
