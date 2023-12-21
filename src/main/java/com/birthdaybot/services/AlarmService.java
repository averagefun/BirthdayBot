package com.birthdaybot.services;

import com.birthdaybot.model.Alarm;
import com.birthdaybot.repositories.AlarmRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public List<Alarm> findByTime(Instant time) {
        return alarmRepository.findByTime(time);
    }

    public void save(Alarm alarm) {
        alarmRepository.save(alarm);
    }
}
