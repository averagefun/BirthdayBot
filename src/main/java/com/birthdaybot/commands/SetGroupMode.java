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
public class SetGroupMode extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(SetGroupMode.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        boolean isAdmin = Boolean.parseBoolean(executePair.getFirst());
        String userLocate = dataService.getLanguageCode(userId);

        try {
            dataService.updateIsGroupModeById(true, userId);
            dataService.updateIsAdminById(isAdmin, userId);
            dataService.setGroupId(chatId, userId);
            logger.info("Group mode enabled for user {} in chat {}", userId, chatId);

            String langCode = dataService.getLanguageCode(userId);
            SendMessage sendMessage = new SendMessage(userId.toString(), localizate("setUpGroup", userLocate) + update.getMessage().getChat().getTitle());
            sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkupGroupMode(langCode, isAdmin));
            Store.addToSendQueue(sendMessage);
            logger.info("Group setup message sent to user {}", userId);
        } catch (Exception e) {
            logger.error("Error executing SetGroupMode for user {}: {}", userId, e.getMessage(), e);
        }
    }
}
