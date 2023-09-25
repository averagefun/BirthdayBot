package com.birthdaybot.services;

import com.birthdaybot.model.Status;
import com.birthdaybot.model.User;
import com.birthdaybot.repositories.BirthdayRepository;
import com.birthdaybot.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    private final BirthdayRepository birthdayRepository;

    private final UserRepository userRepository;

    public DataService(BirthdayRepository birthdayRepository, UserRepository userRepository) {
        this.birthdayRepository = birthdayRepository;
        this.userRepository = userRepository;
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public Status getStatus(Long userId){
        return userRepository.getStatus(userId);
    }

    public boolean existUser(long userId){
        return userRepository.existsById(userId);
    }
}
