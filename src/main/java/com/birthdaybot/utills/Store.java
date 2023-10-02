package com.birthdaybot.utills;

import com.birthdaybot.exceptions.RestartServerException;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.User;
import lombok.Getter;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Store {
    public static final Map<Integer, String> monthMap = Map.ofEntries(
            Map.entry(1, "inJanuary"),
            Map.entry(2, "inFebruary"),
            Map.entry(3, "inMarch"),
            Map.entry(4, "inApril"),
            Map.entry(5, "inMay"),
            Map.entry(6, "inJune"),
            Map.entry(7, "inJuly"),
            Map.entry(8, "inAugust"),
            Map.entry(9, "inSeptember"),
            Map.entry(10, "inOctober"),
            Map.entry(11, "inNovember"),
            Map.entry(12, "inDecember")
    );

    @Getter
    private static final BlockingDeque<Pair<Long, Object>> queueToSend=new LinkedBlockingDeque<>();

    @Getter
    private static final BlockingDeque<Pair<String, Update>> queueToProcess = new LinkedBlockingDeque<>();

    public static HashMap<Long, Birthday> tempMap = new HashMap<>();

    public static Birthday getBirthday(Long userId){
        if(!tempMap.containsKey(userId)) throw new RestartServerException();
        return tempMap.get(userId);
    }

    public static void addToProcessQueue(String string, Update update){
        queueToProcess.add(Pair.of(string, update));
    }

    public static void addToProcessQueue(Update update){
        queueToProcess.add(Pair.of("", update));
    }

    public static void createBirthday (User user) {
        Birthday birthday = new Birthday();
        birthday.setId(user.getId());
        birthday.setOwner(user);
        tempMap.put(user.getId(), birthday);
    }


    public static void addToSendQueue(Long chatId, String message){
        SendMessage NewsendMessage=new SendMessage();
        NewsendMessage.setText(message);
        queueToSend.add(Pair.of(chatId, NewsendMessage));
    }

    public static void addToSendQueue(SendMessage message){
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
    }
    public static void addToSendQueue(EditMessageReplyMarkup message){
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
    }

    public static void addToSendQueue(DeleteMessage message){
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
    }
}
