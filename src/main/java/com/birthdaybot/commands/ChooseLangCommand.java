package com.birthdaybot.commands;


import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Scope("singleton")//default
public class ChooseLangCommand extends BaseCommand {
    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long userId = getUserId(update);
        String text = executePair.getFirst();
        if (text.isEmpty()) {
            SendMessage sendMessage = new SendMessage(userId.toString(), localizate("chooseLanguage", dataService.getLanguageCode(userId)));
            sendMessage.setReplyMarkup(Keyboard.languageKeyboard());
            Store.addToSendQueue(sendMessage);
        } else {
            String locate = "";
            switch (text) {
                case "setRussian" -> {
                    locate = "ru";
                }
                case "setEnglish" -> {
                    locate = "en";
                }
                case "setEmoji" -> {
                    locate = "em";
                }
            }
            dataService.updateLangById(locate, userId);
            SendMessage sendMessage = new SendMessage(userId.toString(), localizate("changedLanguage", locate));
            sendMessage.setReplyMarkup(Keyboard.replyKeyboardMarkup(locate));
            Store.addToSendQueue(sendMessage);
        }
    }
}
