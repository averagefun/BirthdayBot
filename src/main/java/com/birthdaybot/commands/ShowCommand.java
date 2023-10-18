package com.birthdaybot.commands;

import com.birthdaybot.model.Birthday;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.ArrayList;

@Component
public class ShowCommand extends BaseCommand {
    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String s = executePair.getFirst();
        String[] arr = s.split(";");
        String text = arr[0];
        if (text.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Выберите месяц");
            sendMessage.setReplyMarkup(Keyboard.calendarKeyboard(dataService.getLanguageCode(userId)));
            Store.addToSendQueue(sendMessage);
        } else if (text.equals("backToCalendar")) {
            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
            editMessageReplyMarkup.setChatId(chatId);
            editMessageReplyMarkup.setMessageId(getMessageId(update));
            editMessageReplyMarkup.setReplyMarkup(Keyboard.calendarKeyboard(dataService.getLanguageCode(userId)));
            Store.addToSendQueue(editMessageReplyMarkup);
        } else {
            int month = getMonth(text);
            int start=0;
            if (arr.length<3) {
                if(arr.length==2)
                    start = Integer.parseInt(arr[1]);

            } else if (arr.length==3) {
                String command = arr[1];
                Long id = Long.valueOf(arr[2]);
                if(command.equals("remove")){
                    dataService.deleteBirthdayById(id);
                }
            }
            ArrayList<Birthday> allBirthdays = dataService.findBirthdaysByOwnerId(userId);
            ArrayList<Birthday> birthdays = new ArrayList<>();
            for(Birthday b: allBirthdays){
                if(b.getDate().getMonthValue()==month){
                    birthdays.add(b);
                }
            }
            
            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
            editMessageReplyMarkup.setMessageId(getMessageId(update));
            editMessageReplyMarkup.setChatId(chatId);
            InlineKeyboardMarkup inlineKeyboardMarkup = Keyboard.showKeyboardAdd(dataService.getLanguageCode(userId), birthdays, start, text);
            editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
            Store.addToSendQueue(editMessageReplyMarkup);

        }
    }

    private static int getMonth(String text) {
        int month=0;
        switch (text) {
            case "showJanuary" -> {
                month = 1;
            }
            case "showFebruary" -> {
                month = 2;
            }
            case "showMarch" -> {
                month = 3;
            }
            case "showApril" -> {
                month = 4;
            }
            case "showMay" -> {
                month = 5;
            }
            case "showJune" -> {
                month = 6;
            }
            case "showJuly" -> {
                month = 7;
            }
            case "showAugust" -> {
                month = 8;
            }
            case "showSeptember" -> {
                month = 9;
            }
            case "showOctober" -> {
                month = 10;
            }
            case "showNovember" -> {
                month = 11;
            }
            case "showDecember" -> {
                month = 12;
            }
        }
        return month;
    }
}
