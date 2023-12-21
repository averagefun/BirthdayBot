package com.birthdaybot.sched;

import com.birthdaybot.model.Alarm;
import com.birthdaybot.services.AlarmService;
import com.birthdaybot.utills.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.List;

@Component
public class Scheduler {
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private final AlarmService alarmService;

    public Scheduler(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Scheduled(cron = "0 0 * * * *") // Раз в час (каждый час точно в 00 минут)
    public void sendAlarms() {
        logger.info("Starting send alarm task ");
        List<Alarm> alarms = alarmService.findByTime(Instant.now());
        int sended = 0;
        for (Alarm alarm : alarms) {
            Store.addToSendQueue(alarm.getBirthday().getOwner().getId(), alarm.getBirthday().getName());
            sended++;
            incrementYear(alarm);
        }
        logger.info(String.format("Add %d alarms for send", sended));
    }

    private void incrementYear(Alarm alarm) {
        alarm.setTime(alarm.getTime().plus(Duration.ofDays(365)));
        alarmService.save(alarm);
    }
}
