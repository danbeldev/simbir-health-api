package ru.simbir.health.documentservice.features.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ExceptionBody {

    private final String message;
    private final String code;
    private Map<String, String> errors;
}
