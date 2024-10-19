package ru.simbir.health.hospitalservice.features.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionBody {

    private String message;
    private String code;
}
