package com.birthdaybot.commands;

import com.birthdaybot.model.Status;
import com.birthdaybot.model.User;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Keyboard;
import com.birthdaybot.utills.Store;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

public class StartCommand extends BaseCommand{
    @Override
    public void execute(DataService dataService, Long chatId, Long userId, String text) {
        if(!dataService.existUser(userId)) {
            String[] temp= text.split(" ");
            User newUser = new User();
            newUser.setId(userId);
            newUser.setUsername(temp[0]);
            newUser.setLang(temp[1]);
            dataService.addUser(newUser);
            SendMessage sendMessage = new SendMessage(chatId.toString(), localizate("start", temp[1]));
            sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup(temp[1]));
            Store.addToSendQueue(sendMessage);
        }
        dataService.updateStatusById(Status.BASE, userId);
    }
}
