package com.birthdaybot.services;

import com.birthdaybot.model.Alarm;
import com.birthdaybot.repositories.AlarmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
        log.info("AlarmService создан. AlarmRepository внедрён.");
    }

    public List<Alarm> findByTime(Instant time) {
        log.debug("Вызван findByTime() с параметром time={}", time);
        List<Alarm> alarms = alarmRepository.findByTime(time);
        log.debug("Найдено {} будильников (Alarm) на время {}", alarms.size(), time);
        return alarms;
    }

    public void save(Alarm alarm) {
        log.debug("Вызван save() для Alarm с датой {}", alarm.getTime());
        alarmRepository.save(alarm);
        log.info("Alarm успешно сохранён с датой {}", alarm.getTime());
    }
}
