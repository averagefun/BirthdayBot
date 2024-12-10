package com.birthdaybot.services;

import com.birthdaybot.repositories.SupportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupportService {

    private final SupportRepository supportRepository;

    public SupportService(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
        log.info("SupportService создан. SupportRepository внедрён.");
    }

    public boolean supportRequestExist() {
        log.debug("Вызван supportRequestExist()");
        boolean exist = supportRepository.hasRequest(0);
        log.debug("Результат supportRequestExist(): {}", exist);
        return exist;
    }
}
