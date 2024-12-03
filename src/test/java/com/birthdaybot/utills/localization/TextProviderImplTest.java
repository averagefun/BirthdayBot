package com.birthdaybot.utills.localization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextProviderImplTest {

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        new TextProviderImpl(messageSource);
    }

    @Test
    void testLocalizate_ReturnsLocalizedText() {
        String text = "greeting";
        String locale = "es";
        String expected = "Hola";

        when(messageSource.getMessage(eq(text), isNull(), eq(Locale.forLanguageTag(locale))))
                .thenReturn(expected);

        String result = TextProviderImpl.localizate(text, locale);

        assertEquals(expected, result);
        verify(messageSource).getMessage(eq(text), isNull(), eq(Locale.forLanguageTag(locale)));
    }

    @Test
    void testLocalizate_FallbackToEnglishOnException() {
        String text = "greeting";
        String locale = "fr";
        String expected = "Hello";

        when(messageSource.getMessage(eq(text), isNull(), eq(Locale.forLanguageTag(locale))))
                .thenThrow(new RuntimeException("Localization failed"));
        when(messageSource.getMessage(eq(text), isNull(), eq(Locale.ENGLISH)))
                .thenReturn(expected);

        String result = TextProviderImpl.localizate(text, locale);

        assertEquals(expected, result);
        verify(messageSource).getMessage(eq(text), isNull(), eq(Locale.ENGLISH));
    }

    @Test
    void testLocalizate_NullLocale_DefaultsToEnglish() {
        String text = "greeting";
        String expected = "Hello";

        when(messageSource.getMessage(eq(text), isNull(), eq(Locale.ENGLISH)))
                .thenReturn(expected);

        String result = TextProviderImpl.localizate(text, null);

        assertEquals(expected, result);
        verify(messageSource).getMessage(eq(text), isNull(), eq(Locale.ENGLISH));
    }

    @Test
    void testLocalizate_EmptyText_ReturnsEmptyString() {
        String text = "";
        String locale = "de";

        when(messageSource.getMessage(eq(text), isNull(), eq(Locale.forLanguageTag(locale))))
                .thenReturn("");

        String result = TextProviderImpl.localizate(text, locale);

        assertEquals("", result);
        verify(messageSource).getMessage(eq(text), isNull(), eq(Locale.forLanguageTag(locale)));
    }

    @Test
    void testLocalizate_NullText_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                TextProviderImpl.localizate(null, "it"));
    }


}
