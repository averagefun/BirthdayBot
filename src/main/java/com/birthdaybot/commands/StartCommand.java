package com.birthdaybot.commands;

import com.birthdaybot.model.User;
import com.birthdaybot.services.DataService;

public class StartCommand extends BaseCommand{
    @Override
    public void execute(DataService dataService, Long chatId, Long userId, String text) {
        if(!dataService.existUser(userId)) {
            User newUser = new User();
            newUser.setId(userId);
            newUser.setUsername(text);
            dataService.addUser(newUser);
        }
    }
}
