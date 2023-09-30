package com.birthdaybot;

import com.birthdaybot.commands.AddCommand;
import com.birthdaybot.commands.BaseCommand;
import com.birthdaybot.commands.ChooseLangCommand;
import com.birthdaybot.commands.StartCommand;
import com.birthdaybot.model.Status;
import com.birthdaybot.repositories.UserRepository;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Store;
import com.birthdaybot.utills.localization.TextProviderImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.context.MessageSource;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Component
public class Bot extends TelegramLongPollingBot  {
    private final UserRepository userRepository;
    private final DataService dataService;

    private volatile Message message;

    private Map<String, BaseCommand> commands= Map.of(
            "/start", new StartCommand(),
            "/add", new AddCommand(),
            "/lang", new ChooseLangCommand()
//            "/help", new HelpCommand(),
//            "/info", new InfoCommand()
    );

    @Value("${telegram.bot.name}")
    private String botUsername;


    public Bot(@Value("${telegram.bot.token}") String botToken, MessageSource messageSource, DataService dataService,
               UserRepository userRepository) {
        super(botToken);
        this.dataService = dataService;
        this.userRepository = userRepository;
        TextProviderImpl.setMessageSource(messageSource);
    }


    @Override
    public void onUpdateReceived(Update update) {
        message = update.getMessage();
        if(message!=null && message.hasText()){
            Long userId = update.getMessage().getFrom().getId();
            Long chatId = update.getMessage().getChatId();
            System.out.println(update.getMessage().getFrom().getUserName() + " " + update.getMessage().getText());
            switch (message.getText()) {
                case "/start":
                    StartCommand startCommand= (StartCommand) commands.get("/start");
                    startCommand.execute(dataService, chatId, userId, update.getMessage().getFrom().getUserName() + " " + update.getMessage().getFrom().getLanguageCode());
                    break;
                case "/add", "Добавить день рождения \uD83D\uDEAE", "Add a birthday \uD83D\uDEAE", "✍ \uD83C\uDD95 \uD83D\uDC23":
                    dataService.updateStatusById(Status.BASE, userId);
                    AddCommand addCommand= (AddCommand) commands.get("/add");
                    addCommand.execute(dataService, chatId, userId, "");
                    break;
                case "/lang", "Язык \ud83c\uddf7\ud83c\uddfa", "Language \ud83c\uddec\ud83c\udde7", "♻ \ud83d\ude00":
                    ChooseLangCommand chooseLangCommand = (ChooseLangCommand) commands.get("/lang");
                    chooseLangCommand.execute(dataService, chatId, userId, "");
                    break;
                default:
                    switch (dataService.getStatus(userId)) {
                        case BASE -> {
                            Store.addToSendQueue(chatId, "no command");
                        }
                        case NAME_WAITING, BIRTHDAY_WAITING -> {
                            AddCommand addCommandforName= (AddCommand) commands.get("/add");
                            addCommandforName.execute(dataService, chatId, userId, message.getText());
                        }
                    }
            }

        }else if(update.hasCallbackQuery()){
            Long userId = update.getCallbackQuery().getFrom().getId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data=update.getCallbackQuery().getData();
            switch (data){
                case "setRussian", "setEnglish", "setEmoji":
                    ChooseLangCommand chooseLangCommand = (ChooseLangCommand) commands.get("/lang");
                    chooseLangCommand.execute(dataService, chatId, userId, data);
                    break;
            }
            deleteMessage(update.getCallbackQuery().getMessage());
        }
    }

    private void deleteMessage(Message message){
        DeleteMessage deleteMessage=new DeleteMessage();
        deleteMessage.setMessageId(message.getMessageId());
        deleteMessage.setChatId(message.getChatId());
        Store.addToSendQueue(deleteMessage);
    }


    @PostConstruct
    private void startBot() {
        sendMessage();
    }

    private void sendMessage(){
        new Thread(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            while (true){
                try {
                    Pair<Long, Object> sendPair= Store.getQueueToSend() .take();
                    executorService.execute(()->{
                        Object o = sendPair.getSecond();
                        if(o.getClass()==SendMessage.class){
                            SendMessage NewsendMessage= (SendMessage) sendPair.getSecond();
                            NewsendMessage.setChatId(sendPair.getFirst());
                            try {
                                execute(NewsendMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }else if(o.getClass()==DeleteMessage.class){
                            DeleteMessage deleteMessage= (DeleteMessage) sendPair.getSecond();
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
                }catch (RuntimeException e){
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
