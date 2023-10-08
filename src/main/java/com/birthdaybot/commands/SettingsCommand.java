package com.birthdaybot.commands;

import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Scope("singleton")//default
public class SettingsCommand extends BaseCommand {
    @Override
    public void execute(DataService dataService) throws InterruptedException{
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String langCode = dataService.getLanguageCode(userId);
        SendMessage sendMessage = new SendMessage(chatId.toString(), localizate("settings", langCode));
        sendMessage.setReplyMarkup(Keyboard.settingsKeyboard(langCode));
        Store.addToSendQueue(sendMessage);
    }
}
