package com.birthdaybot.commands;

import com.birthdaybot.model.User;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Store;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ShareCommand extends BaseCommand{
    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long chatId = getChatId(update);
        Long userId = getUserId(update);
        String langCode = dataService.getLanguageCode(userId);
        User user = dataService.getUser(userId);
        if(user.getShareCode()==null){
            user.generateShareCode();
        }
        dataService.addUser(user);
        Long shareCode = user.getShareCode();
        Store.addToSendQueue(chatId, localizate("yourShareCode", langCode) + " " + shareCode);

    }
}
