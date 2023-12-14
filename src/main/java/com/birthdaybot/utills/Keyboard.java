package com.birthdaybot.utills;

import com.birthdaybot.model.Birthday;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Keyboard {

    private static final InlineKeyboardButton SELECT_RUSSIAN_BUTTON = new InlineKeyboardButton("currentLanguage");
    private static final InlineKeyboardButton SELECT_ENGLISH_BUTTON = new InlineKeyboardButton("currentLanguage");
    private static final InlineKeyboardButton SELECT_EMOJI_BUTTON = new InlineKeyboardButton("currentLanguage");



    public static InlineKeyboardMarkup languageKeyboard() {
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

    public static InlineKeyboardMarkup calendarKeyboard(String locale) {
        InlineKeyboardButton JANUARY_BUTTON = new InlineKeyboardButton("january");
        JANUARY_BUTTON.setCallbackData("showJanuary");
        InlineKeyboardButton FEBRUARY_BUTTON = new InlineKeyboardButton("february");
        FEBRUARY_BUTTON.setCallbackData("showFebruary");
        InlineKeyboardButton MARCH_BUTTON = new InlineKeyboardButton("march");
        MARCH_BUTTON.setCallbackData("showMarch");
        InlineKeyboardButton APRIL_BUTTON = new InlineKeyboardButton("april");
        APRIL_BUTTON.setCallbackData("showApril");
        InlineKeyboardButton MAY_BUTTON = new InlineKeyboardButton("may");
        MAY_BUTTON.setCallbackData("showMay");
        InlineKeyboardButton JUNE_BUTTON = new InlineKeyboardButton("june");
        JUNE_BUTTON.setCallbackData("showJune");
        InlineKeyboardButton JULY_BUTTON = new InlineKeyboardButton("july");
        JULY_BUTTON.setCallbackData("showJuly");
        InlineKeyboardButton AUGUST_BUTTON = new InlineKeyboardButton("august");
        AUGUST_BUTTON.setCallbackData("showAugust");
        InlineKeyboardButton SEPTEMBER_BUTTON = new InlineKeyboardButton("september");
        SEPTEMBER_BUTTON.setCallbackData("showSeptember");
        InlineKeyboardButton OCTOBER_BUTTON = new InlineKeyboardButton("october");
        OCTOBER_BUTTON.setCallbackData("showOctober");
        InlineKeyboardButton NOVEMBER_BUTTON = new InlineKeyboardButton("november");
        NOVEMBER_BUTTON.setCallbackData("showNovember");
        InlineKeyboardButton DECEMBER_BUTTON = new InlineKeyboardButton("december");
        DECEMBER_BUTTON.setCallbackData("showDecember");
        List<InlineKeyboardButton> rowInline1 = List.of(DECEMBER_BUTTON, JANUARY_BUTTON, FEBRUARY_BUTTON);
        List<InlineKeyboardButton> rowInline2 = List.of(MARCH_BUTTON, APRIL_BUTTON, MAY_BUTTON);
        List<InlineKeyboardButton> rowInline3 = List.of(JUNE_BUTTON, JULY_BUTTON, AUGUST_BUTTON);
        List<InlineKeyboardButton> rowInline4 = List.of(SEPTEMBER_BUTTON, OCTOBER_BUTTON, NOVEMBER_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline1, rowInline2, rowInline3, rowInline4);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }


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

    private static List<KeyboardRow> keyboardRows(String langCode) {
        List<KeyboardRow> rows = new ArrayList<>();
        //создаем список рядов кнопок из списка кнопок
        rows.add(new KeyboardRow(List.of(new KeyboardButton("addBirthday" + " " + "addSmile"))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton("showBirthday" + " " + "showSmile"))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton("settings" + " " + "settingsEmoji"))));
        return rows;
    }


    public static InlineKeyboardMarkup showKeyboardAdd(String locate, ArrayList<Birthday> birthdays, Integer start, String month) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        for (int i = start; i < Math.min(birthdays.size(), start + 5); i++) {
            List<InlineKeyboardButton> rowInline = getBirthdaysRows(locate, birthdays, i, month);
            rowsInLine.add(rowInline);
        }
        List<InlineKeyboardButton> rowInLine = getLastShowBirthdaysRow(locate, birthdays, start, month);
        rowsInLine.add(rowInLine);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }

    private static List<InlineKeyboardButton> getLastShowBirthdaysRow(String locate, ArrayList<Birthday> birthdays, Integer start, String month) {
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        if (start >= 4) {
            InlineKeyboardButton prevButton = new InlineKeyboardButton("⏪ " + "back");
            prevButton.setCallbackData(month + ";" + (start - 5));
            rowInLine.add(prevButton);
        }

        InlineKeyboardButton backButton = new InlineKeyboardButton("toCalendar" + " \ud83d\udcc5");
        backButton.setCallbackData("backToCalendar");
        rowInLine.add(backButton);

        if (start + 5 < birthdays.size()) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton("next" + " ⏩");
            nextButton.setCallbackData(month + ";" + (start + 5));
            rowInLine.add(nextButton);
        }
        return rowInLine;
    }

    private static List<InlineKeyboardButton> getBirthdaysRows(String locate, ArrayList<Birthday> birthdays, int i, String month) {
        Birthday birthday = birthdays.get(i);
        InlineKeyboardButton birthday_button = new InlineKeyboardButton(birthday.getName());
        birthday_button.setCallbackData("String.valueOf(i)");


        String birthdayDate = birthday.getDate().getDayOfMonth() < 10 ? "0" + birthday.getDate().getDayOfMonth() : "" + birthday.getDate().getDayOfMonth();
        String years;
        if (Objects.equals(locate, "em")) birthdayDate = EmojiConverter.convertedString(birthdayDate);
        birthdayDate += " " +
                Store.monthMap.get(birthday.getDate().getMonthValue());
        if (birthday.getDate().getYear() != 1) {
            if (Objects.equals(locate, "em")) {
                int year = birthday.getDate().getYear() % 100;
                years = year < 10 ? "0" + year : "" + year;
                birthdayDate += " " + EmojiConverter.convertedString(years);
            } else {
                birthdayDate += " " + birthday.getDate().getYear();
            }
        }


        InlineKeyboardButton date_button = new InlineKeyboardButton(birthdayDate);
        date_button.setCallbackData(String.valueOf(i));

        InlineKeyboardButton remove_button = new InlineKeyboardButton(" \ud83d\uddd1");
        remove_button.setCallbackData(month + ";remove;" + birthday.getId());

        return List.of(birthday_button, date_button, remove_button);
    }

    public static ReplyKeyboardMarkup settingsKeyboard(String langCode) {
        List<KeyboardRow> rows = new ArrayList<>();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //следующие три строчки могут менять значение аргументов взависимости от ваших задач
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        //добавляем "клавиатуру"
        rows.add(new KeyboardRow(List.of(new KeyboardButton("share" + " " + "shareSmile"))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton("info" + " " + "infoSmile"))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton("tomeZone" + " " + "timeEmoji"))));
        rows.add(new KeyboardRow(List.of(new KeyboardButton("undo" + " " + "undoEmoji"))));
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup backButton(String langCode){
        List<KeyboardRow> rows = new ArrayList<>();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //следующие три строчки могут менять значение аргументов взависимости от ваших задач
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        rows.add(new KeyboardRow(List.of(new KeyboardButton("undo"+ " " + "undoEmoji"))));
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }


}