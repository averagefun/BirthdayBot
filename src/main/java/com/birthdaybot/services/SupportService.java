package com.birthdaybot.services;

import com.birthdaybot.repositories.SupportRepository;
import org.springframework.stereotype.Service;

@Service
public class SupportService {
    private final SupportRepository supportRepository;


    public SupportService(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    public boolean supportRequestExist(){
        return supportRepository.hasRequest(0);
    }
}
