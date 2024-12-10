package com.birthdaybot.commands;

import com.birthdaybot.model.enums.Status;
import com.birthdaybot.model.User;
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
public class StartCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(StartCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String username = update.getMessage().getFrom().getUserName();
        String langCode = update.getMessage().getFrom().getLanguageCode();

        try {
            if (!dataService.existUser(userId)) {
                User newUser = new User();
                newUser.setId(userId);
                newUser.setUsername(username);
                newUser.setLang(langCode);
                dataService.addUser(newUser);
                logger.info("Added new user: {} with username: {}", userId, username);
            }

            SendMessage sendMessage = new SendMessage(chatId.toString(), localizate("start", langCode));
            sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup(langCode));
            Store.addToSendQueue(sendMessage);
            dataService.updateStatusById(Status.BASE, userId);
            logger.info("Sent start message and updated status to BASE for user {}", userId);
        } catch (Exception e) {
            logger.error("Error executing StartCommand for user {}: {}", userId, e.getMessage(), e);
        }
    }
}
