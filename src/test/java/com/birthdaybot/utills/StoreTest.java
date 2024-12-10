package com.birthdaybot.utills;

import com.birthdaybot.exceptions.RestartServerException;
import com.birthdaybot.model.Alarm;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import java.util.concurrent.BlockingDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreTest {

    @Mock
    private Update updateMock;

    @Mock
    private User userMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Store.tempMap.clear();
        Store.getQueueToProcess().clear();
        Store.getQueueToSend().clear();
    }

    @ParameterizedTest
    @MethodSource("provideSendMessages")
    public void testAddToSendQueueParameterized(SendMessage sendMessage, long expectedChatId, String expectedText) {
        Store.addToSendQueue(sendMessage);

        BlockingDeque<Pair<Long, Object>> queue = Store.getQueueToSend();
        assertEquals(1, queue.size());
        assertEquals(expectedChatId, queue.peek().getFirst());
        assertEquals(expectedText, ((SendMessage) queue.peek().getSecond()).getText());
    }

    static List<Arguments> provideSendMessages() {
        SendMessage message1 = new SendMessage();
        message1.setChatId("12345");
        message1.setText("Hello!");

        SendMessage message2 = new SendMessage();
        message2.setChatId("67890");
        message2.setText("Goodbye!");

        return List.of(
                Arguments.of(message1, 12345L, "Hello!"),
                Arguments.of(message2, 67890L, "Goodbye!")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUsersAndChatIds")
    public void testCreateBirthdayParameterized(long userId, long chatId) {
        when(userMock.getId()).thenReturn(userId);

        Store.createBirthday(userMock, chatId);

        Birthday birthday = Store.tempMap.get(userId);
        assertNotNull(birthday);
        assertEquals(userId, birthday.getId());
        assertEquals(userMock, birthday.getOwner());
        assertEquals(chatId, birthday.getChatId());
    }

    static List<Arguments> provideUsersAndChatIds() {
        return List.of(
                Arguments.of(1L, 12345L),
                Arguments.of(2L, 67890L)
        );
    }

    @Test
    public void testGetBirthday_Success() {
        Birthday birthday = new Birthday();
        birthday.setId(1L);
        Store.tempMap.put(1L, birthday);

        Birthday result = Store.getBirthday(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetBirthday_ThrowsException() {
        assertThrows(RestartServerException.class, () -> Store.getBirthday(99L));
    }

    @Test
    public void testAddToProcessQueue_StringAndUpdate() {
        Store.addToProcessQueue("TestString", updateMock);

        BlockingDeque<Pair<String, Update>> queue = Store.getQueueToProcess();
        assertEquals(1, queue.size());
        assertEquals("TestString", queue.peek().getFirst());
        assertEquals(updateMock, queue.peek().getSecond());
    }

    @Test
    public void testAddToProcessQueue_OnlyUpdate() {
        Store.addToProcessQueue(updateMock);

        BlockingDeque<Pair<String, Update>> queue = Store.getQueueToProcess();
        assertEquals(1, queue.size());
        assertEquals("", queue.peek().getFirst());
        assertEquals(updateMock, queue.peek().getSecond());
    }

    @Test
    public void testCreateAlarmFromBirthday() {
        Birthday birthday = new Birthday();
        birthday.setDate(LocalDate.of(2000, 12, 25));

        Alarm alarm = Store.createAlarmFromBirthday(birthday);
        assertNotNull(alarm);
        assertEquals(birthday, alarm.getBirthday());
    }
}
