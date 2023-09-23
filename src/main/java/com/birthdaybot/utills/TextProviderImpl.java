package com.birthdaybot.utills;

import java.util.Locale;

public class TextProviderImpl implements TextProvider{

    private Locale locale;

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String get(String tag) {
        //todo get localiz from user
        return "";
    }
}
