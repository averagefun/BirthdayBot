package com.birthdaybot.commands;

import com.birthdaybot.model.User;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ShareCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(ShareCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);

        try {
            String langCode = dataService.getLanguageCode(userId);
            User user = dataService.getUser(userId);
            if (user.getShareCode() == null) {
                user.setShareCode(user.getId() + (long) (Math.PI * 100000000));
                logger.info("Generated new share code for user {}", userId);
            }
            dataService.addUser(user);
            Long shareCode = user.getShareCode();
            Store.addToSendQueue(chatId, localizate("yourShareCode", langCode) + " " + shareCode);
            logger.info("Share code sent to user {} in chat {}", userId, chatId);
        } catch (Exception e) {
            logger.error("Error executing ShareCommand for user {}: {}", userId, e.getMessage(), e);
        }
    }
}
