package com.birthdaybot.utills;

import com.birthdaybot.exceptions.RestartServerException;
import com.birthdaybot.model.Alarm;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class Store {

    public static final Map<Integer, String> monthMap = Map.ofEntries(
            Map.entry(1, "inJanuary"),
            Map.entry(2, "inFebruary"),
            Map.entry(3, "inMarch"),
            Map.entry(4, "inApril"),
            Map.entry(5, "inMay"),
            Map.entry(6, "inJune"),
            Map.entry(7, "inJuly"),
            Map.entry(8, "inAugust"),
            Map.entry(9, "inSeptember"),
            Map.entry(10, "inOctober"),
            Map.entry(11, "inNovember"),
            Map.entry(12, "inDecember")
    );

    @Getter
    private static final BlockingDeque<Pair<Long, Object>> queueToSend = new LinkedBlockingDeque<>();

    @Getter
    private static final BlockingDeque<Pair<String, Update>> queueToProcess = new LinkedBlockingDeque<>();

    public static HashMap<Long, Birthday> tempMap = new HashMap<>();

    /**
     * Получаем объект Birthday по userId. Если данных нет, выбрасываем исключение.
     */
    public static Birthday getBirthday(Long userId) {
        log.debug("Вызван getBirthday() для userId: {}", userId);
        if (!tempMap.containsKey(userId)) {
            log.error("Не найден Birthday для userId: {}. Бросаем RestartServerException.", userId);
            throw new RestartServerException();
        }
        log.debug("Найден Birthday для userId: {}", userId);
        return tempMap.get(userId);
    }

    /**
     * Добавляем в очередь на обработку пару (string, update).
     */
    public static void addToProcessQueue(String string, Update update) {
        Long userId = null;
        if (update != null && update.getMessage() != null && update.getMessage().getFrom() != null) {
            userId = update.getMessage().getFrom().getId();
        } else if (update != null && update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId();
        }
        log.debug("Вызван addToProcessQueue(string='{}', userId={})", string, userId);
        queueToProcess.add(Pair.of(string, update));
        log.debug("Текущий размер queueToProcess: {}", queueToProcess.size());
    }

    /**
     * Перегруженный метод добавления в очередь на обработку, когда строка не требуется.
     */
    public static void addToProcessQueue(Update update) {
        Long userId = null;
        if (update != null && update.getMessage() != null && update.getMessage().getFrom() != null) {
            userId = update.getMessage().getFrom().getId();
        } else if (update != null && update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId();
        }
        log.debug("Вызван addToProcessQueue(update) для userId={}", userId);
        queueToProcess.add(Pair.of("", update));
        log.debug("Текущий размер queueToProcess: {}", queueToProcess.size());
    }

    /**
     * Создаём запись Birthday и сохраняем её во временную мапу.
     */
    public static void createBirthday(User user, Long chatId) {
        log.debug("Вызван createBirthday() для userId={}, chatId={}", user.getId(), chatId);
        Birthday birthday = new Birthday();
        birthday.setId(user.getId());
        birthday.setOwner(user);
        birthday.setChatId(chatId);
        tempMap.put(user.getId(), birthday);
        log.info("Создан Birthday для userId={}, chatId={}", user.getId(), chatId);
    }

    /**
     * Формируем Alarm на основе Birthday.
     */
    public static Alarm createAlarmFromBirthday(Birthday birthday) {
        log.debug("Вызван createAlarmFromBirthday() для Birthday c ID пользователя: {}",
                birthday.getOwner() != null ? birthday.getOwner().getId() : "unknown");

        LocalDate today = LocalDate.now();
        LocalDate birthdayDateThisYear = LocalDate.of(
                today.getYear(),
                birthday.getDate().getMonth(),
                birthday.getDate().getDayOfMonth()
        );

        LocalDate alarmDate = birthdayDateThisYear;
        if (birthdayDateThisYear.isBefore(today)) {
            alarmDate = birthdayDateThisYear.plusYears(1);
        }

        Instant alarmTime = alarmDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Alarm alarm = new Alarm();
        alarm.setTime(alarmTime);
        alarm.setBirthday(birthday);

        log.info("Создан Alarm для userId={}, дата уведомления: {}",
                birthday.getOwner() != null ? birthday.getOwner().getId() : "unknown",
                alarmTime);
        return alarm;
    }

    /**
     * Добавляем текстовое сообщение в очередь на отправку.
     */
    public static void addToSendQueue(Long chatId, String message) {
        log.debug("Вызван addToSendQueue(chatId={}, message='{}')", chatId, message);
        SendMessage newSendMessage = new SendMessage();
        newSendMessage.setText(message);
        queueToSend.add(Pair.of(chatId, newSendMessage));
        log.debug("Текущий размер queueToSend: {}", queueToSend.size());
    }

    /**
     * Добавляем объект SendMessage в очередь на отправку.
     */
    public static void addToSendQueue(SendMessage message) {
        log.debug("Вызван addToSendQueue(SendMessage) для чата chatId={}", message.getChatId());
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
        log.debug("Текущий размер queueToSend: {}", queueToSend.size());
    }

    /**
     * Добавляем объект EditMessageReplyMarkup в очередь на отправку.
     */
    public static void addToSendQueue(EditMessageReplyMarkup message) {
        log.debug("Вызван addToSendQueue(EditMessageReplyMarkup) для чата chatId={}", message.getChatId());
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
        log.debug("Текущий размер queueToSend: {}", queueToSend.size());
    }

    /**
     * Добавляем объект DeleteMessage в очередь на отправку.
     */
    public static void addToSendQueue(DeleteMessage message) {
        log.debug("Вызван addToSendQueue(DeleteMessage) для чата chatId={}", message.getChatId());
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
        log.debug("Текущий размер queueToSend: {}", queueToSend.size());
    }
}
