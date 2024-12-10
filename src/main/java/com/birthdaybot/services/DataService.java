package com.birthdaybot.services;

import com.birthdaybot.model.Alarm;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.User;
import com.birthdaybot.model.enums.Status;
import com.birthdaybot.repositories.AlarmRepository;
import com.birthdaybot.repositories.BirthdayRepository;
import com.birthdaybot.repositories.UserRepository;
import com.birthdaybot.utills.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
public class DataService {

    private final BirthdayRepository birthdayRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public DataService(
            BirthdayRepository birthdayRepository,
            UserRepository userRepository,
            AlarmRepository alarmRepository
    ) {
        this.birthdayRepository = birthdayRepository;
        this.userRepository = userRepository;
        this.alarmRepository = alarmRepository;

        log.info("DataService создан. Репозитории внедрены: BirthdayRepository, UserRepository, AlarmRepository.");
    }

    public Long getShareCode(Long id) {
        log.debug("Вызван getShareCode() с userId={}", id);
        User user = getUser(id);
        Long shareCode = user.getShareCode();
        log.debug("shareCode для userId={} = {}", id, shareCode);
        return shareCode;
    }

    public void addUser(User user) {
        log.debug("Вызван addUser() для userId={}", user.getId());
        userRepository.save(user);
        log.info("Пользователь с userId={} добавлен в базу данных.", user.getId());
    }

    public Status getStatus(Long userId) {
        log.debug("Вызван getStatus() с userId={}", userId);
        Status status = userRepository.getStatus(userId);
        log.debug("Текущий статус пользователя userId={} = {}", userId, status);
        return status;
    }

    public User getUser(Long userId) {
        log.debug("Вызван getUser() с userId={}", userId);
        User user = userRepository.getUserById(userId);
        if (user == null) {
            log.warn("Пользователь с userId={} не найден в базе данных.", userId);
        } else {
            log.debug("Получен объект User: userId={}, userName={}", user.getId(), user.getUsername());
        }
        return user;
    }

    public String getLanguageCode(Long userId) {
        log.debug("Вызван getLanguageCode() с userId={}", userId);
        String lang = userRepository.getLanguageCode(userId);
        log.debug("Язык для userId={} = {}", userId, lang);
        return lang;
    }

    public Integer getTimeZone(Long userId) {
        log.debug("Вызван getTimeZone() с userId={}", userId);
        Integer timeZone = userRepository.getTimeZone(userId);
        log.debug("Часовой пояс для userId={} = {}", userId, timeZone);
        return timeZone;
    }

    public boolean getIsGroupMode(Long userId) {
        log.debug("Вызван getIsGroupMode() с userId={}", userId);
        boolean isGroupMode = userRepository.getIsGroupMode(userId);
        log.debug("Режим группы для userId={} = {}", userId, isGroupMode);
        return isGroupMode;
    }

    public Long getGroupIdByUserId(Long userId) {
        log.debug("Вызван getGroupIdByUserId() с userId={}", userId);
        Long groupId = userRepository.getGroupIdByUserId(userId);
        log.debug("Получен groupId={} для userId={}", groupId, userId);
        return groupId;
    }

    public boolean getIsGroupAdmin(Long userId) {
        log.debug("Вызван getIsGroupAdmin() с userId={}", userId);
        boolean isGroupAdmin = userRepository.getIsGroupAdmin(userId);
        log.debug("Режим админа группы для userId={} = {}", userId, isGroupAdmin);
        return isGroupAdmin;
    }

    public void setTimeZone(Integer zone, Long userId) {
        log.debug("Вызван setTimeZone(zone={}, userId={})", zone, userId);
        userRepository.setTimeZone(zone, userId);
        log.info("Часовой пояс пользователя userId={} успешно установлен в {}", userId, zone);
    }

    public boolean existUser(long userId) {
        log.debug("Вызван existUser() с userId={}", userId);
        boolean exists = userRepository.existsById(userId);
        log.debug("Проверка существования пользователя userId={} = {}", userId, exists);
        return exists;
    }

    @Transactional
    public void addBirthdayAndAlarm(Birthday birthday) {
        log.debug("Вызван addBirthdayAndAlarm() для userId={}, дата: {}",
                birthday.getId(), birthday.getDate());
        Birthday savedBirthday = birthdayRepository.save(birthday);
        Alarm alarm = Store.createAlarmFromBirthday(savedBirthday);
        alarmRepository.save(alarm);
        log.info("Сохранён Birthday и создан Alarm для userId={}", birthday.getId());
    }

    public void deleteBirthdayById(Long id) {
        log.debug("Вызван deleteBirthdayById() c birthdayId={}", id);
        int count = alarmRepository.deleteByBirthdayId(id);
        birthdayRepository.deleteById(id);
        log.info("Удалён birthdayId={}, также удалено {} alarm(ов), связанных с этим ДР.", id, count);
    }

    public void updateStatusById(Status status, Long id) {
        log.debug("Вызван updateStatusById(status={}, userId={})", status, id);
        userRepository.updateStatusById(status, id);
        log.info("Статус пользователя userId={} обновлён до {}", id, status);
    }

    public void updateIsGroupModeById(Boolean isGroupMode, Long id) {
        log.debug("Вызван updateIsGroupModeById(isGroupMode={}, userId={})", isGroupMode, id);
        userRepository.updateIsGroupModeById(isGroupMode, id);
        log.info("Режим группы для userId={} изменён на {}", id, isGroupMode);
    }

    public void updateIsAdminById(Boolean isAdmin, Long id) {
        log.debug("Вызван updateIsAdminById(isAdmin={}, userId={})", isAdmin, id);
        userRepository.updateIsAdminById(isAdmin, id);
        log.info("Режим администратора группы для userId={} изменён на {}", id, isAdmin);
    }

    public void setGroupId(Long groupId, Long id) {
        log.debug("Вызван setGroupId(groupId={}, userId={})", groupId, id);
        userRepository.setGroupId(groupId, id);
        log.info("Пользователю userId={} установлен groupId={}", id, groupId);
    }

    public void updateLangById(String lang, Long id) {
        log.debug("Вызван updateLangById(lang={}, userId={})", lang, id);
        userRepository.updateLangById(lang, id);
        log.info("Язык пользователя userId={} обновлён до '{}'", id, lang);
    }

    public ArrayList<Birthday> findBirthdaysByOwnerId(Long id) {
        log.debug("Вызван findBirthdaysByOwnerId(userId={})", id);
        ArrayList<Birthday> birthdays = birthdayRepository.findBirthdaysByOwnerId(id);
        log.debug("Найдено {} записей о ДР для userId={}", birthdays.size(), id);
        return birthdays;
    }

    public ArrayList<Birthday> findBirthdaysByChatId(Long id) {
        log.debug("Вызван findBirthdaysByChatId(chatId={})", id);
        ArrayList<Birthday> birthdays = birthdayRepository.findBirthdaysByChatId(id);
        log.debug("Найдено {} записей о ДР для chatId={}", birthdays.size(), id);
        return birthdays;
    }
}
