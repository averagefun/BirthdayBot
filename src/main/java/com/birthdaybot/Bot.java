package com.birthdaybot;

import com.birthdaybot.commands.*;
import com.birthdaybot.model.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.MyBean;
import com.birthdaybot.utills.Store;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    @Qualifier("dataService")
    private DataService dataService;

    @Value("${telegram.bot.name}")
    private String botUsername;

    public Bot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            Long userId = update.getMessage().getFrom().getId();
            Long chatId = update.getMessage().getChatId();
            log.info(update.getMessage().getFrom().getUserName() + " " + update.getMessage().getText());
            switch (message.getText()) {
                case "/start":
                    Store.addToProcessQueue(update);
                    BaseCommand startCommand = MyBean.getApplicationContext().getBean("startCommand", BaseCommand.class);
                    startCommand.execute(dataService);
                    break;
                case "/add", "Добавить день рождения \uD83D\uDEAE", "Add a birthday \uD83D\uDEAE", "✍ \uD83C\uDD95 \uD83D\uDC23":
                    dataService.updateStatusById(Status.BASE, userId);
                    Store.addToProcessQueue(update);
                    BaseCommand addCommand = MyBean.getApplicationContext().getBean("addCommand", BaseCommand.class);
                    addCommand.execute(dataService);
                    break;
                case "/lang", "Язык \ud83c\uddf7\ud83c\uddfa", "Language \ud83c\uddec\ud83c\udde7", "♻ \ud83d\ude00":
                    BaseCommand chooseLangCommand = MyBean.getApplicationContext().getBean("chooseLangCommand", BaseCommand.class);
                    Store.addToProcessQueue(update);
                    chooseLangCommand.execute(dataService);
                    break;
                case "/show", "Показать дни рождения \uD83D\uDC41", "Show birthdays \uD83D\uDC41", "\uD83D\uDD22 \uD83C\uDF10 \uD83C\uDF82":
                    BaseCommand showCommand = MyBean.getApplicationContext().getBean("showCommand", BaseCommand.class);
                    Store.addToProcessQueue(update);
                    showCommand.execute(dataService);
                    break;
                case "/info", "Инфо ℹ", "Info ℹ", "ℹ":
                    Store.addToSendQueue(chatId, "in process");
                    break;
                default:
                    switch (dataService.getStatus(userId)) {
                        case BASE -> {
                            Store.addToSendQueue(chatId, "no command");
                        }
                        case NAME_WAITING, BIRTHDAY_WAITING -> {
                            Store.addToProcessQueue(message.getText(),update);
                            BaseCommand addCommandForName = MyBean.getApplicationContext().getBean("addCommand", BaseCommand.class);
                            addCommandForName.execute(dataService);
                        }
                    }
            }

        } else if (update.hasCallbackQuery()) {
            Long userId = update.getCallbackQuery().getFrom().getId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String text = update.getCallbackQuery().getData();
            String[] s = text.split(";");
            String data = s[0];
            Store.addToProcessQueue(text,update);
            switch (data) {
                case "setRussian", "setEnglish", "setEmoji":
                    BaseCommand chooseLangCommand = MyBean.getApplicationContext().getBean("chooseLangCommand", BaseCommand.class);
                    chooseLangCommand.execute(dataService);
                    deleteMessage(update.getCallbackQuery().getMessage());
                    break;
                case "backToCalendar","showJanuary", "showFebruary", "showMarch", "showApril", "showMay", "showJune", "showJuly", "showAugust", "showSeptember", "showOctober", "showNovember", "showDecember": {
                    BaseCommand showCommand = MyBean.getApplicationContext().getBean("showCommand", BaseCommand .class);
                    showCommand.execute(dataService);
                    break;
                }
            }

        }
    }

    private void deleteMessage(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(message.getMessageId());
        deleteMessage.setChatId(message.getChatId());
        Store.addToSendQueue(deleteMessage);
    }



    @PostConstruct
    private void sendMessage() {
        new Thread(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            while (true) {
                try {
                    Pair<Long, Object> sendPair = Store.getQueueToSend().take();
                    executorService.execute(() -> {
                        Object o = sendPair.getSecond();
                        if (o.getClass() == SendMessage.class) {
                            SendMessage NewsendMessage = (SendMessage) sendPair.getSecond();
                            NewsendMessage.setChatId(sendPair.getFirst());
                            try {
                                execute(NewsendMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (o.getClass() == EditMessageReplyMarkup.class) {
                            EditMessageReplyMarkup editMessageReplyMarkup = (EditMessageReplyMarkup) sendPair.getSecond();
                            editMessageReplyMarkup.setChatId(sendPair.getFirst());
                            try {
                                execute(editMessageReplyMarkup);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }else if (o.getClass() == DeleteMessage.class) {
                            DeleteMessage deleteMessage = (DeleteMessage) sendPair.getSecond();
                            deleteMessage.setChatId(sendPair.getFirst());
                            try {
                                execute(deleteMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    executorService.shutdown();
                    break;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    log.error("Send message error");
                }
            }
        }).start();
    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
