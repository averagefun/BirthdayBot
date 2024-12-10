package com.birthdaybot.commands;

import com.birthdaybot.exceptions.NoAdminRightsException;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.ArrayList;

@Component
@Scope("singleton") //default
public class ShowCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(ShowCommand.class);

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);

        try {
            String s = executePair.getFirst();
            String[] arr = s.split(";");
            String text = arr[0];
            String userLocate = dataService.getLanguageCode(userId);

            boolean isGroupMode = dataService.getIsGroupMode(userId);
            boolean isGroupAdmin = dataService.getIsGroupAdmin(userId);
            Long groupId = isGroupMode ? dataService.getGroupIdByUserId(userId) : userId;

            if (text.isEmpty()) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Выберите месяц");
                sendMessage.setReplyMarkup(Keyboard.calendarKeyboard(dataService.getLanguageCode(userId)));
                Store.addToSendQueue(sendMessage);
                logger.info("Sent calendar selection message to user {}", userId);
            } else if (text.equals("backToCalendar")) {
                EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
                editMessageReplyMarkup.setChatId(chatId);
                editMessageReplyMarkup.setMessageId(getMessageId(update));
                editMessageReplyMarkup.setReplyMarkup(Keyboard.calendarKeyboard(dataService.getLanguageCode(userId)));
                Store.addToSendQueue(editMessageReplyMarkup);
                logger.info("User {} returned to calendar view", userId);
            } else {
                int month = getMonth(text);
                int start = arr.length < 3 ? (arr.length == 2 ? Integer.parseInt(arr[1]) : 0) : 0;

                if (arr.length == 3) {
                    String command = arr[1];
                    Long id = Long.valueOf(arr[2]);
                    if (command.equals("remove")) {
                        if (!isGroupAdmin && isGroupMode) {
                            logger.warn("User {} attempted to remove a birthday without admin rights", userId);
                            throw new NoAdminRightsException(userId, userLocate);
                        }
                        dataService.deleteBirthdayById(id);
                        logger.info("Birthday with ID {} removed by user {}", id, userId);
                    }
                }
                ArrayList<Birthday> allBirthdays = dataService.findBirthdaysByChatId(groupId);
                ArrayList<Birthday> birthdays = new ArrayList<>();
                for (Birthday b : allBirthdays) {
                    if (b.getDate().getMonthValue() == month) {
                        birthdays.add(b);
                    }
                }

                EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
                editMessageReplyMarkup.setMessageId(getMessageId(update));
                editMessageReplyMarkup.setChatId(chatId);
                InlineKeyboardMarkup inlineKeyboardMarkup = Keyboard.showKeyboardAdd(userLocate, birthdays, start, text);
                editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
                Store.addToSendQueue(editMessageReplyMarkup);
                logger.info("Displayed birthdays for month {} to user {}", month, userId);
            }
        } catch (Exception e) {
            logger.error("Error executing ShowCommand for user {}: {}", userId, e.getMessage(), e);
        }
    }

    private static int getMonth(String text) {
        return switch (text) {
            case "showJanuary" -> 1;
            case "showFebruary" -> 2;
            case "showMarch" -> 3;
            case "showApril" -> 4;
            case "showMay" -> 5;
            case "showJune" -> 6;
            case "showJuly" -> 7;
            case "showAugust" -> 8;
            case "showSeptember" -> 9;
            case "showOctober" -> 10;
            case "showNovember" -> 11;
            case "showDecember" -> 12;
            default -> 0;
        };
    }
}