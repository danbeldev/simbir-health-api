package ru.simbir.health.documentservice.features.history.documents.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;

@Getter
@Setter
@Document(indexName = "histories")
@Setting(settingPath = "/elasticsearch-settings.json")
public class HistoryDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Long, name = "pacient_id")
    private Long pacientId;

    @Field(type = FieldType.Long, name = "doctor_id")
    private Long doctorId;

    @Field(type = FieldType.Long, name = "hospital_id")
    private Long hospitalId;

    @Field(type = FieldType.Text, name = "root_name")
    private String roomName;

    @Field(type = FieldType.Text, analyzer = "russian_analyzer", name = "data")
    private String data;

    @Field(type = FieldType.Date, format = DateFormat.date_time, name = "date")
    private Instant date;
}
