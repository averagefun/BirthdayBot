package com.birthdaybot.commands;

import com.birthdaybot.exceptions.TimeZoneException;
import com.birthdaybot.model.enums.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.EmojiConverter;
import com.birthdaybot.utills.Store;
import com.birthdaybot.utills.validators.Validator;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class TimeZoneCommand extends BaseCommand{
    @Override
    public void execute(DataService dataService) throws InterruptedException {
        Pair<String, Update> executePair = Store.getQueueToProcess().take();
        Update update = executePair.getSecond();
        Long userId = getUserId(update);
        Status curStatus = dataService.getStatus(userId);
        String userLocate = dataService.getLanguageCode(userId);
        if(curStatus!=Status.TIME_ZONE_WAITING){
            Integer timeZone = dataService.getTimeZone(userId);
            String gmt="GMT";
            if(timeZone>0){
                gmt+="+";
            }
            SendMessage sendMessage = new SendMessage(userId.toString(), "timeZone" + " " +gmt+ timeZone);
            Store.addToSendQueue(sendMessage);
            SendMessage sm2 = new SendMessage(userId.toString(), "newTimeZone");
            Store.addToSendQueue(sm2);
            dataService.updateStatusById(Status.TIME_ZONE_WAITING, userId);
        }else {
            SendMessage message = new SendMessage();
            message.setChatId(userId);
            try {
                Integer newTimeZone = Validator.validateTimeZone(update.getMessage().getText());
                dataService.setTimeZone(newTimeZone, userId);
                String gmt="GMT";
                if(newTimeZone>0){
                    gmt+="+";
                }
                String response = "successTimeZone";
                response += userLocate.equals("em") ? EmojiConverter.convertedString(newTimeZone.toString()) : " "+ gmt + newTimeZone;
                message.setText(response);
                dataService.updateStatusById(Status.BASE, userId);
            }catch (TimeZoneException e){
                message.setText("timeZoneFormat");
            }catch (Exception e){
                message.setText("wrongTimeZone");
            }
            finally {
                Store.addToSendQueue(message);
            }
        }
    }
}
