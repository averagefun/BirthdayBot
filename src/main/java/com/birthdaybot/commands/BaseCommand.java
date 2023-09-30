package com.birthdaybot.commands;

import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.localization.TextProviderImpl;

public abstract class BaseCommand extends TextProviderImpl {
    abstract void execute(DataService dataService, Long chatId, Long userId, String text);
}