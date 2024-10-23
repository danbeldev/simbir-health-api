package ru.simbir.health.accountservice.common.validate.pagination;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPagination {}
