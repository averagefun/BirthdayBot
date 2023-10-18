package com.birthdaybot.commands;

import com.birthdaybot.exceptions.DayFormatException;
import com.birthdaybot.exceptions.FutureDateException;
import com.birthdaybot.exceptions.MonthFormatException;
import com.birthdaybot.exceptions.YearFormatException;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Store;
import com.birthdaybot.utills.validators.Validator;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.zip.DataFormatException;

@Component
public class AddCommand extends BaseCommand{

    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String text = executePair.getFirst();
        Status curStatus = dataService.getStatus(userId);
        String userLocate = dataService.getLanguageCode(userId);
        switch (curStatus){
            case BASE -> {
                Store.createBirthday(dataService.getUser(userId));
                dataService.updateStatusById(Status.NAME_WAITING, userId);
                Store.addToSendQueue(chatId, localizate("enterName",userLocate));
            }case NAME_WAITING -> {
                Birthday temp = Store.getBirthday(userId);
                temp.setName(text);
                Store.tempMap.put(userId,temp);
                dataService.updateStatusById(Status.BIRTHDAY_WAITING, userId);
                Store.addToSendQueue(chatId, localizate("enterDate",userLocate));
            }case BIRTHDAY_WAITING -> {
                try {
                    LocalDate birthday = Validator.parseDate(text);
                    Birthday temp = Store.getBirthday(userId);
                    temp.setDate(birthday);
                    Store.tempMap.remove(userId);
                    dataService.updateStatusById(Status.BASE, userId);
                    dataService.addBirthday(temp);
                    Store.addToSendQueue(chatId, localizate("successAdd", userLocate) + " " + temp.getName());
                } catch (DayFormatException e) {
                    Store.addToSendQueue(chatId, localizate("incorrectDay",userLocate));
                    Store.addToSendQueue(chatId, localizate("enterDate", userLocate));
                } catch (MonthFormatException e){
                    Store.addToSendQueue(chatId, localizate("incorrectMonth", userLocate));
                    Store.addToSendQueue(chatId, localizate("enterDate", userLocate));
                } catch (FutureDateException e){
                    Store.addToSendQueue(chatId, localizate("futureDate", userLocate));
                    Store.addToSendQueue(chatId, localizate("enterDate", userLocate));
                } catch (YearFormatException e){
                    Store.addToSendQueue(chatId, localizate("incorrectYear", userLocate));
                    Store.addToSendQueue(chatId, localizate("enterDate", userLocate));
                } catch (DataFormatException e){
                    Store.addToSendQueue(chatId, localizate("incorrectDateFormat", userLocate));
                    Store.addToSendQueue(chatId, localizate("enterDate", userLocate));
                }
            }
        }
    }

}