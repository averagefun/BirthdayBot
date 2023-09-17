package com.birthdaybot;

import com.birthdaybot.model.User;
import com.birthdaybot.repositories.UserRepository;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Keyboard;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;



@Slf4j
@Component
public class Bot extends TelegramLongPollingBot  {
    private final UserRepository userRepository;

    private final MessageSource messageSource;

    private final DataService dataService;

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
        System.out.println(update.getMessage().getText());
        localizate_text("aboba", new Locale("ru"));
        SendMessage sm = new SendMessage();
        sm.setChatId(update.getMessage().getChatId());
        sm.setText("TEst");
        sm.setReplyMarkup(Keyboard.replyKeyboardMarkup());
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        createNewUser(update.getMessage().getFrom());

    }

    boolean createNewUser(org.telegram.telegrambots.meta.api.objects.User user){
        Long userId = user.getId();
        if(userRepository.existsById(userId)) {
            return false;
        }
        String userName = user.getUserName();
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUsername(userName);
        dataService.addUser(newUser);
        return true;
    }

    String localizate_text(String text, Locale locale){
        String titleTextWithArgument=messageSource.getMessage(text,null,locale);
        System.out.println(titleTextWithArgument);
        return titleTextWithArgument;
    }



    private void startBot(long chatId) {

    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
