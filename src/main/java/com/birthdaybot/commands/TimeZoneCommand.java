package com.birthdaybot.commands;

import com.birthdaybot.exceptions.TimeZoneException;
import com.birthdaybot.model.enums.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.EmojiConverter;
import com.birthdaybot.utills.Store;
import com.birthdaybot.utills.validators.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TimeZoneCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(TimeZoneCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long userId = getUserId(update);
        Long chatId = getChatId(update);
        String username = update.getMessage().getFrom().getUserName();

        try {
            Status curStatus = dataService.getStatus(userId);
            String userLocate = dataService.getLanguageCode(userId);

            if (curStatus != Status.TIME_ZONE_WAITING) {
                Integer timeZone = dataService.getTimeZone(userId);
                String gmt = "GMT";
                if (timeZone > 0) {
                    gmt += "+";
                }
                SendMessage sendMessage = new SendMessage(userId.toString(), localizate("timeZone", userLocate) + " " + gmt + timeZone);
                Store.addToSendQueue(sendMessage);
                logger.info("Sent current timezone to user - chatId: {}, userId: {}, username: {}: {}", chatId, userId, username != null ? username : "unknown", timeZone);

                SendMessage sm2 = new SendMessage(userId.toString(), localizate("newTimeZone", userLocate));
                Store.addToSendQueue(sm2);
                dataService.updateStatusById(Status.TIME_ZONE_WAITING, userId);
                logger.info("Updated status to TIME_ZONE_WAITING - chatId: {}, userId: {}, username: {}", chatId, userId, username != null ? username : "unknown");
            } else {
                SendMessage message = new SendMessage();
                message.setChatId(userId);
                try {
                    Integer newTimeZone = Validator.validateTimeZone(update.getMessage().getText());
                    dataService.setTimeZone(newTimeZone, userId);
                    String gmt = "GMT";
                    if (newTimeZone > 0) {
                        gmt += "+";
                    }
                    String response = localizate("successTimeZone", userLocate);
                    response += userLocate.equals("em") ? EmojiConverter.convertedString(newTimeZone.toString()) : " " + gmt + newTimeZone;
                    message.setText(response);
                    dataService.updateStatusById(Status.BASE, userId);
                    logger.info("Successfully updated timezone - chatId: {}, userId: {}, username: {} to {}", chatId, userId, username != null ? username : "unknown", newTimeZone);
                } catch (TimeZoneException e) {
                    message.setText(localizate("timeZoneFormat", userLocate));
                    logger.warn("Invalid timezone format - chatId: {}, userId: {}, username: {}", chatId, userId, username != null ? username : "unknown");
                } catch (Exception e) {
                    message.setText(localizate("wrongTimeZone", userLocate));
                    logger.error("Error updating timezone - chatId: {}, userId: {}, username: {}: {}", chatId, userId, username != null ? username : "unknown", e.getMessage(), e);
                } finally {
                    Store.addToSendQueue(message);
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected error - chatId: {}, userId: {}, username: {}: {}", chatId, userId, username != null ? username : "unknown", e.getMessage(), e);
        }
    }
}
