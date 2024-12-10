package com.birthdaybot.commands;

import com.birthdaybot.model.enums.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BackCommand extends BaseCommand{
    private static final Logger logger = LoggerFactory.getLogger(BackCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String userName = update.getMessage().getFrom().getUserName();

        String langCode = dataService.getLanguageCode(userId);
        boolean isGroupMode = dataService.getIsGroupMode(userId);
        boolean isGroupAdmin = dataService.getIsGroupAdmin(userId);

        SendMessage sendMessage = new SendMessage(chatId.toString(), localizate("chooseCommand", langCode));

        if (!isGroupMode) {
            logger.info("Setting private chat keyboard for user {} (username: {})", userId, userName);
            sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup(langCode));
        } else {
            logger.info("Setting group mode keyboard for user {} (username: {}) (Admin: {})", userId, userName, isGroupAdmin);
            sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkupGroupMode(langCode, isGroupAdmin));
        }
        Store.addToSendQueue(sendMessage);
        logger.info("Message queued for user {} (username: {}) in chat {}", userId, userName, chatId);
        dataService.updateStatusById(Status.BASE, userId);
    }
}
