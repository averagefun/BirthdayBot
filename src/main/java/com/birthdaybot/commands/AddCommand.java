package com.birthdaybot.commands;

import com.birthdaybot.exceptions.*;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.enums.Status;
import com.birthdaybot.services.AlarmService;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import com.birthdaybot.utills.validators.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.time.LocalDate;
import java.util.zip.DataFormatException;

@Component
@Scope("singleton") //default
public class AddCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(AddCommand.class);

    @Autowired
    AlarmService alarmService;

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String userName = update.getMessage().getFrom().getUserName();

        boolean isGroupMode = dataService.getIsGroupMode(userId);
        boolean isGroupAdmin = dataService.getIsGroupAdmin(userId);
        Long groupId = isGroupMode ? dataService.getGroupIdByUserId(userId) : userId;
        String text = executePair.getFirst();
        Status curStatus = dataService.getStatus(userId);
        String userLocate = dataService.getLanguageCode(userId);

        if (isGroupMode && !isGroupAdmin) {
            logger.warn("User {} (username: {}) attempted to add a birthday without admin rights in chat {}.", userId, userName, chatId);
            throw new NoAdminRightsException(userId, userLocate);
        }
        try {
            switch (curStatus) {
                case BASE -> {
                    logger.info("User {} (username: {}) is entering new birthday (BASE stage) in chat {}.", userId, userName, chatId);
                    Store.createBirthday(dataService.getUser(userId), groupId);
                    dataService.updateStatusById(Status.NAME_WAITING, userId);
                    SendMessage sm = new SendMessage(chatId.toString(), localizate("enterName", userLocate));
                    sm.setReplyMarkup(Keyboard.backButton(userLocate));
                    Store.addToSendQueue(sm);
                }
                case NAME_WAITING -> {
                    logger.info("User {} (username: {}) is entering name for birthday in chat {}.", userId, userName, chatId);
                    try {
                        Validator.validateName(text);
                        Birthday temp = Store.getBirthday(userId);
                        temp.setName(text);
                        Store.tempMap.put(userId, temp);
                        dataService.updateStatusById(Status.BIRTHDAY_WAITING, userId);
                        SendMessage sm = new SendMessage(chatId.toString(), localizate("enterDate", userLocate));
                        sm.setReplyMarkup(Keyboard.backButton(userLocate));
                        Store.addToSendQueue(sm);
                    } catch (Exception e) {
                        logger.error("Error validating name for user {} (username: {}): {}", userId, userName, e.getMessage());
                        SendMessage error = new SendMessage(userId.toString(), localizate("longName", userLocate));
                        Store.addToSendQueue(error);
                    }
                }
                case BIRTHDAY_WAITING -> {
                    logger.info("User {} (username: {}) is entering birthday date in chat {}.", userId, userName, chatId);
                    try {
                        LocalDate birthday = Validator.parseDate(text);
                        Birthday temp = Store.getBirthday(userId);
                        temp.setDate(birthday);
                        Store.tempMap.remove(userId);
                        dataService.updateStatusById(Status.BASE, userId);
                        dataService.addBirthdayAndAlarm(temp);
                        SendMessage sm = new SendMessage(chatId.toString(), localizate("successAdd", userLocate) + " " + temp.getName());
                        sm.setReplyMarkup(isGroupMode ? Keyboard.replyKeyboardMarkupGroupMode(userLocate, true) : Keyboard.replyKeyboardMarkup(userLocate));
                        Store.addToSendQueue(sm);
                    } catch (DayFormatException e) {
                        logger.warn("Incorrect day format for user {} (username: {}): {}", userId, userName, e.getMessage());
                        handleDateException(chatId, userLocate, "incorrectDay");
                    } catch (MonthFormatException e) {
                        logger.warn("Incorrect month format for user {} (username: {}): {}", userId, userName, e.getMessage());
                        handleDateException(chatId, userLocate, "incorrectMonth");
                    } catch (FutureDateException e) {
                        logger.warn("Future date entered by user {} (username: {}): {}", userId, userName, e.getMessage());
                        handleDateException(chatId, userLocate, "futureDate");
                    } catch (YearFormatException e) {
                        logger.warn("Incorrect year format for user {} (username: {}): {}", userId, userName, e.getMessage());
                        handleDateException(chatId, userLocate, "incorrectYear");
                    } catch (DataFormatException e) {
                        logger.error("Invalid date format entered by user {} (username: {}): {}", userId, userName, e.getMessage());
                        handleDateException(chatId, userLocate, "incorrectDateFormat");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected error during AddCommand execution for user {} (username: {}): {}", userId, userName, e.getMessage(), e);
        }
    }

    private void handleDateException(Long chatId, String userLocate, String errorKey) {
        Store.addToSendQueue(chatId, localizate(errorKey, userLocate));
        Store.addToSendQueue(chatId, localizate("enterDate", userLocate));
    }
}