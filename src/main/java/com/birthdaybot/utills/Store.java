package com.birthdaybot.utills;

import com.birthdaybot.exceptions.RestartServerException;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.User;
import lombok.Getter;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Store {

    @Getter
    private static BlockingDeque<Pair<Long, Object>> queueToSend=new LinkedBlockingDeque<>();

    public static HashMap<Long, Birthday> tempMap = new HashMap<>();

    public static Birthday getBirthday(Long userId){
        if(!tempMap.containsKey(userId)) throw new RestartServerException();
        return tempMap.get(userId);
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

    public static void addToSendQueue(DeleteMessage message){
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
    }
}
