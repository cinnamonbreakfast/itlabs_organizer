package com.organizer.core.repository;

import com.organizer.core.model.ValidationCode;

public interface ValidationCodesRepository extends Repository<Long, ValidationCode> {
    ValidationCode findByCode(Integer code);
}
