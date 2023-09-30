package com.birthdaybot.utills;

import com.birthdaybot.utills.localization.TextProviderImpl;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard extends TextProviderImpl {
    private static final InlineKeyboardButton ADD_BIRTHDAY_BUTTON = new InlineKeyboardButton("Добавить день рождения \uD83D\uDEBE");
    private static final InlineKeyboardButton SHOW_BIRTHDAYS_BUTTON = new InlineKeyboardButton("Показать дни рождения \u26A7");
    private static final InlineKeyboardButton SHARE_BIRTHDAYS_BUTTON = new InlineKeyboardButton("Поделиться \u27A1");
    private static final InlineKeyboardButton INFO_BUTTON = new InlineKeyboardButton("Инфо \u2139");
    private static final InlineKeyboardButton SELECT_RUSSIAN_BUTTON = new InlineKeyboardButton(localizate("currentLanguage", "ru"));
    private static final InlineKeyboardButton SELECT_ENGLISH_BUTTON = new InlineKeyboardButton(localizate("currentLanguage", "uk"));
    private static final InlineKeyboardButton SELECT_EMOJI_BUTTON = new InlineKeyboardButton(localizate("currentLanguage", "em"));



    @Deprecated
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

    public static InlineKeyboardMarkup languageKeyboard(){
        SELECT_RUSSIAN_BUTTON.setCallbackData("setRussian");
        SELECT_ENGLISH_BUTTON.setCallbackData("setEnglish");
        SELECT_EMOJI_BUTTON.setCallbackData("setEmoji");
        List<InlineKeyboardButton> rowInline1 = List.of(SELECT_RUSSIAN_BUTTON);
        List<InlineKeyboardButton> rowInline2 = List.of(SELECT_ENGLISH_BUTTON);
        List<InlineKeyboardButton> rowInline3 = List.of(SELECT_EMOJI_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline1, rowInline2, rowInline3);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    @Bean
    public static ReplyKeyboardMarkup replyKeyboardMarkup(String langCode) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //следующие три строчки могут менять значение аргументов взависимости от ваших задач
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        //добавляем "клавиатуру"
        replyKeyboardMarkup.setKeyboard(keyboardRows(langCode));
        return replyKeyboardMarkup;
    }

    @Bean
    private static List<KeyboardRow> keyboardRows(String langCode) {
        List<KeyboardRow> rows = new ArrayList<>();
        //создаем список рядов кнопок из списка кнопок
        rows.add(new KeyboardRow(List.of(new KeyboardButton(localizate("addBirthday", langCode) +" "+ localizate("addSmile", langCode)))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton(localizate("showBirthday", langCode)+" "+localizate("showSmile", langCode)))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton(localizate("share", langCode)+" "+localizate("shareSmile", langCode)),
                new KeyboardButton(localizate("info", langCode)+" "+localizate("infoSmile", langCode)),
                new KeyboardButton(localizate("language", langCode) +  " " + localizate("flag", langCode)))));

        return rows;
    }
}