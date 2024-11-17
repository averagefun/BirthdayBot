package com.birthdaybot.commands;

import com.birthdaybot.model.enums.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Keyboard;
import com.birthdaybot.utills.Store;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class SetGroupMode extends BaseCommand {
    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        boolean isAdmin = Boolean.parseBoolean(executePair.getFirst());
        String userLocate = dataService.getLanguageCode(userId);

        dataService.updateIsGroupModeById(true, userId);
        dataService.updateIsAdminById(isAdmin, userId);
        dataService.setGroupId(chatId, userId);
        String langCode = dataService.getLanguageCode(userId);

        SendMessage sendMessage = new SendMessage(userId.toString(), localizate("setUpGroup", userLocate) + update.getMessage().getChat().getTitle());
        sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkupGroupMode(langCode, isAdmin));
        Store.addToSendQueue(sendMessage);

    }
}