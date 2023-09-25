package com.birthdaybot.utills;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Store {
    @Getter
    public static BlockingDeque<Pair<Long, Object>> queueToSend=new LinkedBlockingDeque<>();

    public static void addToSendQueue(Long chatId, String message){
        SendMessage NewsendMessage=new SendMessage();
        NewsendMessage.setText(message);
        queueToSend.add(Pair.of(chatId, NewsendMessage));
    }

    public static void addToSendQueue(SendMessage message){
        queueToSend.add(Pair.of(Long.parseLong(message.getChatId()), message));
    }
}
