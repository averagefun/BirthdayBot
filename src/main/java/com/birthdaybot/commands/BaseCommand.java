package com.birthdaybot.commands;

import com.birthdaybot.services.DataService;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class BaseCommand {
    public abstract void execute(DataService dataService) throws InterruptedException;

    Long getChatId(Update update){
        if (update.hasMessage()){
            return update.getMessage().getChatId();
        } else {
            return update.getCallbackQuery().getMessage().getChatId();
        }
    }

    Long getUserId(Update update){
        if (update.hasMessage()){
            return update.getMessage().getFrom().getId();
        } else {
            return update.getCallbackQuery().getFrom().getId();
        }
    }

    Integer getMessageId(Update update){
        if (update.hasMessage()){
            return update.getMessage().getMessageId();
        } else {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
    }
}