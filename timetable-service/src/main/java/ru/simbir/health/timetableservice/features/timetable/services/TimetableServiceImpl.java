package ru.simbir.health.timetableservice.features.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.timetableservice.common.message.LocalizedMessageService;
import ru.simbir.health.timetableservice.features.timetable.dto.params.CreateOrUpdateTimetableParams;
import ru.simbir.health.timetableservice.features.timetable.dto.params.GetAllTimetablesParams;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableEntity;
import ru.simbir.health.timetableservice.features.timetable.repositories.TimetableRepository;
import ru.simbir.health.timetableservice.features.timetable.specifications.TimetableSpecifications;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository timetableRepository;
    private final LocalizedMessageService localizedMessageService;

    @Override
    @Transactional(readOnly = true)
    public List<TimetableEntity> getAll(GetAllTimetablesParams params) {
        Specification<TimetableEntity> spec = Specification.where(TimetableSpecifications.active())
                .and(TimetableSpecifications.hasDateRange(params.getFrom(), params.getTo()));

        if (params.getDoctorId() != null) {
            spec = spec.and(TimetableSpecifications.byDoctorId(params.getDoctorId()));
        }

        if (params.getHospitalId() != null) {
            spec = spec.and(TimetableSpecifications.byHospitalId(params.getHospitalId()));
        }

        if (params.getRoom() != null) {
            spec = spec.and(TimetableSpecifications.byRoom(params.getRoom()));
        }

        return timetableRepository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public TimetableEntity getById(long id) {
        return timetableRepository.finActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        localizedMessageService.getMessage("error.timetable.notfound", id)));
    }

    @Override
    @Transactional
    public TimetableEntity create(CreateOrUpdateTimetableParams params) {
        validationFromAndToInstants(params.from(), params.to());
        var timetable = new TimetableEntity();
        timetable.setTo(params.to());
        timetable.setFrom(params.from());
        timetable.setDoctorId(params.doctorId());
        timetable.setHospitalId(params.hospitalId());
        timetable.setRoomName(params.room());
        return timetableRepository.save(timetable);
    }

    @Override
    @Transactional
    public void update(long id, CreateOrUpdateTimetableParams params) {
        validationFromAndToInstants(params.from(), params.to());
        var timetable = getById(id);
        timetable.setTo(params.to());
        timetable.setFrom(params.from());
        timetable.setDoctorId(params.doctorId());
        timetable.setHospitalId(params.hospitalId());
        timetable.setRoomName(params.room());
        timetableRepository.save(timetable);
    }

    @Override
    @Transactional
    public void softDeleteById(long id) {
        var timetable = getById(id);
        timetable.setIsDeleted(true);
        timetableRepository.save(timetable);
    }

    @Override
    @Transactional
    public void softDeleteByHospitalId(long id) {
        var timetables = getAll(GetAllTimetablesParams.builder().hospitalId(id).build());
        for (TimetableEntity timetable : timetables) {
            timetable.setIsDeleted(true);
        }
        timetableRepository.saveAll(timetables);
    }

    @Override
    @Transactional
    public void softDeleteByDoctorId(long id) {
        var timetables = getAll(GetAllTimetablesParams.builder().doctorId(id).build());
        for (TimetableEntity timetable : timetables) {
            timetable.setIsDeleted(true);
        }
        timetableRepository.saveAll(timetables);
    }

    private void validationFromAndToInstants(Instant from, Instant to) {
        if (to.isBefore(from)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, localizedMessageService.getMessage("error.time.invalid"));
        }

        validationInstants(from);
        validationInstants(to);

        if (Duration.between(from, to).toHours() > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, localizedMessageService.getMessage("error.time.range"));
        }
    }

    private void validationInstants(Instant instant) {
        int minutes = instant.atZone(java.time.ZoneOffset.UTC).getMinute();
        int seconds = instant.atZone(java.time.ZoneOffset.UTC).getSecond();

        if (minutes % 30 != 0 || seconds != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, localizedMessageService.getMessage("error.time.minutes.seconds"));
        }
    }
}
