package com.birthdaybot.commands;

import com.birthdaybot.model.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BackCommand extends BaseCommand{
    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String langCode = dataService.getLanguageCode(userId);
        SendMessage sendMessage = new SendMessage(chatId.toString(), localizate("chooseCommand", langCode));
        sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup(langCode));
        Store.addToSendQueue(sendMessage);
        dataService.updateStatusById(Status.BASE, userId);
    }
}
