package com.birthdaybot.utills.validators;

import com.birthdaybot.exceptions.DayFormatException;
import com.birthdaybot.exceptions.FutureDateException;
import com.birthdaybot.exceptions.MonthFormatException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.zip.DataFormatException;

public class Validator {
    private static Map<Integer, Integer> monthMap = new HashMap<>(){{
        put(1,31);
        put(2,28);
        put(3,31);
        put(4,30);
        put(5,31);
        put(6,30);
        put(7,31);
        put(8,31);
        put(9,30);
        put(10,31);
        put(11,30);
        put(12,31);
    }};


    private static String dateRegex="^\\s?\\d{1,2}\\W?\\d{1,2}(\\W?\\d{4})?\\s?$";
    private static String delimiters="\\W";

    private static boolean validateNums(int day, int month, int year) throws DataFormatException{
        if(month<=0 || month>12) throw new MonthFormatException();
        int shouldBe = monthMap.get(month);
        if(year%4==0 && month==2) shouldBe++;
        if(day<=0 || day>shouldBe) throw new DayFormatException();
        if(year==0) return true;
        LocalDate inputDate = LocalDate.of(year, month, day);
        LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return today.isAfter(inputDate);
    }


    //date format <1-2 digits><Any non word><1-2 digits><Any non word><4 digits>
    public static LocalDate parseDate(String date) throws DataFormatException {
        date=date.toLowerCase().trim();
        if(!date.matches(dateRegex)) throw new DataFormatException();
        String[] tokensVal = date.split(delimiters);
        if(tokensVal.length>3 || tokensVal.length<2) throw new DataFormatException();
        int day = Integer.parseInt(tokensVal[0]);
        int month = Integer.parseInt(tokensVal[1]);
        int year=1;
        if(tokensVal.length==3)
            year = Integer.parseInt(tokensVal[2]);
        if(!validateNums(day, month, year)) throw new FutureDateException();
        return LocalDate.of(year, month, day);
    }
}
