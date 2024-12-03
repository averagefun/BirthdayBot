package com.birthdaybot;

import com.birthdaybot.commands.BaseCommand;
import com.birthdaybot.model.enums.Status;
import com.birthdaybot.services.DataService;
import com.birthdaybot.utills.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Тестовый класс, который показывает, как правильно мокать Bot с помощью spy,
 * чтобы можно было замокать (stub) вызовы execute(...).
 */
class BotTest {

    // Все зависимости, которые в реальном приложении @Autowired в Bot
    @Mock
    private DataService dataService;

    @Mock
    private BaseCommand startCommand;

    @Mock
    private BaseCommand addCommand;

    @Mock
    private BaseCommand chooseLangCommand;

    @Mock
    private BaseCommand showCommand;

    @Mock
    private BaseCommand settingsCommand;

    @Mock
    private BaseCommand shareCommand;

    @Mock
    private BaseCommand backCommand;

    @Mock
    private BaseCommand backToUserModeCommand;

    @Mock
    private BaseCommand timeZoneCommand;

    @Mock
    private BaseCommand setGroupMode;

    // Реальный объект Bot (конструктор с токеном)
    private Bot realBot;

    // Spy-объект на Bot, чтобы мокать execute(...)
    private Bot spyBot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 1. Создаем реальный объект Bot
        realBot = new Bot("dummyToken");

        // 2. Делаем spy. Теперь spyBot "оборачивает" realBot, но для Mockito это mock/spy
        spyBot = spy(realBot);

        // 3. "Вкалываем" моки во все поля Bot, где в реальном коде были бы @Autowired
        ReflectionTestUtils.setField(spyBot, "dataService", dataService);
        ReflectionTestUtils.setField(spyBot, "startCommand", startCommand);
        ReflectionTestUtils.setField(spyBot, "addCommand", addCommand);
        ReflectionTestUtils.setField(spyBot, "chooseLangCommand", chooseLangCommand);
        ReflectionTestUtils.setField(spyBot, "showCommand", showCommand);
        ReflectionTestUtils.setField(spyBot, "settingsCommand", settingsCommand);
        ReflectionTestUtils.setField(spyBot, "shareCommand", shareCommand);
        ReflectionTestUtils.setField(spyBot, "backCommand", backCommand);
        ReflectionTestUtils.setField(spyBot, "backToUserModeCommand", backToUserModeCommand);
        ReflectionTestUtils.setField(spyBot, "timeZoneCommand", timeZoneCommand);
        ReflectionTestUtils.setField(spyBot, "setGroupMode", setGroupMode);

        // Очищаем очереди Store перед каждым тестом
        Store.getQueueToProcess().clear();
        Store.getQueueToSend().clear();
    }

    // -------------------------- Тесты ---------------------------------

    @Test
    void testOnUpdateReceived_startCommand() throws TelegramApiException, InterruptedException {
        Update update = createTextUpdate(123L, 123L, "/start");
        spyBot.onUpdateReceived(update);

        verify(startCommand, times(1)).execute(dataService);
        verify(addCommand, never()).execute(any());
    }

    @Test
    void testOnUpdateReceived_addCommand() throws TelegramApiException, InterruptedException {
        Update update = createTextUpdate(1000L, 1000L, "/add");
        when(dataService.getStatus(1000L)).thenReturn(Status.BASE);

        spyBot.onUpdateReceived(update);

        verify(addCommand, times(1)).execute(dataService);
        verify(dataService, times(1)).updateStatusById(Status.BASE, 1000L);
    }

    @Test
    void testOnUpdateReceived_timeZoneCommand() throws TelegramApiException, InterruptedException {
        Update update = createTextUpdate(2000L, 2000L, "/time");
        spyBot.onUpdateReceived(update);

        verify(timeZoneCommand, times(1)).execute(dataService);
    }

    @Test
    void testOnUpdateReceived_inStatusWaiting() throws TelegramApiException, InterruptedException {
        // NAME_WAITING
        Update updateName = createTextUpdate(3000L, 3000L, "Любое имя");
        when(dataService.getStatus(3000L)).thenReturn(Status.NAME_WAITING);

        spyBot.onUpdateReceived(updateName);
        verify(addCommand, times(1)).execute(dataService);

        // TIME_ZONE_WAITING
        Update updateTimeZone = createTextUpdate(3001L, 3001L, "GMT+3");
        when(dataService.getStatus(3001L)).thenReturn(Status.TIME_ZONE_WAITING);

        spyBot.onUpdateReceived(updateTimeZone);
        verify(timeZoneCommand, times(1)).execute(dataService);
    }

    @Test
    void testOnUpdateReceived_inGroupChatUserIsAdmin() throws Exception {
        // Команда /add@BirthdayRemind_bot в групповом чате
        Update updateGroupAdd = createTextUpdate(777L, -100123456789L, "/add@BirthdayRemind_bot");

        // Список админов
        List<ChatMember> admins = new ArrayList<>();
        ChatMemberAdministrator adminMember = new ChatMemberAdministrator();
        User user = new User();
        user.setId(777L);
        adminMember.setUser(user);
        admins.add(adminMember);

        // ВАЖНО: подменяем вызов execute(...) у SPY, а не у realBot
        doReturn(admins).when(spyBot).execute(any(GetChatAdministrators.class));

        spyBot.onUpdateReceived(updateGroupAdd);

        verify(setGroupMode, times(1)).execute(dataService);
    }

    // --------------------- Утилитный метод ----------------------------
    private Update createTextUpdate(Long userId, Long chatId, String text) {
        Update update = new Update();
        Message message = new Message();

        User user = new User();
        user.setId(userId);

        Chat chat = new Chat();
        chat.setId(chatId);

        message.setFrom(user);
        message.setChat(chat);
        message.setText(text);

        update.setMessage(message);
        return update;
    }
}
