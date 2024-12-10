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
public class ChooseLangCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(ChooseLangCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long userId = getUserId(update);
        String text = executePair.getFirst();

        try {
            if (text.isEmpty()) {
                logger.info("User {} did not provide language input.", userId);
                SendMessage sendMessage = new SendMessage(userId.toString(), localizate("chooseLanguage", dataService.getLanguageCode(userId)));
                sendMessage.setReplyMarkup(Keyboard.languageKeyboard());
                Store.addToSendQueue(sendMessage);
                logger.info("Language selection keyboard sent to user {}", userId);
            } else {
                String locate = switch (text) {
                    case "setRussian" -> "ru";
                    case "setEnglish" -> "en";
                    case "setEmoji" -> "em";
                    default -> "unknown";
                };
                if (!locate.equals("unknown")) {
                    dataService.updateLangById(locate, userId);
                    logger.info("User {} language updated to {}", userId, locate);
                    SendMessage sendMessage = new SendMessage(userId.toString(), localizate("changedLanguage", locate));
                    sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup(locate));
                    Store.addToSendQueue(sendMessage);
                } else {
                    logger.warn("Invalid language selection by user {}: {}", userId, text);
                    SendMessage errorMsg = new SendMessage(userId.toString(), localizate("invalidLanguage", dataService.getLanguageCode(userId)));
                    Store.addToSendQueue(errorMsg);
                }
            }
        } catch (Exception e) {
            logger.error("Error executing ChooseLangCommand for user {}: {}", userId, e.getMessage(), e);
        }
    }
}
