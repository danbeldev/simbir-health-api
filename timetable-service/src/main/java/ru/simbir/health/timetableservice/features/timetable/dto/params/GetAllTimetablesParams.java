package ru.simbir.health.timetableservice.features.timetable.dto.params;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class GetAllTimetablesParams {

    private Long hospitalId;
    private Long doctorId;
    private String room;

    private Instant from;
    private Instant to;
}
