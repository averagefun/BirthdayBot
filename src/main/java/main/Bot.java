package main;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${telegram.bot.name}")
    private String botUsername;


    public Bot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
    }


    private void startBot(long chatId) {

    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
