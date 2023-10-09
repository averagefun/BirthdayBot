package com.birthdaybot.services;

import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.Status;
import com.birthdaybot.model.User;
import com.birthdaybot.repositories.BirthdayRepository;
import com.birthdaybot.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DataService {


    private final BirthdayRepository birthdayRepository;

    private final UserRepository userRepository;

    public DataService(BirthdayRepository birthdayRepository, UserRepository userRepository) {
        this.birthdayRepository = birthdayRepository;
        this.userRepository = userRepository;
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

    public void setTimeZone(Integer zone, Long userId){
        userRepository.setTimeZone(zone, userId);
    }

    public boolean existUser(long userId){
        return userRepository.existsById(userId);
    }

    public void addBirthday(Birthday birthday){
        birthdayRepository.save(birthday);
    }
    public void deleteBirthdayById(Long id){
        birthdayRepository.deleteById(id);
    }

    public void updateStatusById(Status status, Long id){
        userRepository.updateStatusById(status, id);
    }
    public void updateLangById(String lang, Long id){
        userRepository.updateLangById(lang, id);
    }
    public ArrayList<Birthday> findBirthdaysByOwnerId(Long id){
        return birthdayRepository.findBirthdaysByOwnerId(id);
    }
}
