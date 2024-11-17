package com.birthdaybot;

import com.birthdaybot.commands.*;
import com.birthdaybot.exceptions.NoAdminRightsException;
import com.birthdaybot.model.enums.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Store;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.birthdaybot.utills.localization.TextProviderImpl.localizate;


@Slf4j
@Component
@Scope("singleton")//default
@PropertySource("classpath:application.properties")//default
public class Bot extends TelegramLongPollingBot {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    @Qualifier("dataService")
    private DataService dataService;

    @Value("${telegram.bot.name}")
    private String botUsername;

    @Autowired
    @Qualifier("startCommand")
    private BaseCommand startCommand;

    @Autowired
    @Qualifier("addCommand")
    private BaseCommand addCommand;

    @Autowired
    @Qualifier("chooseLangCommand")
    private BaseCommand chooseLangCommand;

    @Autowired
    @Qualifier("showCommand")
    private BaseCommand showCommand;

    @Autowired
    @Qualifier("settingsCommand")
    private BaseCommand settingsCommand;

    @Autowired
    @Qualifier("shareCommand")
    private BaseCommand shareCommand;

    @Autowired
    @Qualifier("backCommand")
    private BaseCommand backCommand;

    @Autowired
    @Qualifier("backToUserModeCommand")
    private BaseCommand backToUserModeCommand;

    @Autowired
    @Qualifier("timeZoneCommand")
    private BaseCommand timeZoneCommand;

    @Autowired
    @Qualifier("setGroupMode")
    private BaseCommand setGroupMode;

    public Bot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        try {

            if (message != null && message.hasText()) {
                Long userId = update.getMessage().getFrom().getId();
                Long chatId = update.getMessage().getChatId();
                log.info(update.getMessage().getFrom().getUserName() + " " + update.getMessage().getText());
                if (userId.equals(chatId)) {
                    switch (message.getText()) {
                        case "/start":
                            Store.addToProcessQueue(update);
                            startCommand.execute(dataService);
                            break;
                        case "/add", "Добавить день рождения \uD83D\uDEAE", "Add a birthday \uD83D\uDEAE",
                             "✍ \uD83C\uDD95 \uD83D\uDC23":
                            dataService.updateStatusById(Status.BASE, userId);
                            Store.addToProcessQueue(update);
                            addCommand.execute(dataService);
                            break;
                        case "/lang", "Язык \ud83c\uddf7\ud83c\uddfa", "Language \ud83c\uddec\ud83c\udde7",
                             "♻ \ud83d\ude00":
                            Store.addToProcessQueue(update);
                            chooseLangCommand.execute(dataService);
                            break;
                        case "/show", "Показать дни рождения \uD83D\uDC41", "Show birthdays \uD83D\uDC41",
                             "\uD83D\uDD22 \uD83C\uDF10 \uD83C\uDF82":
                            Store.addToProcessQueue(update);
                            showCommand.execute(dataService);
                            break;
                        case "/settings", "Настройки ⚙️", "Settings ⚙️", "⚙️":
                            Store.addToProcessQueue(update);
                            settingsCommand.execute(dataService);
                            break;
                        case "/back", "Назад \uD83D\uDD19", "Back \uD83D\uDD19", "\uD83D\uDD19":
                            Store.addToProcessQueue(update);
                            backCommand.execute(dataService);
                            break;
                        case "Back to user mode \uD83D\uDD19", "Выйти из режима группы \uD83D\uDD19", "\uD83D\uDC6A\uD83D\uDD19":
                            Store.addToProcessQueue(update);
                            backToUserModeCommand.execute(dataService);
                            break;
                        case "/share", "Поделиться ➡", "Share ➡", "\uD83E\uDD1D ↖️":
                            Store.addToProcessQueue(update);
                            shareCommand.execute(dataService);
                            break;
                        case "/time", "Часовой пояс \uD83D\uDD51", "Time zone \uD83D\uDD51", "\uD83D\uDD51":
                            Store.addToProcessQueue(update);
                            timeZoneCommand.execute(dataService);
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
                                    Store.addToProcessQueue(message.getText(), update);
                                    addCommand.execute(dataService);
                                }
                                case TIME_ZONE_WAITING -> {
                                    Store.addToProcessQueue(update);
                                    timeZoneCommand.execute(dataService);
                                }
                            }
                    }
                } else {
                    List<ChatMember> admins = execute(new GetChatAdministrators(chatId.toString()));
                    boolean isAdmin = admins.stream()
                            .anyMatch(admin -> admin.getUser().getId().equals(userId));
                    switch (message.getText()) {
                        case "/add@BirthdayRemind_bot", "/show@BirthdayRemind_bot":
                            Store.addToProcessQueue(Boolean.toString(isAdmin), update);
                            setGroupMode.execute(dataService);
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
                        chooseLangCommand.execute(dataService);
                        deleteMessage(update.getCallbackQuery().getMessage());
                        break;
                    case "backToCalendar","showJanuary", "showFebruary", "showMarch", "showApril", "showMay", "showJune", "showJuly", "showAugust", "showSeptember", "showOctober", "showNovember", "showDecember": {
                        showCommand.execute(dataService);
                        break;
                    }
                }
            }
        }catch (NoAdminRightsException e){
            SendMessage error = new SendMessage(e.getUserId().toString(), localizate("noAdminError", e.getUserLocate()));
            Store.addToSendQueue(error);
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
