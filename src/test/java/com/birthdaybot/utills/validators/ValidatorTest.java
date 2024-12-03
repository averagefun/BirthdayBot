package com.birthdaybot.utills.validators;

import com.birthdaybot.exceptions.DayFormatException;
import com.birthdaybot.exceptions.FutureDateException;
import com.birthdaybot.exceptions.LongNameException;
import com.birthdaybot.exceptions.MonthFormatException;
import com.birthdaybot.exceptions.TimeZoneException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.zip.DataFormatException;

public class ValidatorTest {

    @ParameterizedTest
    @CsvSource({
            "12-05-2023, 2023, 5, 12",
            "01-01-2000, 2000, 1, 1",
            "29-02-2020, 2020, 2, 29"
    })
    public void testParseDate_ValidDate(String input, int year, int month, int day) throws DataFormatException {
        LocalDate date = Validator.parseDate(input);
        assertEquals(LocalDate.of(year, month, day), date);
    }

    @ParameterizedTest
    @ValueSource(strings = {"31-04-2023", "32-01-2023", "30-02-2023"})
    public void testParseDate_InvalidDay(String input) {
        assertThrows(DayFormatException.class, () -> Validator.parseDate(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"15-13-2023", "10-0-2023"})
    public void testParseDate_InvalidMonth(String input) {
        assertThrows(MonthFormatException.class, () -> Validator.parseDate(input));
    }

    @Test
    public void testParseDate_FutureDate() {
        assertThrows(FutureDateException.class, () -> Validator.parseDate("15-12-9999"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd-ef-ghij", "123-xy-4567", "45-67-8901"})
    public void testParseDate_InvalidFormat(String input) {
        assertThrows(DataFormatException.class, () -> Validator.parseDate(input));
    }

    @ParameterizedTest
    @CsvSource({"5, 5", "0, 0", "-5, -5"})
    public void testValidateTimeZone_ValidZone(String input, int expected) {
        assertEquals(expected, Validator.validateTimeZone(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-13", "15"})
    public void testValidateTimeZone_InvalidZone(String input) {
        assertThrows(TimeZoneException.class, () -> Validator.validateTimeZone(input));
    }

    @Test
    public void testValidateName_ValidName() {
        assertDoesNotThrow(() -> Validator.validateName("John Doe"));
    }

    @ParameterizedTest
    @CsvSource({"135", "150"})
    public void testValidateName_TooLongName(int length) {
        String longName = "a".repeat(length);
        assertThrows(LongNameException.class, () -> Validator.validateName(longName));
    }
}
