package com.birthdaybot.commands;


import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Keyboard;
import com.birthdaybot.utills.Store;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ChooseLangCommand extends BaseCommand {
    @Override
    public void execute(DataService dataService, Long chatId, Long userId, String text) {
        switch (text){
            case ""->{
                SendMessage sendMessage = new SendMessage(userId.toString(), localizate("chooseLanguage", dataService.getLanguageCode(userId)));
                sendMessage.setReplyMarkup(Keyboard.languageKeyboard());
                Store.addToSendQueue(sendMessage);
            }case "setRussian"->{
                dataService.updateLangById("ru", userId);
                SendMessage sendMessage = new SendMessage(userId.toString(), localizate("changedLanguage", "ru"));
                sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup("ru"));
                Store.addToSendQueue(sendMessage);
            }case "setEnglish"->{
                dataService.updateLangById("en", userId);
                SendMessage sendMessage = new SendMessage(userId.toString(), localizate("changedLanguage", "en"));
                sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup("en"));
                Store.addToSendQueue(sendMessage);
            }case "setEmoji"->{
                dataService.updateLangById("em", userId);
                SendMessage sendMessage = new SendMessage(userId.toString(), localizate("changedLanguage", "em"));
                sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup("em"));
                Store.addToSendQueue(sendMessage);
            }
        }

    }
}
