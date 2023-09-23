package com.birthdaybot.utills;

import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    private static final InlineKeyboardButton ADD_BIRTHDAY_BUTTON = new InlineKeyboardButton("Добавить день рождения \uD83D\uDEBE");
    private static final InlineKeyboardButton SHOW_BIRTHDAYS_BUTTON = new InlineKeyboardButton("Показать дни рождения \u26A7");
    private static final InlineKeyboardButton SHARE_BIRTHDAYS_BUTTON = new InlineKeyboardButton("Поделиться \u27A1");
    private static final InlineKeyboardButton INFO_BUTTON = new InlineKeyboardButton("Инфо \u2139");


    public static InlineKeyboardMarkup startKeyboard() {
        ADD_BIRTHDAY_BUTTON.setCallbackData("/add");
        SHOW_BIRTHDAYS_BUTTON.setCallbackData("/show");
        SHARE_BIRTHDAYS_BUTTON.setCallbackData("/share");
        INFO_BUTTON.setCallbackData("/info");

        List<InlineKeyboardButton> rowInline1 = List.of(ADD_BIRTHDAY_BUTTON);
        List<InlineKeyboardButton> rowInline2 = List.of(SHOW_BIRTHDAYS_BUTTON);
        List<InlineKeyboardButton> rowInline3 = List.of(SHARE_BIRTHDAYS_BUTTON, INFO_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline1, rowInline2, rowInline3);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    @Bean
    public static ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //следующие три строчки могут менять значение аргументов взависимости от ваших задач
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        //добавляем "клавиатуру"
        replyKeyboardMarkup.setKeyboard(keyboardRows());
        return replyKeyboardMarkup;
    }

    @Bean
    private static List<KeyboardRow> keyboardRows() {
        List<KeyboardRow> rows = new ArrayList<>();
        //создаем список рядов кнопок из списка кнопок
        rows.add(new KeyboardRow(List.of(new KeyboardButton("Добавить день рождения \uD83D\uDEBE"))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton("Показать дни рождения \u26A7"))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton("Поделиться \u27A1"), new KeyboardButton("Инфо \u2139"))));


        return rows;
    }
}