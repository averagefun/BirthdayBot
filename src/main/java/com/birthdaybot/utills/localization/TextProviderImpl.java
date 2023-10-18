package com.birthdaybot.utills.localization;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TextProviderImpl {


    @Setter
    private static MessageSource messageSource;

    @Autowired(required = true)
    public TextProviderImpl(@Qualifier("messageSource")
                                MessageSource messageSource) {
        TextProviderImpl.messageSource =messageSource;
    }

    public TextProviderImpl() {
    }

    public static String localizate(String text, String locale) {
        String localizedtext="";
        try {
            Locale loc = Locale.forLanguageTag(locale);
            localizedtext=messageSource.getMessage(text,null,loc);
        }catch (Exception e){
            localizedtext= messageSource.getMessage(text,null,Locale.forLanguageTag("en"));
        }
        return localizedtext;
    }

}
