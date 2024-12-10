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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.birthdaybot.utills.localization.TextProviderImpl.localizate;

@Slf4j
@Component
@Scope("singleton") // default
@PropertySource("classpath:application.properties") // default
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
        log.info("Bot bean создан. Bot token получен.");
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        // Логируем сам Update (полезно для отладки, хотя userId может пока быть неизвестен)
        log.debug("Получен Update: {}", update);

        // Если в апдейте есть сообщение
        Message message = update.getMessage();
        try {
            if (message != null && message.hasText()) {
                Long userId = message.getFrom().getId();
                Long chatId = message.getChatId();
                String userName = message.getFrom().getUserName();

                // Логируем текст сообщения, userId и userName
                log.info("Пользователь [{}] (ID: {}) отправил сообщение: '{}'",
                        userName, userId, message.getText());

                if (userId.equals(chatId)) {
                    // Личная переписка
                    switch (message.getText()) {
                        case "/start" -> {
                            log.debug("Команда /start (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            startCommand.execute(dataService);
                        }
                        case "/add", "Добавить день рождения \uD83D\uDEAE", "Add a birthday \uD83D\uDEAE",
                                "✍ \uD83C\uDD95 \uD83D\uDC23" -> {
                            log.debug("Команда /add (userId: {})", userId);
                            dataService.updateStatusById(Status.BASE, userId);
                            Store.addToProcessQueue(update);
                            addCommand.execute(dataService);
                        }
                        case "/lang", "Язык \ud83c\uddf7\ud83c\uddfa", "Language \ud83c\uddec\ud83c\udde7",
                                "♻ \ud83d\ude00" -> {
                            log.debug("Команда /lang (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            chooseLangCommand.execute(dataService);
                        }
                        case "/show", "Показать дни рождения \uD83D\uDC41", "Show birthdays \uD83D\uDC41",
                                "\uD83D\uDD22 \uD83C\uDF10 \uD83C\uDF82" -> {
                            log.debug("Команда /show (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            showCommand.execute(dataService);
                        }
                        case "/settings", "Настройки ⚙️", "Settings ⚙️", "⚙️" -> {
                            log.debug("Команда /settings (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            settingsCommand.execute(dataService);
                        }
                        case "/back", "Назад \uD83D\uDD19", "Back \uD83D\uDD19", "\uD83D\uDD19" -> {
                            log.debug("Команда /back (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            backCommand.execute(dataService);
                        }
                        case "Back to user mode \uD83D\uDD19", "Выйти из режима группы \uD83D\uDD19", "\uD83D\uDC6A\uD83D\uDD19" -> {
                            log.debug("Команда 'Back to user mode' (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            backToUserModeCommand.execute(dataService);
                        }
                        case "/share", "Поделиться ➡", "Share ➡", "\uD83E\uDD1D ↖️" -> {
                            log.debug("Команда /share (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            shareCommand.execute(dataService);
                        }
                        case "/time", "Часовой пояс \uD83D\uDD51", "Time zone \uD83D\uDD51", "\uD83D\uDD51" -> {
                            log.debug("Команда /time (userId: {})", userId);
                            Store.addToProcessQueue(update);
                            timeZoneCommand.execute(dataService);
                        }
                        case "/info", "Инфо ℹ", "Info ℹ", "ℹ" -> {
                            log.debug("Команда /info (userId: {})", userId);
                            Store.addToSendQueue(chatId, "in process");
                        }
                        default -> {
                            log.debug("Неизвестная команда (userId: {}). Проверяем статус пользователя...", userId);
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
                    }
                } else {
                    // Обработка группового чата
                    log.debug("Обработка запроса в групповом чате: {}, userId: {}", chatId, userId);

                    List<ChatMember> admins = execute(new GetChatAdministrators(chatId.toString()));
                    boolean isAdmin = admins.stream()
                            .anyMatch(admin -> admin.getUser().getId().equals(userId));

                    switch (message.getText()) {
                        case "/add@BirthdayRemind_bot", "/show@BirthdayRemind_bot" -> {
                            log.debug("Групповой режим. Проверяем права администратора (userId: {}, isAdmin: {})",
                                    userId, isAdmin);
                            Store.addToProcessQueue(Boolean.toString(isAdmin), update);
                            setGroupMode.execute(dataService);
                        }
                        default -> {
                            log.debug("Неизвестная команда в групповом чате (userId: {}): '{}'",
                                    userId, message.getText());
                        }
                    }
                }

            } else if (update.hasCallbackQuery()) {
                // Обработка нажатия inline-кнопок
                Long userId = update.getCallbackQuery().getFrom().getId();
                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                String text = update.getCallbackQuery().getData();
                String[] s = text.split(";");
                String data = s[0];

                log.info("CallbackQuery от userId: {}. Data: {}", userId, text);

                Store.addToProcessQueue(text, update);

                switch (data) {
                    case "setRussian", "setEnglish", "setEmoji" -> {
                        log.debug("Callback смена языка (userId: {}, data: {})", userId, data);
                        chooseLangCommand.execute(dataService);
                        deleteMessage(update.getCallbackQuery().getMessage());
                    }
                    case "backToCalendar", "showJanuary", "showFebruary", "showMarch", "showApril", "showMay",
                            "showJune", "showJuly", "showAugust", "showSeptember", "showOctober", "showNovember",
                            "showDecember" -> {
                        log.debug("Callback работа с календарем (userId: {}, data: {})", userId, data);
                        showCommand.execute(dataService);
                    }
                    default -> {
                        log.debug("Необработанный callback (userId: {}, data: {})", userId, data);
                    }
                }
            }
        } catch (NoAdminRightsException e) {
            log.error("Ошибка прав администратора (userId: {}): {}", e.getUserId(), e.getMessage());
            SendMessage error = new SendMessage(e.getUserId().toString(),
                    localizate("noAdminError", e.getUserLocate()));
            Store.addToSendQueue(error);
        } catch (TelegramApiException te) {
            log.error("Ошибка при работе с Telegram API: ", te);
        } catch (Exception ex) {
            log.error("Неизвестная ошибка: ", ex);
        }
    }

    private void deleteMessage(Message message) {
        log.debug("Удаление сообщения [{}] из чата [{}] (userId: {})",
                message.getMessageId(), message.getChatId(),
                message.getFrom() != null ? message.getFrom().getId() : "unknown");
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(message.getMessageId());
        deleteMessage.setChatId(message.getChatId());
        Store.addToSendQueue(deleteMessage);
    }

    @PostConstruct
    private void sendMessage() {
        log.info("Запуск отдельного потока для отправки сообщений...");
        new Thread(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            while (true) {
                try {
                    Pair<Long, Object> sendPair = Store.getQueueToSend().take();
                    log.debug("Получен объект для отправки: чат = {}, объект = {}",
                            sendPair.getFirst(), sendPair.getSecond());
                    executorService.execute(() -> {
                        Object o = sendPair.getSecond();
                        if (o.getClass() == SendMessage.class) {
                            SendMessage newSendMessage = (SendMessage) o;
                            newSendMessage.setChatId(sendPair.getFirst());
                            try {
                                log.debug("Отправка SendMessage в чат [{}]", sendPair.getFirst());
                                execute(newSendMessage);
                            } catch (TelegramApiException e) {
                                log.error("Ошибка при отправке SendMessage: ", e);
                                throw new RuntimeException(e);
                            }
                        } else if (o.getClass() == EditMessageReplyMarkup.class) {
                            EditMessageReplyMarkup editMessageReplyMarkup = (EditMessageReplyMarkup) o;
                            editMessageReplyMarkup.setChatId(sendPair.getFirst());
                            try {
                                log.debug("Отправка EditMessageReplyMarkup в чат [{}]", sendPair.getFirst());
                                execute(editMessageReplyMarkup);
                            } catch (TelegramApiException e) {
                                log.error("Ошибка при отправке EditMessageReplyMarkup: ", e);
                                throw new RuntimeException(e);
                            }
                        } else if (o.getClass() == DeleteMessage.class) {
                            DeleteMessage deleteMessage = (DeleteMessage) o;
                            deleteMessage.setChatId(sendPair.getFirst());
                            try {
                                log.debug("Отправка DeleteMessage в чат [{}]", sendPair.getFirst());
                                execute(deleteMessage);
                            } catch (TelegramApiException e) {
                                log.error("Ошибка при удалении сообщения: ", e);
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    log.error("Поток отправки сообщений прерван.", e);
                    executorService.shutdown();
                    break;
                } catch (RuntimeException e) {
                    log.error("Ошибка при выполнении задачи отправки сообщений: ", e);
                }
            }
        }).start();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
