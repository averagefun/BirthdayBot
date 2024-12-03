package com.birthdaybot.utills;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmojiConverterTest {

    @Test
    void testConvertStringWithDigits() {
        String input = "123";
        String expected = "1️⃣2️⃣3️⃣";
        assertEquals(expected, EmojiConverter.convertedString(input));
    }

    @Test
    void testConvertStringWithDash() {
        String input = "2024-12-24";
        String expected = "2️⃣0️⃣2️⃣4️⃣➖1️⃣2️⃣➖2️⃣4️⃣";
        assertEquals(expected, EmojiConverter.convertedString(input));
    }

    @Test
    void testConvertStringWithSpaces() {
        String input = "1 2 3";
        String expected = "1️⃣ 2️⃣ 3️⃣";
        assertEquals(expected, EmojiConverter.convertedString(input));
    }

    @Test
    void testConvertStringWithUnsupportedCharacters() {
        String input = "abc";
        assertThrows(NullPointerException.class, () -> {
            EmojiConverter.convertedString(input);
        });
    }

    @Test
    void testConvertEmptyString() {
        String input = "";
        String expected = "";
        assertEquals(expected, EmojiConverter.convertedString(input));
    }

    @Test
    void testConvertStringWithMixedCharacters() {
        String input = "12a 3b";
        assertThrows(NullPointerException.class, () -> {
            EmojiConverter.convertedString(input);
        });
    }
}
