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
        logger.info("Scheduler создан. AlarmService внедрён.");
    }

    /**
     * Запускается раз в час (каждый час ровно в 00 минут)
     */
    @Scheduled(cron = "0 * * * * *")
    public void sendAlarms() {
        logger.info("Старт задания sendAlarms() — проверка будильников (Alarm).");

        Instant now = Instant.now();
        logger.debug("Текущее время (Instant.now()): {}", now);

        // Ищем все будильники, сработавшие в текущий момент
        List<Alarm> alarms = alarmService.findByTime(now);
        logger.debug("Найдено {} будильников на текущее время: {}", alarms.size(), now);

        int sended = 0;
        for (Alarm alarm : alarms) {
            if (alarm.getBirthday() == null || alarm.getBirthday().getOwner() == null) {
                logger.warn("Пропускаем Alarm с некорректными данными (birthday/owner=null).");
                continue;
            }

            String name = "Сегодня день рождения у "
                    + alarm.getBirthday().getName()
                    + " исполнилось "
                    + calculateYearsAgo(alarm.getBirthday().getDate())
                    + " "
                    + getYearWord(calculateYearsAgo(alarm.getBirthday().getDate()))
                    + ".";

            Long chatId = alarm.getBirthday().getChatId();
            logger.debug("Отправка напоминания в чат {}: {}", chatId, name);

            // Кладём задачу на отправку в очередь
            Store.addToSendQueue(chatId, name);
            sended++;

            incrementYear(alarm);
        }

        logger.info("Отправлено оповещений о днях рождения: {}.", sended);
    }

    /**
     * Сдвигаем дату Alarm ещё на 365 дней вперёд, чтобы в следующем году снова напомнить.
     */
    private void incrementYear(Alarm alarm) {
        alarm.setTime(alarm.getTime().plus(Duration.ofDays(365)));

        // Сохраняем изменённый Alarm в БД
        alarmService.save(alarm);
        logger.debug("Будильник инкрементирован на 365 дней и сохранён для пользователя ID={}",
                alarm.getBirthday().getOwner().getId());
    }

    /**
     * Вычисляем, сколько лет назад была переданная дата.
     */
    private long calculateYearsAgo(LocalDate date) {
        LocalDate today = LocalDate.now();
        return ChronoUnit.YEARS.between(date, today);
    }

    /**
     * Возвращаем правильное слово ("год", "года", "лет") в зависимости от возраста.
     */
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
