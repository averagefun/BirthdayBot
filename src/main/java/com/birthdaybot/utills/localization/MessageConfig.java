package com.birthdaybot.utills.localization;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;


import java.util.Locale;

@Configuration
public class MessageConfig {
    //Путь к properties
    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("language/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    //Эта хрень тотали не работает и при не нахождении файла или заголовка ищет в файле messages_Location.ROOT
    //Я не смог это поменять, поэтому прото устанавливаю стандартную локацию пустой
    //3 часа убил на это
    @Bean
    public void localeResolver() {
        Locale.setDefault(new Locale(""));
    }
}