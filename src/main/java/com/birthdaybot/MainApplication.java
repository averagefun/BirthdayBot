package com.birthdaybot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@EnableScheduling
@SpringBootApplication
public class MainApplication implements CommandLineRunner {

    private final Bot myTelegramBot;

    public MainApplication(Bot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }

    public static void main(String[] args)  {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(myTelegramBot);
    }

}
