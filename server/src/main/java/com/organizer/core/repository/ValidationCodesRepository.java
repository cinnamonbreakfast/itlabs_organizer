package com.organizer.core.repository;

import com.organizer.core.model.ValidationCode;
import org.springframework.data.jpa.repository.Query;

public interface ValidationCodesRepository extends Repository<Long, ValidationCode> {
    ValidationCode findByCode(Integer code);

    @Query("select c from ValidationCode c where c.code =?1 and c.account.email=?2")
    ValidationCode findByCodeAndEmail(Integer code, String mail);

    @Query("select c from ValidationCode c where c.code =?1 and c.account.phone=?2")
    ValidationCode findByCodeAndPhone(Integer code, String phone);
}
