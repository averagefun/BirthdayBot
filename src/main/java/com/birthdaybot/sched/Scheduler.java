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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class Scheduler {
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private final AlarmService alarmService;

    public Scheduler(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Scheduled(cron = "0 * * * * *") // Раз в час (каждый час точно в 00 минут)
    public void sendAlarms() {
        logger.info("Starting send alarm task ");
        List<Alarm> alarms = alarmService.findByTime(Instant.now());
        int sended = 0;
        for (Alarm alarm : alarms) {
            String name = "Сегодня день рождения у "
                    + alarm.getBirthday().getName()
                    + " исполнилось "
                    + calculateYearsAgo(alarm.getBirthday().getDate())
                    + " "
                    + getYearWord(calculateYearsAgo(alarm.getBirthday().getDate()))
                    + ".";
            Store.addToSendQueue(alarm.getBirthday().getChatId(), name);
            sended++;
            incrementYear(alarm);
        }
        logger.info(String.format("Add %d alarms for send", sended));
    }

    private void incrementYear(Alarm alarm) {
        alarm.setTime(alarm.getTime().plus(Duration.ofDays(365)));
        alarmService.save(alarm);
    }

    private long calculateYearsAgo(LocalDate date) {
        LocalDate today = LocalDate.now();

        return ChronoUnit.YEARS.between(date, today);
    }

    private String getYearWord(long years) {
        long lastTwoDigits = years % 100;
        long lastDigit = years % 10;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            return "лет";
        }
        if (lastDigit == 1) {
            return "год";
        }
        if (lastDigit >= 2 && lastDigit <= 4) {
            return "года";
        }
        return "лет";
    }
}
