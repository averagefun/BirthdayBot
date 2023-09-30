package com.birthdaybot.utills.localization;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class TextProviderImpl {


    @Setter
    private static MessageSource messageSource;

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
