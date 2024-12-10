package com.birthdaybot.commands;

import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Scope("singleton") //default
public class SettingsCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(SettingsCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException{
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);

        try {
            String langCode = dataService.getLanguageCode(userId);
            SendMessage sendMessage = new SendMessage(chatId.toString(), localizate("settings", langCode));
            sendMessage.setReplyMarkup(Keyboard.settingsKeyboard(langCode));
            Store.addToSendQueue(sendMessage);
            logger.info("Settings message sent to user {} in chat {}", userId, chatId);
        } catch (Exception e) {
            logger.error("Error executing SettingsCommand for user {}: {}", userId, e.getMessage(), e);
        }
    }
}
