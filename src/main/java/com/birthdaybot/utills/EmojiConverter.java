package com.birthdaybot.utills;

import java.util.Map;

public class EmojiConverter {
    private static final Map<Character, String> emojiMap = Map.ofEntries(
            Map.entry('0', "0️⃣"),
            Map.entry('1', "1️⃣"),
            Map.entry('2', "2️⃣"),
            Map.entry('3', "3️⃣"),
            Map.entry('4', "4️⃣"),
            Map.entry('5', "5️⃣"),
            Map.entry('6', "6️⃣"),
            Map.entry('7', "7️⃣"),
            Map.entry('8', "8️⃣"),
            Map.entry('9', "9️⃣"),
            Map.entry('-', "➖"),
            Map.entry(' ', " ")

    );
    public static String convertedString(String s){
        StringBuilder emojiString= new StringBuilder();
        for(char c:s.toCharArray()){
            emojiString.append(emojiMap.get(c));
        }
        return emojiString.toString();
    }
}
