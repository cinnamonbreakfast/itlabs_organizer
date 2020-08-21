package com.organizer.core.service;

import com.organizer.core.model.User;
import com.organizer.core.model.ValidationCode;
import com.organizer.core.repository.ValidationCodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ValidationCodeService {
    private final ValidationCodesRepository validationCodesRepository;

    @Autowired
    public ValidationCodeService(ValidationCodesRepository validationCodesRepository) {
        this.validationCodesRepository = validationCodesRepository;
    }

    public ValidationCode find(Integer code) {
        return this.validationCodesRepository.findByCode(code);
    }

    public ValidationCode createNewCode(User target, String purpose) {
        Integer code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        ValidationCode newCode = ValidationCode.builder()
                .account(target)
                .purpose(purpose)
                .code(code)
                .dueDate(LocalDateTime.now())
                .build();

        return this.validationCodesRepository.save(newCode);
    }
}
