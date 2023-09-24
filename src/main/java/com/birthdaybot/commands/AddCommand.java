package com.birthdaybot.commands;

import com.birthdaybot.exceptions.DayFormatException;
import com.birthdaybot.exceptions.FutureDateException;
import com.birthdaybot.exceptions.MonthFormatException;
import com.birthdaybot.exceptions.YearFormatException;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.Status;
import com.birthdaybot.model.User;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Store;
import com.birthdaybot.utills.validators.Validator;

import java.time.LocalDate;
import java.util.zip.DataFormatException;

public class AddCommand extends BaseCommand{

    @Override
    public void execute(DataService dataService, Long chatId, Long userId, String text) {
        Status curStatus = dataService.getStatus(userId);
        switch (curStatus){
            case BASE -> {
                Store.createBirthday(dataService.getUser(userId));
                dataService.updateStatusById(Status.NAME_WAITING, userId);
                Store.addToSendQueue(chatId, "Введите имя");
            }case NAME_WAITING -> {
                Birthday temp = Store.getBirthday(userId);
                temp.setName(text);
                Store.tempMap.put(userId,temp);
                dataService.updateStatusById(Status.BIRTHDAY_WAITING, userId);
                Store.addToSendQueue(chatId, "Введите дату");
            }case BIRTHDAY_WAITING -> {
                try {
                    LocalDate birthday = Validator.parseDate(text);
                    Birthday temp = Store.getBirthday(userId);
                    temp.setDate(birthday);
                    Store.tempMap.remove(userId);
                    dataService.updateStatusById(Status.BASE, userId);
                    dataService.addBirthday(temp);
                    Store.addToSendQueue(chatId, "Добавлен день рождения "+temp.getName());
                } catch (DayFormatException e) {
                    Store.addToSendQueue(chatId, "incorrect day");
                    Store.addToSendQueue(chatId, "Введите дату");
                } catch (MonthFormatException e){
                    Store.addToSendQueue(chatId, "incorrect month");
                    Store.addToSendQueue(chatId, "Введите дату");
                } catch (FutureDateException e){
                    Store.addToSendQueue(chatId, "future date");
                    Store.addToSendQueue(chatId, "Введите дату");
                } catch (YearFormatException e){
                    Store.addToSendQueue(chatId, "incorrect yer");
                    Store.addToSendQueue(chatId, "Введите дату");
                } catch (DataFormatException e){
                    Store.addToSendQueue(chatId, "incorrect date format");
                    Store.addToSendQueue(chatId, "Введите дату");
                }
            }
        }
    }

}
