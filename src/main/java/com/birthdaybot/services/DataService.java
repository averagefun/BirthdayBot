package com.birthdaybot.services;

import com.birthdaybot.model.Alarm;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.enums.Status;
import com.birthdaybot.model.User;
import com.birthdaybot.repositories.AlarmRepository;
import com.birthdaybot.repositories.BirthdayRepository;
import com.birthdaybot.repositories.UserRepository;
import com.birthdaybot.utills.Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class DataService {


    private final BirthdayRepository birthdayRepository;

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public DataService(BirthdayRepository birthdayRepository, UserRepository userRepository, AlarmRepository alarmRepository) {
        this.birthdayRepository = birthdayRepository;
        this.userRepository = userRepository;
        this.alarmRepository = alarmRepository;
    }

    public Long getShareCode(Long id){
        User user = getUser(id);
        return user.getShareCode();
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public Status getStatus(Long userId){
        return userRepository.getStatus(userId);
    }

    public User getUser(Long userId){
        return userRepository.getUserById(userId);
    }

    public String getLanguageCode(Long userId){
        return userRepository.getLanguageCode(userId);
    }
    public Integer getTimeZone(Long userId){
        return userRepository.getTimeZone(userId);
    }

    public boolean getIsGroupMode(Long userId){
        return userRepository.getIsGroupMode(userId);
    }
    public Long getGroupIdByUserId(Long userId){
        return userRepository.getGroupIdByUserId(userId);
    }
    public boolean getIsGroupAdmin(Long userId){
        return userRepository.getIsGroupAdmin(userId);
    }
    public void setTimeZone(Integer zone, Long userId){
        userRepository.setTimeZone(zone, userId);
    }

    public boolean existUser(long userId){
        return userRepository.existsById(userId);
    }

    @Transactional
    public void addBirthdayAndAlarm(Birthday birthday){
        Birthday savedBirthday = birthdayRepository.save(birthday);
        alarmRepository.save(Store.createAlarmFromBirthday(savedBirthday));
    }
    public void deleteBirthdayById(Long id){
        int c = alarmRepository.deleteByBirthdayId(id);
        birthdayRepository.deleteById(id);
    }

    public void updateStatusById(Status status, Long id){
        userRepository.updateStatusById(status, id);
    }
    public void updateIsGroupModeById(Boolean isGroupMode, Long id){
        userRepository.updateIsGroupModeById(isGroupMode, id);
    }
    public void updateIsAdminById(Boolean isAdmin, Long id){
        userRepository.updateIsAdminById(isAdmin, id);
    }
    public void setGroupId(Long groupId, Long id){
        userRepository.setGroupId(groupId, id);
    }
    public void updateLangById(String lang, Long id){
        userRepository.updateLangById(lang, id);
    }
    public ArrayList<Birthday> findBirthdaysByOwnerId(Long id){
        return birthdayRepository.findBirthdaysByOwnerId(id);
    }
    public ArrayList<Birthday> findBirthdaysByChatId(Long id){
        return birthdayRepository.findBirthdaysByChatId(id);
    }
}
