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
public class BackToUserModeCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(BackToUserModeCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        logger.info("Executing BackToUserModeCommand...");
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);

        try {
            String langCode = dataService.getLanguageCode(userId);
            SendMessage sendMessage = new SendMessage(chatId.toString(), localizate("chooseCommand", langCode));
            sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup(langCode));

            dataService.updateStatusById(Status.BASE, userId);
            logger.info("User {} status updated to BASE", userId);

            dataService.updateIsGroupModeById(false, userId);
            logger.info("User {} switched to user mode (GroupMode: false)", userId);

            Store.addToSendQueue(sendMessage);
            logger.info("Message queued for user {} in chat {}", userId, chatId);
        } catch (Exception e) {
            logger.error("Error executing BackToUserModeCommand for user {}: {}", userId, e.getMessage(), e);
        }
    }
}