package com.birthdaybot;

import com.birthdaybot.commands.AddCommand;
import com.birthdaybot.commands.BaseCommand;
import com.birthdaybot.commands.StartCommand;
import com.birthdaybot.model.User;
import com.birthdaybot.repositories.UserRepository;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Keyboard;
import com.birthdaybot.utills.Store;
import jakarta.annotation.PostConstruct;
import org.springframework.context.MessageSource;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Component
public class Bot extends TelegramLongPollingBot  {
    private final UserRepository userRepository;

    private final MessageSource messageSource;

    private final DataService dataService;

    private volatile Message message;

    private Map<String, BaseCommand> commands= Map.of(
            "/start", new StartCommand(),
            "/add", new AddCommand()
//            "/help", new HelpCommand(),
//            "/info", new InfoCommand()
    );

    @Value("${telegram.bot.name}")
    private String botUsername;


    public Bot(@Value("${telegram.bot.token}") String botToken, MessageSource messageSource, DataService dataService,
               UserRepository userRepository) {
        super(botToken);
        this.messageSource = messageSource;
        this.dataService = dataService;
        this.userRepository = userRepository;
    }


    @Override
    public void onUpdateReceived(Update update) {
        message = update.getMessage();
        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();

        if(message!=null && message.hasText()){
            switch (message.getText()) {
                case "/start":
                    StartCommand startCommand= (StartCommand) commands.get("/start");
                    startCommand.execute(dataService, chatId, userId, update.getMessage().getFrom().getUserName());
                    break;
                case "/add", "Добавить день рождения \uD83D\uDEBE":

                    break;
            }

        }
        System.out.println(dataService.getStatus(userId));
//        System.out.println(update.getMessage().getText());
//        localizate_text("aboba", new Locale("ru"));
//        SendMessage sm = new SendMessage();
//        sm.setChatId(update.getMessage().getChatId());
//        sm.setText("TEst");
//        sm.setReplyMarkup(Keyboard.replyKeyboardMarkup());
//        Store.addToSendQueue(sm);
    }


    String localizate_text(String text, Locale locale){
        String titleTextWithArgument=messageSource.getMessage(text,null,locale);
        System.out.println(titleTextWithArgument);
        return titleTextWithArgument;
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
                    Pair<Long, Object> sendPair= Store.queueToSend.take();
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
