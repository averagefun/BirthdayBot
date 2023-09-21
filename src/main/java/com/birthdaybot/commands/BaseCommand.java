package com.birthdaybot.commands;

import com.birthdaybot.services.DataService;

public abstract class BaseCommand {
    abstract void execute(DataService dataService, Long chatId, Long userId, String text);
}